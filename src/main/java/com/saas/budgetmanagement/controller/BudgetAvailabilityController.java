package com.saas.budgetmanagement.controller;

import com.saas.budgetmanagement.dto.requestDto.BudgetAvailabilityCheckRequest;
import com.saas.budgetmanagement.service.BudgetAvailabilityService;
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
@RequestMapping("/api/budget-management/budget-availability/{tenantId}")
public class BudgetAvailabilityController {

    private final BudgetAvailabilityService budgetAvailabilityService;
    private final SecurityUtil securityUtil;

    public BudgetAvailabilityController(BudgetAvailabilityService budgetAvailabilityService, SecurityUtil securityUtil) {
        this.budgetAvailabilityService = budgetAvailabilityService;
        this.securityUtil = securityUtil;
    }

    @PostMapping
    public ResponseEntity<Void> checkBudgetAvailability(@PathVariable UUID tenantId, @RequestBody BudgetAvailabilityCheckRequest request) {
        UUID resolvedTenantId = UUID.fromString(securityUtil.getTenantId());
        if (!resolvedTenantId.equals(tenantId)) {
            throw new AccessDeniedException("Tenant mismatch");
        }
        budgetAvailabilityService.checkAvailability(request);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}