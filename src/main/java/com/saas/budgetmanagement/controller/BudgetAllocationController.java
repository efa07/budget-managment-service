package com.saas.budgetmanagement.controller;

import com.saas.budgetmanagement.dto.requestDto.CreateAllocationRequest;
import com.saas.budgetmanagement.dto.responseDto.BudgetAllocationResponse;
import com.saas.budgetmanagement.service.BudgetAllocationService;
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
@RequestMapping("/api/budget-management/budget-allocations/{tenantId}")
@RequiredArgsConstructor
@Tag(name = "Budget Allocation")
public class BudgetAllocationController {

    private final BudgetAllocationService budgetAllocationService;
    private final PermissionUtil permissionUtil;

    @PostMapping
    public ResponseEntity<BudgetAllocationResponse> create(@PathVariable UUID tenantId,
                                                             @Valid @RequestBody CreateAllocationRequest request) {
        if (!permissionUtil.hasPermission(tenantId, "budget-allocation-create")) {
            throw new AccessDeniedException("You do not have permission to create budget allocations");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetAllocationService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetAllocationResponse> getById(@PathVariable UUID tenantId, @PathVariable UUID id) {
        return ResponseEntity.ok(budgetAllocationService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<BudgetAllocationResponse>> list(@PathVariable UUID tenantId,
                                                                 @RequestParam UUID budgetPlanId,
                                                                 Pageable pageable) {
        return ResponseEntity.ok(budgetAllocationService.list(budgetPlanId, pageable));
    }
}