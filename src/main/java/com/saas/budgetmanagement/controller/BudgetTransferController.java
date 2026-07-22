package com.saas.budgetmanagement.controller;

import com.saas.budgetmanagement.dto.requestDto.ApprovalDecisionRequest;
import com.saas.budgetmanagement.dto.requestDto.CreateTransferRequest;
import com.saas.budgetmanagement.dto.responseDto.BudgetTransferResponse;
import com.saas.budgetmanagement.service.BudgetTransferService;
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
@RequestMapping("/api/budget-management/budget-transfers/{tenantId}")
@RequiredArgsConstructor
@Tag(name = "Budget Transfer")
public class BudgetTransferController {

    private final BudgetTransferService budgetTransferService;
    private final PermissionUtil permissionUtil;

    @PostMapping
    public ResponseEntity<BudgetTransferResponse> requestTransfer(@PathVariable UUID tenantId,
                                                                    @Valid @RequestBody CreateTransferRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetTransferService.requestTransfer(request));
    }

@GetMapping("/{id}")
public ResponseEntity<BudgetTransferResponse> getById(@PathVariable UUID tenantId, @PathVariable UUID id) {
    return ResponseEntity.ok(budgetTransferService.getById(id));
}

    @GetMapping
    public ResponseEntity<Page<BudgetTransferResponse>> list(@PathVariable UUID tenantId,
                                                               @RequestParam(required = false) String status,
                                                               Pageable pageable) {
        return ResponseEntity.ok(budgetTransferService.list(status, pageable));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<BudgetTransferResponse> approve(@PathVariable UUID tenantId, @PathVariable UUID id,
                                                            @Valid @RequestBody ApprovalDecisionRequest request) {
        if (!permissionUtil.hasPermission(tenantId, "budget-transfer-approval")) {
            throw new AccessDeniedException("You do not have permission to approve budget transfers");
        }
        return ResponseEntity.ok(budgetTransferService.approve(id, request));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<BudgetTransferResponse> reject(@PathVariable UUID tenantId, @PathVariable UUID id,
                                                           @Valid @RequestBody ApprovalDecisionRequest request) {
        if (!permissionUtil.hasPermission(tenantId, "budget-transfer-approval")) {
            throw new AccessDeniedException("You do not have permission to approve budget transfers");
        }
        return ResponseEntity.ok(budgetTransferService.reject(id, request));
    }
}