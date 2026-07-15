package com.saas.budgetmanagement.controller;

import com.saas.budgetmanagement.dto.requestDto.CreateCashRequirementEstimateRequest;
import com.saas.budgetmanagement.service.CashRequirementEstimateService;
import com.saas.budgetmanagement.utility.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/budget-management/cash-requirement-estimates/{tenantId}")
public class CashRequirementEstimateController {

    private final CashRequirementEstimateService cashRequirementEstimateService;
    private final SecurityUtil securityUtil;

    public CashRequirementEstimateController(CashRequirementEstimateService cashRequirementEstimateService, SecurityUtil securityUtil) {
        this.cashRequirementEstimateService = cashRequirementEstimateService;
        this.securityUtil = securityUtil;
    }

    @PostMapping
    public ResponseEntity<Void> createCashRequirementEstimate(@PathVariable UUID tenantId, @RequestBody CreateCashRequirementEstimateRequest request) {
        UUID resolvedTenantId = UUID.fromString(securityUtil.getTenantId());
        if (!resolvedTenantId.equals(tenantId)) {
            throw new AccessDeniedException("Tenant mismatch");
        }
        cashRequirementEstimateService.createEstimate(resolvedTenantId, request);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}