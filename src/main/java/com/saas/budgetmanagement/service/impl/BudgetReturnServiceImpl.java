package com.saas.budgetmanagement.service.impl;

import com.saas.budgetmanagement.dto.common.CoaSegmentReference;
import com.saas.budgetmanagement.dto.requestDto.ApprovalDecisionRequest;
import com.saas.budgetmanagement.dto.requestDto.CreateBudgetReturnRequest;
import com.saas.budgetmanagement.dto.responseDto.BudgetReturnResponse;
import com.saas.budgetmanagement.enums.BudgetReturnStatus;
import com.saas.budgetmanagement.exception.BudgetNotFoundException;
import com.saas.budgetmanagement.exception.InvalidBudgetReturnStateException;
import com.saas.budgetmanagement.mapper.BudgetReturnMapper;
import com.saas.budgetmanagement.model.BudgetAllocation;
import com.saas.budgetmanagement.model.BudgetReturn;
import com.saas.budgetmanagement.model.BudgetLedger;
import com.saas.budgetmanagement.repository.BudgetAllocationRepository;
import com.saas.budgetmanagement.repository.BudgetLedgerRepository;
import com.saas.budgetmanagement.repository.BudgetReturnRepository;
import com.saas.budgetmanagement.service.BudgetEventPublisherService;
import com.saas.budgetmanagement.service.BudgetLedgerService;
import com.saas.budgetmanagement.service.BudgetReturnService;
import com.saas.budgetmanagement.utility.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BudgetReturnServiceImpl implements BudgetReturnService {

    private static final String ROUTING_RETURN_REQUESTED = "budget.return.requested";
    private static final String ROUTING_RETURN_APPROVED = "budget.return.approved";

    private final BudgetReturnRepository budgetReturnRepository;
    private final BudgetAllocationRepository budgetAllocationRepository;
    private final BudgetLedgerRepository budgetLedgerRepository;
    private final BudgetLedgerService budgetLedgerService;
    private final BudgetEventPublisherService eventPublisherService;
    private final BudgetReturnMapper budgetReturnMapper;
    private final SecurityUtil securityUtil;

    @Override
    @Transactional
    public BudgetReturnResponse requestReturn(CreateBudgetReturnRequest request) {
        UUID tenantId = currentTenantId();
        BudgetAllocation allocation = findAllocation(tenantId, request.getAllocationId());

        if (request.getAmount().compareTo(allocation.getAllocatedAmount()) > 0) {
            throw new InvalidBudgetReturnStateException(
                    "Return amount exceeds the allocation's current allocated amount");
        }

        BudgetReturn budgetReturn = new BudgetReturn();
        budgetReturn.setTenantId(tenantId);
        budgetReturn.setAllocationId(allocation.getId());
        budgetReturn.setAmount(request.getAmount());
        budgetReturn.setReason(request.getReason());
        budgetReturn.setStatus(BudgetReturnStatus.REQUESTED);
        budgetReturn.setRequestedBy(UUID.fromString(securityUtil.getUserId()));

        BudgetReturn saved = budgetReturnRepository.save(budgetReturn);
        eventPublisherService.publish(
                "budget.return.requested", ROUTING_RETURN_REQUESTED,
                budgetReturnMapper.toRequestedEvent(saved));
        return budgetReturnMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public BudgetReturnResponse approve(UUID id, ApprovalDecisionRequest request) {
        UUID tenantId = currentTenantId();
        BudgetReturn budgetReturn = findReturn(tenantId, id);

        if (budgetReturn.getStatus() != BudgetReturnStatus.REQUESTED) {
            throw new InvalidBudgetReturnStateException(
                    "Only REQUESTED returns can be approved. Current status: " + budgetReturn.getStatus());
        }

        BudgetAllocation allocation = findAllocation(tenantId, budgetReturn.getAllocationId());
        CoaSegmentReference scope = scopeOf(allocation);
        BudgetLedger ledger = budgetLedgerRepository
                .findForUpdateByScope(tenantId, allocationFiscalYearId(tenantId, allocation), allocation.getPeriodId(),
                        scope.getAccountId(), scope.getCostCenterId(), scope.getDepartmentId(),
                        scope.getFundSourceId(), scope.getProjectId())
                .orElseThrow(() -> new InvalidBudgetReturnStateException(
                        "No budget ledger exists for the allocation's scope"));

        if (ledger.getAvailableAmount().compareTo(budgetReturn.getAmount()) < 0) {
            throw new InvalidBudgetReturnStateException(
                    "Return amount exceeds the allocation's currently available amount");
        }

        allocation.setAllocatedAmount(allocation.getAllocatedAmount().subtract(budgetReturn.getAmount()));
        budgetAllocationRepository.save(allocation);
        budgetLedgerService.applyAllocationDelta(
                tenantId, ledger.getFiscalYearId(), allocation.getPeriodId(), scope,
                budgetReturn.getAmount().negate());

        budgetReturn.setStatus(BudgetReturnStatus.APPROVED);
        budgetReturn.setApprovedBy(UUID.fromString(securityUtil.getUserId()));
        BudgetReturn saved = budgetReturnRepository.save(budgetReturn);
        eventPublisherService.publish(
                "budget.return.approved", ROUTING_RETURN_APPROVED,
                budgetReturnMapper.toApprovedEvent(saved));
        return budgetReturnMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public BudgetReturnResponse reject(UUID id, ApprovalDecisionRequest request) {
        UUID tenantId = currentTenantId();
        BudgetReturn budgetReturn = findReturn(tenantId, id);

        if (budgetReturn.getStatus() != BudgetReturnStatus.REQUESTED) {
            throw new InvalidBudgetReturnStateException(
                    "Only REQUESTED returns can be rejected. Current status: " + budgetReturn.getStatus());
        }

        budgetReturn.setStatus(BudgetReturnStatus.REJECTED);
        return budgetReturnMapper.toResponse(budgetReturnRepository.save(budgetReturn));
    }

    @Override
    public BudgetReturnResponse getById(UUID id) {
        return budgetReturnMapper.toResponse(findReturn(currentTenantId(), id));
    }

    @Override
    public Page<BudgetReturnResponse> list(String status, Pageable pageable) {
        UUID tenantId = currentTenantId();
        BudgetReturnStatus returnStatus = status == null ? null : BudgetReturnStatus.valueOf(status);
        Page<BudgetReturn> returns = returnStatus == null
                ? budgetReturnRepository.findByTenantId(tenantId, pageable)
                : budgetReturnRepository.findByTenantIdAndStatus(tenantId, returnStatus, pageable);
        return returns.map(budgetReturnMapper::toResponse);
    }

    private UUID currentTenantId() {
        return UUID.fromString(securityUtil.getTenantId());
    }

    private BudgetAllocation findAllocation(UUID tenantId, UUID id) {
        return budgetAllocationRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new BudgetNotFoundException("Budget allocation not found: " + id));
    }

    private BudgetReturn findReturn(UUID tenantId, UUID id) {
        return budgetReturnRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new BudgetNotFoundException("Budget return not found: " + id));
    }

    private CoaSegmentReference scopeOf(BudgetAllocation allocation) {
        return new CoaSegmentReference(
                allocation.getAccountId(), allocation.getCostCenterId(), allocation.getDepartmentId(),
                allocation.getFundSourceId(), allocation.getProjectId());
    }

    private UUID allocationFiscalYearId(UUID tenantId, BudgetAllocation allocation) {
        return budgetAllocationRepository.findFiscalYearIdByTenantIdAndAllocationId(tenantId, allocation.getId())
                .orElseThrow(() -> new BudgetNotFoundException("Budget plan not found for allocation: " + allocation.getId()));
    }
}