package com.saas.budgetmanagement.controller;

import com.saas.budgetmanagement.service.BudgetLedgerService;
import com.saas.budgetmanagement.utility.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/budget-management/budget-ledger/{tenantId}")
public class BudgetLedgerController {

    private final BudgetLedgerService budgetLedgerService;
    private final SecurityUtil securityUtil;

    public BudgetLedgerController(BudgetLedgerService budgetLedgerService, SecurityUtil securityUtil) {
        this.budgetLedgerService = budgetLedgerService;
        this.securityUtil = securityUtil;
    }

    @GetMapping
    public ResponseEntity<Void> getBudgetLedger(@PathVariable UUID tenantId) {
        UUID resolvedTenantId = UUID.fromString(securityUtil.getTenantId());
        if (!resolvedTenantId.equals(tenantId)) {
            throw new AccessDeniedException("Tenant mismatch");
        }
        budgetLedgerService.recordLedgerEntry(resolvedTenantId);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}