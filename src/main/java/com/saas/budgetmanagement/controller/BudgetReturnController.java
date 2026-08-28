package com.saas.budgetmanagement.controller;

import com.saas.budgetmanagement.dto.requestDto.ApprovalDecisionRequest;
import com.saas.budgetmanagement.dto.requestDto.CreateBudgetReturnRequest;
import com.saas.budgetmanagement.dto.responseDto.BudgetReturnResponse;
import com.saas.budgetmanagement.service.BudgetReturnService;
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
@RequestMapping("/api/budget-management/budget-returns/{tenantId}")
@RequiredArgsConstructor
@Tag(name = "Budget Return")
public class BudgetReturnController {

    private final BudgetReturnService budgetReturnService;
    private final PermissionUtil permissionUtil;

    @PostMapping
    public ResponseEntity<BudgetReturnResponse> requestReturn(@PathVariable UUID tenantId,
                                                                @Valid @RequestBody CreateBudgetReturnRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetReturnService.requestReturn(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetReturnResponse> getById(@PathVariable UUID tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(budgetReturnService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<BudgetReturnResponse>> list(@PathVariable UUID tenantId,
                                                              @RequestParam(required = false) String status,
                                                              Pageable pageable) {
        return ResponseEntity.ok(budgetReturnService.list(status, pageable));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<BudgetReturnResponse> approve(@PathVariable UUID tenantId, @PathVariable UUID id,
                                                         @Valid @RequestBody ApprovalDecisionRequest request) {
        requirePermission(tenantId);
        return ResponseEntity.ok(budgetReturnService.approve(id, request));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<BudgetReturnResponse> reject(@PathVariable UUID tenantId, @PathVariable UUID id,
                                                        @Valid @RequestBody ApprovalDecisionRequest request) {
        requirePermission(tenantId);
        return ResponseEntity.ok(budgetReturnService.reject(id, request));
    }

    private void requirePermission(UUID tenantId) {
        if (!permissionUtil.hasPermission(tenantId, "budget-return-approval")) {
            throw new AccessDeniedException("You do not have permission to approve budget returns");
        }
    }
}