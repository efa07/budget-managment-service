package com.saas.budgetmanagement.controller;

import com.saas.budgetmanagement.dto.requestDto.CreateRevisionRequest;
import com.saas.budgetmanagement.service.BudgetRevisionService;
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
@RequestMapping("/api/budget-management/budget-revisions/{tenantId}")
public class BudgetRevisionController {

    private final BudgetRevisionService budgetRevisionService;
    private final SecurityUtil securityUtil;

    public BudgetRevisionController(BudgetRevisionService budgetRevisionService, SecurityUtil securityUtil) {
        this.budgetRevisionService = budgetRevisionService;
        this.securityUtil = securityUtil;
    }

    @PostMapping
    public ResponseEntity<Void> createBudgetRevision(@PathVariable UUID tenantId, @RequestBody CreateRevisionRequest request) {
        UUID resolvedTenantId = UUID.fromString(securityUtil.getTenantId());
        if (!resolvedTenantId.equals(tenantId)) {
            throw new AccessDeniedException("Tenant mismatch");
        }
        budgetRevisionService.createRevision(resolvedTenantId, request);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}