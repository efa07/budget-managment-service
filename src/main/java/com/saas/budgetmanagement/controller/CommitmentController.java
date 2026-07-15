package com.saas.budgetmanagement.controller;

import com.saas.budgetmanagement.service.CommitmentService;
import com.saas.budgetmanagement.utility.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/budget-management/commitments/{tenantId}")
public class CommitmentController {

    private final CommitmentService commitmentService;
    private final SecurityUtil securityUtil;

    public CommitmentController(CommitmentService commitmentService, SecurityUtil securityUtil) {
        this.commitmentService = commitmentService;
        this.securityUtil = securityUtil;
    }

    @PostMapping
    public ResponseEntity<Void> registerCommitment(@PathVariable UUID tenantId) {
        UUID resolvedTenantId = UUID.fromString(securityUtil.getTenantId());
        if (!resolvedTenantId.equals(tenantId)) {
            throw new AccessDeniedException("Tenant mismatch");
        }
        commitmentService.registerCommitment(resolvedTenantId);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}