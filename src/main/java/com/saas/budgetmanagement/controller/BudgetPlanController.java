package com.saas.budgetmanagement.controller;

import com.saas.budgetmanagement.dto.requestDto.ApprovalDecisionRequest;
import com.saas.budgetmanagement.dto.requestDto.CreateBudgetPlanRequest;
import com.saas.budgetmanagement.dto.requestDto.UpdateBudgetPlanRequest;
import com.saas.budgetmanagement.dto.responseDto.BudgetPlanResponse;
import com.saas.budgetmanagement.service.BudgetPlanService;
import com.saas.budgetmanagement.utility.PermissionUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/budget-management/budget-plans/{tenantId}")
@RequiredArgsConstructor
@Tag(name = "Budget Plan")
public class BudgetPlanController {

    private final BudgetPlanService budgetPlanService;
    private final PermissionUtil permissionUtil;

    @PostMapping
    public ResponseEntity<BudgetPlanResponse> create(@PathVariable UUID tenantId,
                                                       @Valid @RequestBody CreateBudgetPlanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetPlanService.create(request));
    }

    @GetMapping
    public ResponseEntity<Page<BudgetPlanResponse>> list(@PathVariable UUID tenantId,
                                                           @RequestParam(required = false) UUID fiscalYearId,
                                                           @RequestParam(required = false) String status,
                                                           Pageable pageable) {
        return ResponseEntity.ok(budgetPlanService.list(fiscalYearId, status, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetPlanResponse> getById(@PathVariable UUID tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(budgetPlanService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetPlanResponse> update(@PathVariable UUID tenantId, @PathVariable UUID id,
                                                       @Valid @RequestBody UpdateBudgetPlanRequest request) {
        return ResponseEntity.ok(budgetPlanService.update(id, request));
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<BudgetPlanResponse> submit(@PathVariable UUID tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(budgetPlanService.submit(id));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<BudgetPlanResponse> approve(@PathVariable UUID tenantId, @PathVariable UUID id,
                                                        @Valid @RequestBody ApprovalDecisionRequest request) {
        if (!permissionUtil.hasPermission(tenantId, "budget-plan-approval")) {
            throw new AccessDeniedException("You do not have permission to approve budget plans");
        }
        return ResponseEntity.ok(budgetPlanService.approve(id, request));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<BudgetPlanResponse> reject(@PathVariable UUID tenantId, @PathVariable UUID id,
                                                       @Valid @RequestBody ApprovalDecisionRequest request) {
        if (!permissionUtil.hasPermission(tenantId, "budget-plan-approval")) {
            throw new AccessDeniedException("You do not have permission to approve budget plans");
        }
        return ResponseEntity.ok(budgetPlanService.reject(id, request));
    }
}