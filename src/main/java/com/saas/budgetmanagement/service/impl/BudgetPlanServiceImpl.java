package com.saas.budgetmanagement.service.impl;

import com.saas.budgetmanagement.dto.common.CoaSegmentReference;
import com.saas.budgetmanagement.dto.requestDto.BudgetLineItemRequest;
import com.saas.budgetmanagement.dto.requestDto.CreateBudgetPlanRequest;
import com.saas.budgetmanagement.dto.requestDto.UpdateBudgetPlanRequest;
import com.saas.budgetmanagement.dto.requestDto.ApprovalDecisionRequest;
import com.saas.budgetmanagement.dto.responseDto.BudgetPlanResponse;
import com.saas.budgetmanagement.enums.BudgetPlanStatus;
import com.saas.budgetmanagement.exception.BudgetNotFoundException;
import com.saas.budgetmanagement.exception.InvalidBudgetPlanStateException;
import com.saas.budgetmanagement.mapper.BudgetPlanMapper;
import com.saas.budgetmanagement.model.BudgetLine;
import com.saas.budgetmanagement.model.BudgetPlan;
import com.saas.budgetmanagement.model.refcache.RefFiscalYear;
import com.saas.budgetmanagement.repository.BudgetLineRepository;
import com.saas.budgetmanagement.repository.BudgetPlanRepository;
import com.saas.budgetmanagement.repository.refcache.RefFiscalYearRepository;
import com.saas.budgetmanagement.service.BudgetEventPublisherService;
import com.saas.budgetmanagement.service.BudgetApprovalService;
import com.saas.budgetmanagement.service.BudgetPlanService;
import com.saas.budgetmanagement.utility.CoaSegmentDisplayResolver;
import com.saas.budgetmanagement.utility.CoaSegmentValidator;
import com.saas.budgetmanagement.utility.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetPlanServiceImpl implements BudgetPlanService {

    private static final String ROUTING_PLAN_CREATED = "budget.plan.created";
    private static final String ROUTING_PLAN_APPROVED = "budget.plan.approved";
    private static final String ROUTING_PLAN_REJECTED = "budget.plan.rejected";

    private final BudgetPlanRepository budgetPlanRepository;
    private final BudgetLineRepository budgetLineRepository;
    private final RefFiscalYearRepository refFiscalYearRepository;
    private final BudgetEventPublisherService eventPublisherService;
    private final BudgetPlanMapper budgetPlanMapper;
    private final SecurityUtil securityUtil;
    private final BudgetApprovalService budgetApprovalService;
    private final CoaSegmentDisplayResolver displayResolver;
    private final CoaSegmentValidator coaSegmentValidator;

    @Override
    @Transactional
    public BudgetPlanResponse create(CreateBudgetPlanRequest request) {
        UUID tenantId = UUID.fromString(securityUtil.getTenantId());

        RefFiscalYear fiscalYear = refFiscalYearRepository
                .findByTenantIdAndExternalId(tenantId, request.getFiscalYearId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid fiscal year: " + request.getFiscalYearId()));

        if (!fiscalYear.isOpen()) {
            throw new InvalidBudgetPlanStateException("Cannot create a budget plan against a closed fiscal year");
        }

        for (BudgetLineItemRequest lineRequest : request.getLines()) {
            coaSegmentValidator.validate(tenantId, toScope(lineRequest));
        }

        BudgetPlan plan = new BudgetPlan();
        plan.setFiscalYearId(request.getFiscalYearId());
        plan.setRequestedAmount(request.getRequestedAmount());
        plan.setStatus(BudgetPlanStatus.DRAFT);
        plan.setRequestedBy(UUID.fromString(securityUtil.getUserId()));

        BudgetPlan saved = budgetPlanRepository.save(plan);

        List<BudgetLine> lines = request.getLines().stream()
                .map(lineRequest -> budgetPlanMapper.toBudgetLine(lineRequest, saved.getId()))
                .collect(Collectors.toList());
        budgetLineRepository.saveAll(lines);

        eventPublisherService.publish(
                "budget.plan.created",
                ROUTING_PLAN_CREATED,
                budgetPlanMapper.toCreatedEvent(saved)
        );

        return toEnrichedResponse(tenantId, saved, lines);
    }

    @Override
    @Transactional
    public BudgetPlanResponse update(UUID id, UpdateBudgetPlanRequest request) {
        BudgetPlan plan = getOwnedPlanOrThrow(id);

        if (plan.getStatus() != BudgetPlanStatus.DRAFT) {
            throw new InvalidBudgetPlanStateException("Only DRAFT plans can be edited. Current status: " + plan.getStatus());
        }

        UUID tenantId = UUID.fromString(securityUtil.getTenantId());
        for (BudgetLineItemRequest lineRequest : request.getLines()) {
            coaSegmentValidator.validate(tenantId, toScope(lineRequest));
        }

        plan.setRequestedAmount(request.getRequestedAmount());
        BudgetPlan saved = budgetPlanRepository.save(plan);

        budgetLineRepository.deleteByTenantIdAndBudgetPlanId(tenantId, id);
        List<BudgetLine> lines = request.getLines().stream()
                .map(lineRequest -> budgetPlanMapper.toBudgetLine(lineRequest, saved.getId()))
                .collect(Collectors.toList());
        budgetLineRepository.saveAll(lines);

        return toEnrichedResponse(tenantId, saved, lines);
    }

    @Override
    public BudgetPlanResponse getById(UUID id) {
        UUID tenantId = UUID.fromString(securityUtil.getTenantId());
        BudgetPlan plan = getOwnedPlanOrThrow(id);
        List<BudgetLine> lines = budgetLineRepository.findByTenantIdAndBudgetPlanId(tenantId, id);
        return toEnrichedResponse(tenantId, plan, lines);
    }

    @Override
    public Page<BudgetPlanResponse> list(UUID fiscalYearId, String status, Pageable pageable) {
        UUID tenantId = UUID.fromString(securityUtil.getTenantId());

        Page<BudgetPlan> plans;
        if (fiscalYearId != null) {
            plans = budgetPlanRepository.findByTenantIdAndFiscalYearId(tenantId, fiscalYearId, pageable);
        } else if (status != null) {
            plans = budgetPlanRepository.findByTenantIdAndStatus(tenantId, BudgetPlanStatus.valueOf(status), pageable);
        } else {
            plans = budgetPlanRepository.findByTenantId(tenantId, pageable);
        }

        return plans.map(plan -> toEnrichedResponse(tenantId, plan, List.of()));
    }

    @Override
    @Transactional
    public BudgetPlanResponse submit(UUID id) {
        UUID tenantId = UUID.fromString(securityUtil.getTenantId());
        BudgetPlan plan = getOwnedPlanOrThrow(id);

        if (plan.getStatus() != BudgetPlanStatus.DRAFT) {
            throw new InvalidBudgetPlanStateException("Only DRAFT plans can be submitted. Current status: " + plan.getStatus());
        }

        plan.setStatus(BudgetPlanStatus.SUBMITTED);
        BudgetPlan saved = budgetPlanRepository.save(plan);

        return toEnrichedResponse(tenantId, saved, List.of());
    }

    @Override
    @Transactional
    public BudgetPlanResponse approve(UUID id, ApprovalDecisionRequest request) {
        UUID tenantId = UUID.fromString(securityUtil.getTenantId());
        BudgetPlan plan = getOwnedPlanOrThrow(id);

        if (plan.getStatus() != BudgetPlanStatus.SUBMITTED) {
            throw new InvalidBudgetPlanStateException("Only SUBMITTED plans can be approved. Current status: " + plan.getStatus());
        }

        budgetApprovalService.assertCanApprove(tenantId, UUID.fromString(securityUtil.getCurrentUserId()), plan);

        plan.setStatus(BudgetPlanStatus.APPROVED);
        plan.setApprovedBy(UUID.fromString(securityUtil.getUserId()));
        BudgetPlan saved = budgetPlanRepository.save(plan);

        eventPublisherService.publish(
                "budget.plan.approved",
                ROUTING_PLAN_APPROVED,
                budgetPlanMapper.toApprovedEvent(saved)
        );

        return toEnrichedResponse(tenantId, saved, List.of());
    }

    @Override
    @Transactional
    public BudgetPlanResponse reject(UUID id, ApprovalDecisionRequest request) {
        UUID tenantId = UUID.fromString(securityUtil.getTenantId());
        BudgetPlan plan = getOwnedPlanOrThrow(id);

        if (plan.getStatus() != BudgetPlanStatus.SUBMITTED) {
            throw new InvalidBudgetPlanStateException("Only SUBMITTED plans can be rejected. Current status: " + plan.getStatus());
        }

        plan.setStatus(BudgetPlanStatus.REJECTED);
        plan.setRejectionReason(request.getComments());
        BudgetPlan saved = budgetPlanRepository.save(plan);

        eventPublisherService.publish(
                "budget.plan.rejected",
                ROUTING_PLAN_REJECTED,
                budgetPlanMapper.toRejectedEvent(saved)
        );

        return toEnrichedResponse(tenantId, saved, List.of());
    }

    private BudgetPlan getOwnedPlanOrThrow(UUID id) {
        return budgetPlanRepository.findByTenantIdAndId(UUID.fromString(securityUtil.getTenantId()), id)
                .orElseThrow(() -> new BudgetNotFoundException("Budget plan not found: " + id));
    }

    private BudgetPlanResponse toEnrichedResponse(UUID tenantId, BudgetPlan plan, List<BudgetLine> lines) {
        BudgetPlanResponse response = budgetPlanMapper.toResponse(plan, lines);
        enrichPlanResponse(tenantId, response);
        return response;
    }

    private void enrichPlanResponse(UUID tenantId, BudgetPlanResponse response) {
        if (response.getFiscalYearId() != null) {
            Map<UUID, String> fiscalYearNames = displayResolver.resolveFiscalYearNames(tenantId, Set.of(response.getFiscalYearId()));
            response.setFiscalYearName(fiscalYearNames.get(response.getFiscalYearId()));
        }
        displayResolver.enrichCoaSegments(tenantId, response.getLines());
    }

    private CoaSegmentReference toScope(BudgetLineItemRequest lineRequest) {
        return new CoaSegmentReference(
                lineRequest.getAccountId(),
                lineRequest.getCostCenterId(),
                lineRequest.getDepartmentId(),
                lineRequest.getFundSourceId(),
                lineRequest.getProjectId()
        );
    }

    
}