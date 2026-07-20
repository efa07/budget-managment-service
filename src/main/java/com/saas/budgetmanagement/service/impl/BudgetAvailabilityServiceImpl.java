package com.saas.budgetmanagement.service.impl;

import com.saas.budgetmanagement.dto.common.CoaSegmentReference;
import com.saas.budgetmanagement.dto.requestDto.BudgetAvailabilityCheckRequest;
import com.saas.budgetmanagement.dto.responseDto.BudgetAvailabilityCheckResponse;
import com.saas.budgetmanagement.enums.ControlMode;
import com.saas.budgetmanagement.model.BudgetControlViolationLog;
import com.saas.budgetmanagement.model.BudgetLedger;
import com.saas.budgetmanagement.repository.BudgetControlViolationLogRepository;
import com.saas.budgetmanagement.repository.BudgetLedgerRepository;
import com.saas.budgetmanagement.service.BudgetAvailabilityService;
import com.saas.budgetmanagement.service.BudgetEventPublisherService;
import com.saas.budgetmanagement.utility.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BudgetAvailabilityServiceImpl implements BudgetAvailabilityService {

    private static final String ROUTING_CONTROL_VIOLATION = "budget.control.violation";

    private final BudgetLedgerRepository budgetLedgerRepository;
    private final BudgetControlViolationLogRepository violationLogRepository;
    private final BudgetEventPublisherService eventPublisherService;
    private final SecurityUtil securityUtil;

    @Value("${budget-management.control.default-mode:BLOCKED}")
    private ControlMode defaultControlMode;

    @Override
    @Transactional
    public BudgetAvailabilityCheckResponse checkAvailability(BudgetAvailabilityCheckRequest request) {
        UUID tenantId = UUID.fromString(securityUtil.getTenantId());

        BudgetLedger ledger = budgetLedgerRepository
                .findByTenantIdAndFiscalYearIdAndPeriodIdAndAccountIdAndCostCenterIdAndDepartmentIdAndFundSourceIdAndProjectId(
                        tenantId, request.getFiscalYearId(), request.getPeriodId(),
                        request.getAccountId(), request.getCostCenterId(), request.getDepartmentId(),
                        request.getFundSourceId(), request.getProjectId())
                .orElse(null);

        BigDecimal availableAmount = ledger != null ? ledger.getAvailableAmount() : BigDecimal.ZERO;
        boolean sufficient = availableAmount.compareTo(request.getRequestedAmount()) >= 0;

        BudgetAvailabilityCheckResponse response = new BudgetAvailabilityCheckResponse();
        response.setAvailableAmount(availableAmount);
        response.setRequestedAmount(request.getRequestedAmount());

        if (sufficient) {
            response.setAllowed(true);
            return response;
        }

        // Insufficient budget — apply the configured control mode
        BudgetControlViolationLog violation = new BudgetControlViolationLog();
        violation.setTenantId(tenantId);
        violation.setRequestReference(request.getSourceService() + ":" + request.getSourceReferenceId());
        violation.setAccountId(request.getAccountId());
        violation.setCostCenterId(request.getCostCenterId());
        violation.setDepartmentId(request.getDepartmentId());
        violation.setFundSourceId(request.getFundSourceId());
        violation.setProjectId(request.getProjectId());
        violation.setRequestedAmount(request.getRequestedAmount());
        violation.setAvailableAmountAtTime(availableAmount);
        violation.setMode(defaultControlMode);

        BudgetControlViolationLog savedViolation = violationLogRepository.save(violation);

        eventPublisherService.publish(
                "budget.control.violation",
                ROUTING_CONTROL_VIOLATION,
                buildViolationEventPayload(request, availableAmount)
        );

        response.setMode(defaultControlMode);
        response.setViolationId(savedViolation.getId());
        response.setAllowed(defaultControlMode == ControlMode.WARNED);

        return response;
    }

    private Object buildViolationEventPayload(BudgetAvailabilityCheckRequest request, BigDecimal availableAmount) {
        return new java.util.HashMap<>() {{
            put("requestReference", request.getSourceService() + ":" + request.getSourceReferenceId());
            put("accountId", request.getAccountId());
            put("costCenterId", request.getCostCenterId());
            put("requestedAmount", request.getRequestedAmount());
            put("availableAmount", availableAmount);
            put("mode", defaultControlMode);
        }};
    }
}