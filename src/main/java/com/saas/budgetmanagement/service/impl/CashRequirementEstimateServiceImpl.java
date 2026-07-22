package com.saas.budgetmanagement.service.impl;

import com.saas.budgetmanagement.dto.requestDto.CreateCashRequirementEstimateRequest;
import com.saas.budgetmanagement.dto.responseDto.CashRequirementEstimateResponse;
import com.saas.budgetmanagement.enums.BudgetPlanStatus;
import com.saas.budgetmanagement.enums.CashRequirementEstimateStatus;
import com.saas.budgetmanagement.exception.BudgetNotFoundException;
import com.saas.budgetmanagement.exception.InvalidBudgetPlanStateException;
import com.saas.budgetmanagement.mapper.CashRequirementEstimateMapper;
import com.saas.budgetmanagement.model.BudgetPlan;
import com.saas.budgetmanagement.model.CashRequirementEstimate;
import com.saas.budgetmanagement.repository.BudgetPlanRepository;
import com.saas.budgetmanagement.repository.CashRequirementEstimateRepository;
import com.saas.budgetmanagement.service.CashRequirementEstimateService;
import com.saas.budgetmanagement.utility.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CashRequirementEstimateServiceImpl implements CashRequirementEstimateService {

    private final CashRequirementEstimateRepository cashRequirementEstimateRepository;
    private final BudgetPlanRepository budgetPlanRepository;
    private final CashRequirementEstimateMapper cashRequirementEstimateMapper;
    private final SecurityUtil securityUtil;
    // TODO: inject CashBankingClient once built, for the advisory position check in create()

    @Override
    @Transactional
    public CashRequirementEstimateResponse create(CreateCashRequirementEstimateRequest request) {
        UUID tenantId = UUID.fromString(securityUtil.getTenantId());

        BudgetPlan plan = budgetPlanRepository.findByTenantIdAndId(tenantId, request.getBudgetPlanId())
                .orElseThrow(() -> new BudgetNotFoundException("Budget plan not found: " + request.getBudgetPlanId()));

        if (plan.getStatus() != BudgetPlanStatus.APPROVED) {
            throw new InvalidBudgetPlanStateException(
                    "Cash requirement estimates can only be created against an APPROVED plan. Current status: " + plan.getStatus());
        }

        CashRequirementEstimate estimate = new CashRequirementEstimate();
        estimate.setTenantId(tenantId);
        estimate.setBudgetPlanId(request.getBudgetPlanId());
        estimate.setPeriodId(request.getPeriodId());
        estimate.setEstimatedAmount(request.getEstimatedAmount());
        estimate.setStatus(CashRequirementEstimateStatus.DRAFT);

        CashRequirementEstimate saved = cashRequirementEstimateRepository.save(estimate);

        CashRequirementEstimateResponse response = cashRequirementEstimateMapper.toResponse(saved);
        response.setAdvisoryNote(fetchAdvisoryNote(tenantId, request.getPeriodId(), request.getEstimatedAmount()));
        return response;
    }

    @Override
    @Transactional
    public CashRequirementEstimateResponse approve(UUID id) {
        UUID tenantId = UUID.fromString(securityUtil.getTenantId());

        CashRequirementEstimate estimate = cashRequirementEstimateRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new BudgetNotFoundException("Cash requirement estimate not found: " + id));

        if (estimate.getStatus() != CashRequirementEstimateStatus.DRAFT) {
            throw new InvalidBudgetPlanStateException(
                    "Only DRAFT estimates can be approved. Current status: " + estimate.getStatus());
        }

        estimate.setStatus(CashRequirementEstimateStatus.APPROVED);
        estimate.setApprovedBy(UUID.fromString(securityUtil.getCurrentUserId()));
        CashRequirementEstimate saved = cashRequirementEstimateRepository.save(estimate);

        return cashRequirementEstimateMapper.toResponse(saved);
    }

    @Override
    public List<CashRequirementEstimateResponse> listByPlan(UUID budgetPlanId) {
        UUID tenantId = UUID.fromString(securityUtil.getTenantId());
        return cashRequirementEstimateRepository.findByTenantIdAndBudgetPlanId(tenantId, budgetPlanId).stream()
                .map(cashRequirementEstimateMapper::toResponse)
                .collect(Collectors.toList());
    }

    private String fetchAdvisoryNote(UUID tenantId, UUID periodId, java.math.BigDecimal estimatedAmount) {
        // Advisory only, per the architecture guide — Cash & Banking's position/forecast should
        // never block estimate creation, only inform it. Once CashBankingClient exists, call it
        // here, compare estimatedAmount against the forecasted position, and return a human-readable
        // note if the estimate looks high relative to forecast. Any failure calling Cash & Banking
        // (timeout, service down) must be swallowed here, not thrown — this is advisory, never a gate.
        log.debug("Cash & Banking advisory check not yet wired — CashBankingClient not built");
        return null;
    }
}