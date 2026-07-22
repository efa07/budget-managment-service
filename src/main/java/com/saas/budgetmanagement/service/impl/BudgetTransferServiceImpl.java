package com.saas.budgetmanagement.service.impl;

import com.saas.budgetmanagement.dto.common.CoaSegmentReference;
import com.saas.budgetmanagement.dto.requestDto.ApprovalDecisionRequest;
import com.saas.budgetmanagement.dto.requestDto.CreateTransferRequest;
import com.saas.budgetmanagement.dto.responseDto.BudgetTransferResponse;
import com.saas.budgetmanagement.enums.TransferStatus;
import com.saas.budgetmanagement.exception.BudgetNotFoundException;
import com.saas.budgetmanagement.exception.InvalidBudgetTransferStateException;
import com.saas.budgetmanagement.mapper.BudgetTransferMapper;
import com.saas.budgetmanagement.model.BudgetAllocation;
import com.saas.budgetmanagement.model.BudgetPlan;
import com.saas.budgetmanagement.model.BudgetTransfer;
import com.saas.budgetmanagement.repository.BudgetAllocationRepository;
import com.saas.budgetmanagement.repository.BudgetPlanRepository;
import com.saas.budgetmanagement.repository.BudgetTransferRepository;
import com.saas.budgetmanagement.service.BudgetEventPublisherService;
import com.saas.budgetmanagement.service.BudgetLedgerService;
import com.saas.budgetmanagement.service.BudgetTransferService;
import com.saas.budgetmanagement.utility.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BudgetTransferServiceImpl implements BudgetTransferService {

    private static final String ROUTING_TRANSFER_REQUESTED = "budget.transfer.requested";
    private static final String ROUTING_TRANSFER_APPROVED = "budget.transfer.approved";

    private final BudgetTransferRepository budgetTransferRepository;
    private final BudgetAllocationRepository budgetAllocationRepository;
    private final BudgetPlanRepository budgetPlanRepository;
    private final BudgetLedgerService budgetLedgerService;
    private final BudgetEventPublisherService eventPublisherService;
    private final BudgetTransferMapper budgetTransferMapper;
    private final SecurityUtil securityUtil;

    @Override
    @Transactional
    public BudgetTransferResponse requestTransfer(CreateTransferRequest request) {
        UUID tenantId = UUID.fromString(securityUtil.getTenantId());

        BudgetAllocation fromAllocation = budgetAllocationRepository
                .findByTenantIdAndId(tenantId, request.getFromAllocationId())
                .orElseThrow(() -> new BudgetNotFoundException("Source allocation not found: " + request.getFromAllocationId()));

        BudgetAllocation toAllocation = budgetAllocationRepository
                .findByTenantIdAndId(tenantId, request.getToAllocationId())
                .orElseThrow(() -> new BudgetNotFoundException("Target allocation not found: " + request.getToAllocationId()));

        if (request.getAmount().compareTo(fromAllocation.getAllocatedAmount()) > 0) {
            throw new InvalidBudgetTransferStateException(
                    "Transfer amount exceeds source allocation's allocated amount");
        }

        BudgetTransfer transfer = new BudgetTransfer();
        transfer.setTenantId(tenantId);
        transfer.setFromAllocationId(request.getFromAllocationId());
        transfer.setToAllocationId(request.getToAllocationId());
        transfer.setAmount(request.getAmount());
        transfer.setStatus(TransferStatus.REQUESTED);
        transfer.setJustification(request.getJustification());
        transfer.setRequestedBy(UUID.fromString(securityUtil.getUserId()));

        BudgetTransfer saved = budgetTransferRepository.save(transfer);

        eventPublisherService.publish(
                "budget.transfer.requested", ROUTING_TRANSFER_REQUESTED,
                budgetTransferMapper.toRequestedEvent(saved));

        return budgetTransferMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public BudgetTransferResponse approve(UUID id, ApprovalDecisionRequest request) {
        UUID tenantId = UUID.fromString(securityUtil.getTenantId());

        BudgetTransfer transfer = budgetTransferRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new BudgetNotFoundException("Budget transfer not found: " + id));

        if (transfer.getStatus() != TransferStatus.REQUESTED) {
            throw new InvalidBudgetTransferStateException(
                    "Only REQUESTED transfers can be approved. Current status: " + transfer.getStatus());
        }

        BudgetAllocation fromAllocation = budgetAllocationRepository
                .findByTenantIdAndId(tenantId, transfer.getFromAllocationId())
                .orElseThrow(() -> new BudgetNotFoundException("Source allocation not found: " + transfer.getFromAllocationId()));

        BudgetAllocation toAllocation = budgetAllocationRepository
                .findByTenantIdAndId(tenantId, transfer.getToAllocationId())
                .orElseThrow(() -> new BudgetNotFoundException("Target allocation not found: " + transfer.getToAllocationId()));

        // Re-validate at approval time — the source allocation's balance may have changed since the request was made
        if (transfer.getAmount().compareTo(fromAllocation.getAllocatedAmount()) > 0) {
            throw new InvalidBudgetTransferStateException(
                    "Transfer amount now exceeds source allocation's current allocated amount");
        }

        BudgetPlan fromPlan = budgetPlanRepository.findByTenantIdAndId(tenantId, fromAllocation.getBudgetPlanId())
                .orElseThrow(() -> new BudgetNotFoundException("Budget plan not found: " + fromAllocation.getBudgetPlanId()));
        BudgetPlan toPlan = budgetPlanRepository.findByTenantIdAndId(tenantId, toAllocation.getBudgetPlanId())
                .orElseThrow(() -> new BudgetNotFoundException("Budget plan not found: " + toAllocation.getBudgetPlanId()));

        fromAllocation.setAllocatedAmount(fromAllocation.getAllocatedAmount().subtract(transfer.getAmount()));
        toAllocation.setAllocatedAmount(toAllocation.getAllocatedAmount().add(transfer.getAmount()));
        budgetAllocationRepository.save(fromAllocation);
        budgetAllocationRepository.save(toAllocation);

        CoaSegmentReference fromScope = scopeOf(fromAllocation);
        CoaSegmentReference toScope = scopeOf(toAllocation);

        budgetLedgerService.applyAllocationDelta(
                tenantId, fromPlan.getFiscalYearId(), fromAllocation.getPeriodId(), fromScope, transfer.getAmount().negate());
        budgetLedgerService.applyAllocationDelta(
                tenantId, toPlan.getFiscalYearId(), toAllocation.getPeriodId(), toScope, transfer.getAmount());

        transfer.setStatus(TransferStatus.APPROVED);
        transfer.setApprovedBy(UUID.fromString(securityUtil.getUserId()));
        BudgetTransfer saved = budgetTransferRepository.save(transfer);

        eventPublisherService.publish(
                "budget.transfer.approved", ROUTING_TRANSFER_APPROVED,
                budgetTransferMapper.toApprovedEvent(saved));

        return budgetTransferMapper.toResponse(saved);
    }

@Override
public BudgetTransferResponse getById(UUID id) {
    UUID tenantId = UUID.fromString(securityUtil.getTenantId());
    BudgetTransfer transfer = budgetTransferRepository.findByTenantIdAndId(tenantId, id)
            .orElseThrow(() -> new BudgetNotFoundException("Budget transfer not found: " + id));
    return budgetTransferMapper.toResponse(transfer);
}

    @Override
    @Transactional
    public BudgetTransferResponse reject(UUID id, ApprovalDecisionRequest request) {
        UUID tenantId = UUID.fromString(securityUtil.getTenantId());

        BudgetTransfer transfer = budgetTransferRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new BudgetNotFoundException("Budget transfer not found: " + id));

        if (transfer.getStatus() != TransferStatus.REQUESTED) {
            throw new InvalidBudgetTransferStateException(
                    "Only REQUESTED transfers can be rejected. Current status: " + transfer.getStatus());
        }

        transfer.setStatus(TransferStatus.REJECTED);
        BudgetTransfer saved = budgetTransferRepository.save(transfer);

        return budgetTransferMapper.toResponse(saved);
    }

  @Override
public Page<BudgetTransferResponse> list(String status, Pageable pageable) {
    UUID tenantId = UUID.fromString(securityUtil.getTenantId());
    TransferStatus transferStatus = status != null ? TransferStatus.valueOf(status) : null;

    Page<BudgetTransfer> transfers = transferStatus != null
            ? budgetTransferRepository.findByTenantIdAndStatus(tenantId, transferStatus, pageable)
            : budgetTransferRepository.findByTenantId(tenantId, pageable);

    return transfers.map(budgetTransferMapper::toResponse);
}

    private CoaSegmentReference scopeOf(BudgetAllocation allocation) {
        return new CoaSegmentReference(
                allocation.getAccountId(), allocation.getCostCenterId(), allocation.getDepartmentId(),
                allocation.getFundSourceId(), allocation.getProjectId());
    }
}