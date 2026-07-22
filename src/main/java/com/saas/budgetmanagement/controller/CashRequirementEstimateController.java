package com.saas.budgetmanagement.controller;

import com.saas.budgetmanagement.dto.requestDto.CreateCashRequirementEstimateRequest;
import com.saas.budgetmanagement.dto.responseDto.CashRequirementEstimateResponse;
import com.saas.budgetmanagement.service.CashRequirementEstimateService;
import com.saas.budgetmanagement.utility.PermissionUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/budget-management/cash-requirement-estimates/{tenantId}")
@RequiredArgsConstructor
@Tag(name = "Cash Requirement Estimate")
public class CashRequirementEstimateController {

    private final CashRequirementEstimateService cashRequirementEstimateService;
    private final PermissionUtil permissionUtil;

    @PostMapping
    public ResponseEntity<CashRequirementEstimateResponse> create(@PathVariable UUID tenantId,
                                                                    @Valid @RequestBody CreateCashRequirementEstimateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cashRequirementEstimateService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<CashRequirementEstimateResponse>> listByPlan(@PathVariable UUID tenantId,
                                                                              @RequestParam UUID budgetPlanId) {
        return ResponseEntity.ok(cashRequirementEstimateService.listByPlan(budgetPlanId));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<CashRequirementEstimateResponse> approve(@PathVariable UUID tenantId, @PathVariable UUID id) {
        if (!permissionUtil.hasPermission(tenantId, "cash-requirement-estimate-approval")) {
            throw new AccessDeniedException("You do not have permission to approve cash requirement estimates");
        }
        return ResponseEntity.ok(cashRequirementEstimateService.approve(id));
    }
}