package com.saas.budgetmanagement.controller;

import com.saas.budgetmanagement.dto.common.CoaSegmentReference;
import com.saas.budgetmanagement.dto.responseDto.BudgetLedgerResponse;
import com.saas.budgetmanagement.service.BudgetLedgerService;
import com.saas.budgetmanagement.utility.SecurityUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/budget-management/budget-ledger/{tenantId}")
@RequiredArgsConstructor
@Tag(name = "Budget Ledger")
public class BudgetLedgerController {

    private final BudgetLedgerService budgetLedgerService;
    private final SecurityUtil securityUtil;

    @GetMapping
    public ResponseEntity<BudgetLedgerResponse> getLedger(@PathVariable UUID tenantId,
                                                            @RequestParam UUID fiscalYearId,
                                                            @RequestParam UUID periodId,
                                                            @RequestParam UUID accountId,
                                                            @RequestParam(required = false) UUID costCenterId,
                                                            @RequestParam(required = false) UUID departmentId,
                                                            @RequestParam(required = false) UUID fundSourceId,
                                                            @RequestParam(required = false) UUID projectId) {

        UUID resolvedTenantId = UUID.fromString(securityUtil.getTenantId());

        CoaSegmentReference scope = new CoaSegmentReference(
                accountId, costCenterId, departmentId, fundSourceId, projectId);

        return ResponseEntity.ok(budgetLedgerService.getLedger(resolvedTenantId, fiscalYearId, periodId, scope));
    }
}