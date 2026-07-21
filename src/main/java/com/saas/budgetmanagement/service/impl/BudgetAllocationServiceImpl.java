package com.saas.budgetmanagement.service.impl;

import com.saas.budgetmanagement.dto.common.CoaSegmentReference;
import com.saas.budgetmanagement.dto.requestDto.CreateAllocationRequest;
import com.saas.budgetmanagement.dto.responseDto.BudgetAllocationResponse;
import com.saas.budgetmanagement.enums.BudgetPlanStatus;
import com.saas.budgetmanagement.exception.BudgetNotFoundException;
import com.saas.budgetmanagement.exception.InvalidBudgetPlanStateException;
import com.saas.budgetmanagement.mapper.BudgetAllocationMapper;
import com.saas.budgetmanagement.model.BudgetAllocation;
import com.saas.budgetmanagement.model.BudgetPlan;
import com.saas.budgetmanagement.repository.BudgetAllocationRepository;
import com.saas.budgetmanagement.repository.BudgetPlanRepository;
import com.saas.budgetmanagement.service.BudgetAllocationService;
import com.saas.budgetmanagement.service.BudgetEventPublisherService;
import com.saas.budgetmanagement.service.BudgetLedgerService;
import com.saas.budgetmanagement.utility.CoaSegmentDisplayResolver;
import com.saas.budgetmanagement.utility.CoaSegmentValidator;
import com.saas.budgetmanagement.utility.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetAllocationServiceImpl implements BudgetAllocationService {

    private static final String ROUTING_ALLOCATION_CREATED = "budget.allocation.created";

    private final BudgetAllocationRepository budgetAllocationRepository;
    private final BudgetPlanRepository budgetPlanRepository;
    private final CoaSegmentValidator coaSegmentValidator;
    private final CoaSegmentDisplayResolver displayResolver;
    private final BudgetLedgerService budgetLedgerService;
    private final BudgetEventPublisherService eventPublisherService;
    private final BudgetAllocationMapper budgetAllocationMapper;
    private final SecurityUtil securityUtil;

    @Override
    @Transactional
    public BudgetAllocationResponse create(CreateAllocationRequest request) {
        UUID tenantId = UUID.fromString(securityUtil.getTenantId());

        BudgetPlan plan = budgetPlanRepository.findByTenantIdAndId(tenantId, request.getBudgetPlanId())
                .orElseThrow(() -> new BudgetNotFoundException("Budget plan not found: " + request.getBudgetPlanId()));

        if (plan.getStatus() != BudgetPlanStatus.APPROVED) {
            throw new InvalidBudgetPlanStateException(
                    "Allocations can only be created against an APPROVED plan. Current status: " + plan.getStatus());
        }

        CoaSegmentReference scope = new CoaSegmentReference(
                request.getAccountId(), request.getCostCenterId(), request.getDepartmentId(),
                request.getFundSourceId(), request.getProjectId());
        coaSegmentValidator.validate(tenantId, scope);

        BudgetAllocation allocation = new BudgetAllocation();
        allocation.setTenantId(tenantId);
        allocation.setBudgetPlanId(request.getBudgetPlanId());
        allocation.setBudgetLineId(request.getBudgetLineId());
        allocation.setPeriodId(request.getPeriodId());
        allocation.setAccountId(request.getAccountId());
        allocation.setCostCenterId(request.getCostCenterId());
        allocation.setDepartmentId(request.getDepartmentId());
        allocation.setFundSourceId(request.getFundSourceId());
        allocation.setProjectId(request.getProjectId());
        allocation.setAllocatedAmount(request.getAmount());
        allocation.setAllocatedAt(LocalDateTime.now());
        allocation.setAllocatedBy(securityUtil.getCurrentUserId());

        BudgetAllocation saved = budgetAllocationRepository.save(allocation);

        budgetLedgerService.applyAllocationDelta(
                tenantId, plan.getFiscalYearId(), request.getPeriodId(), scope, request.getAmount());

        eventPublisherService.publish(
                "budget.allocation.created", ROUTING_ALLOCATION_CREATED,
                budgetAllocationMapper.toCreatedEvent(saved));

        BudgetAllocationResponse response = budgetAllocationMapper.toResponse(saved);
        enrichAllocationResponses(tenantId, List.of(response));
        return response;
    }

    @Override
    public BudgetAllocationResponse getById(UUID id) {
        UUID tenantId = UUID.fromString(securityUtil.getTenantId());
        BudgetAllocation allocation = budgetAllocationRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new BudgetNotFoundException("Budget allocation not found: " + id));

        BudgetAllocationResponse response = budgetAllocationMapper.toResponse(allocation);
        enrichAllocationResponses(tenantId, List.of(response));
        return response;
    }

    @Override
    public Page<BudgetAllocationResponse> list(UUID budgetPlanId, Pageable pageable) {
        UUID tenantId = UUID.fromString(securityUtil.getTenantId());
        Page<BudgetAllocationResponse> page = budgetAllocationRepository
                .findByTenantIdAndBudgetPlanId(tenantId, budgetPlanId, pageable)
                .map(budgetAllocationMapper::toResponse);

        enrichAllocationResponses(tenantId, page.getContent());
        return page;
    }

    private void enrichAllocationResponses(UUID tenantId, List<BudgetAllocationResponse> responses) {
        if (responses.isEmpty()) {
            return;
        }

        Set<UUID> accountIds = collectIds(responses, BudgetAllocationResponse::getAccountId);
        Set<UUID> costCenterIds = collectIds(responses, BudgetAllocationResponse::getCostCenterId);
        Set<UUID> departmentIds = collectIds(responses, BudgetAllocationResponse::getDepartmentId);
        Set<UUID> fundSourceIds = collectIds(responses, BudgetAllocationResponse::getFundSourceId);
        Set<UUID> projectIds = collectIds(responses, BudgetAllocationResponse::getProjectId);

        Map<UUID, String> accountNames = displayResolver.resolveAccountNames(tenantId, accountIds);
        Map<UUID, String> costCenterNames = displayResolver.resolveCostCenterNames(tenantId, costCenterIds);
        Map<UUID, String> departmentNames = displayResolver.resolveDepartmentNames(tenantId, departmentIds);
        Map<UUID, String> fundSourceNames = displayResolver.resolveFundSourceNames(tenantId, fundSourceIds);
        Map<UUID, String> projectNames = displayResolver.resolveProjectNames(tenantId, projectIds);

        for (BudgetAllocationResponse r : responses) {
            r.setAccountName(accountNames.get(r.getAccountId()));
            r.setCostCenterName(costCenterNames.get(r.getCostCenterId()));
            r.setDepartmentName(departmentNames.get(r.getDepartmentId()));
            r.setFundSourceName(fundSourceNames.get(r.getFundSourceId()));
            r.setProjectName(projectNames.get(r.getProjectId()));
        }
    }

    private Set<UUID> collectIds(List<BudgetAllocationResponse> responses, Function<BudgetAllocationResponse, UUID> extractor) {
        return responses.stream().map(extractor).filter(Objects::nonNull).collect(Collectors.toSet());
    }
}