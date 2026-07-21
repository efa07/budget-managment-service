package com.saas.budgetmanagement.service.impl;

import com.saas.budgetmanagement.dto.requestDto.CreateRevisionRequest;
import com.saas.budgetmanagement.dto.responseDto.BudgetRevisionResponse;
import com.saas.budgetmanagement.enums.BudgetPlanStatus;
import com.saas.budgetmanagement.exception.BudgetNotFoundException;
import com.saas.budgetmanagement.exception.InvalidBudgetPlanStateException;
import com.saas.budgetmanagement.exception.InvalidBudgetRevisionException;
import com.saas.budgetmanagement.mapper.BudgetRevisionMapper;
import com.saas.budgetmanagement.model.BudgetPlan;
import com.saas.budgetmanagement.model.BudgetRevision;
import com.saas.budgetmanagement.repository.BudgetAllocationRepository;
import com.saas.budgetmanagement.repository.BudgetPlanRepository;
import com.saas.budgetmanagement.repository.BudgetRevisionRepository;
import com.saas.budgetmanagement.service.BudgetEventPublisherService;
import com.saas.budgetmanagement.service.BudgetRevisionService;
import com.saas.budgetmanagement.utility.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetRevisionServiceImpl implements BudgetRevisionService {

    private static final String ROUTING_REVISION_CREATED = "budget.revision.created";

    private final BudgetRevisionRepository budgetRevisionRepository;
    private final BudgetAllocationRepository budgetAllocationRepository;
    private final BudgetPlanRepository budgetPlanRepository;
    private final BudgetEventPublisherService eventPublisherService;
    private final BudgetRevisionMapper budgetRevisionMapper;
    private final SecurityUtil securityUtil;

    @Override
@Transactional
public BudgetRevisionResponse create(CreateRevisionRequest request) {
    UUID tenantId = UUID.fromString(securityUtil.getTenantId());

    BudgetPlan plan = budgetPlanRepository.findByTenantIdAndId(tenantId, request.getBudgetPlanId())
            .orElseThrow(() -> new BudgetNotFoundException("Budget plan not found: " + request.getBudgetPlanId()));

    if (plan.getStatus() != BudgetPlanStatus.APPROVED) {
        throw new InvalidBudgetPlanStateException(
                "Only APPROVED plans can be revised. Current status: " + plan.getStatus());
    }

    BigDecimal totalAllocated = budgetAllocationRepository.sumAllocatedAmountByPlan(tenantId, plan.getId());

    if (request.getRevisedAmount().compareTo(totalAllocated) < 0) {
        throw new InvalidBudgetRevisionException(String.format(
                "Cannot revise plan to %s — total already allocated (%s) exceeds the proposed amount. " +
                "Reduce or transfer existing allocations first.",
                request.getRevisedAmount(), totalAllocated));
    }

    BudgetRevision revision = new BudgetRevision();
    revision.setTenantId(tenantId);
    revision.setBudgetPlanId(plan.getId());
    revision.setOriginalAmount(plan.getRequestedAmount());
    revision.setRevisedAmount(request.getRevisedAmount());
    revision.setReason(request.getReason());

    BudgetRevision saved = budgetRevisionRepository.save(revision);

    plan.setRequestedAmount(request.getRevisedAmount());
    budgetPlanRepository.save(plan);

    eventPublisherService.publish(
            "budget.revision.created", ROUTING_REVISION_CREATED,
            budgetRevisionMapper.toCreatedEvent(saved));

    return budgetRevisionMapper.toResponse(saved);
}

    @Override
    public List<BudgetRevisionResponse> listByPlan(UUID budgetPlanId) {
        UUID tenantId = UUID.fromString(securityUtil.getTenantId());
        return budgetRevisionRepository.findByTenantIdAndBudgetPlanId(tenantId, budgetPlanId).stream()
                .map(budgetRevisionMapper::toResponse)
                .collect(Collectors.toList());
    }
}