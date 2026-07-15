package com.saas.budgetmanagement.controller;

import com.saas.budgetmanagement.dto.requestDto.CreateTransferRequest;
import com.saas.budgetmanagement.service.BudgetTransferService;
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
@RequestMapping("/api/budget-management/budget-transfers/{tenantId}")
public class BudgetTransferController {

    private final BudgetTransferService budgetTransferService;
    private final SecurityUtil securityUtil;

    public BudgetTransferController(BudgetTransferService budgetTransferService, SecurityUtil securityUtil) {
        this.budgetTransferService = budgetTransferService;
        this.securityUtil = securityUtil;
    }

    @PostMapping
    public ResponseEntity<Void> createBudgetTransfer(@PathVariable UUID tenantId, @RequestBody CreateTransferRequest request) {
        UUID resolvedTenantId = UUID.fromString(securityUtil.getTenantId());
        if (!resolvedTenantId.equals(tenantId)) {
            throw new AccessDeniedException("Tenant mismatch");
        }
        budgetTransferService.createTransfer(resolvedTenantId, request);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}