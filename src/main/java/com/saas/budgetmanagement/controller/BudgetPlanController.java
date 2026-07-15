package com.saas.budgetmanagement.controller;

import com.saas.budgetmanagement.dto.requestDto.CreateBudgetPlanRequest;
import com.saas.budgetmanagement.service.BudgetPlanService;
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
@RequestMapping("/api/budget-management/budget-plans/{tenantId}")
public class BudgetPlanController {

    private final BudgetPlanService budgetPlanService;
    private final SecurityUtil securityUtil;

    public BudgetPlanController(BudgetPlanService budgetPlanService, SecurityUtil securityUtil) {
        this.budgetPlanService = budgetPlanService;
        this.securityUtil = securityUtil;
    }

    @PostMapping
    public ResponseEntity<Void> createBudgetPlan(@PathVariable UUID tenantId, @RequestBody CreateBudgetPlanRequest request) {
        UUID resolvedTenantId = UUID.fromString(securityUtil.getTenantId());
        if (!resolvedTenantId.equals(tenantId)) {
            throw new AccessDeniedException("Tenant mismatch");
        }
        budgetPlanService.createBudgetPlan(resolvedTenantId, request);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}