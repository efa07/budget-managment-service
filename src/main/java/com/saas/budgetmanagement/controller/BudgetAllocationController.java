package com.saas.budgetmanagement.controller;

import com.saas.budgetmanagement.dto.requestDto.CreateAllocationRequest;
import com.saas.budgetmanagement.service.BudgetAllocationService;
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
@RequestMapping("/api/budget-management/budget-allocations/{tenantId}")
public class BudgetAllocationController {

    private final BudgetAllocationService budgetAllocationService;
    private final SecurityUtil securityUtil;

    public BudgetAllocationController(BudgetAllocationService budgetAllocationService, SecurityUtil securityUtil) {
        this.budgetAllocationService = budgetAllocationService;
        this.securityUtil = securityUtil;
    }

    @PostMapping
    public ResponseEntity<Void> createBudgetAllocation(@PathVariable UUID tenantId, @RequestBody CreateAllocationRequest request) {
        UUID resolvedTenantId = UUID.fromString(securityUtil.getTenantId());
        if (!resolvedTenantId.equals(tenantId)) {
            throw new AccessDeniedException("Tenant mismatch");
        }
        budgetAllocationService.create(request);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}