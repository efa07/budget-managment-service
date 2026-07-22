package com.saas.budgetmanagement.controller;

import com.saas.budgetmanagement.dto.requestDto.CreateRevisionRequest;
import com.saas.budgetmanagement.dto.responseDto.BudgetRevisionResponse;
import com.saas.budgetmanagement.service.BudgetRevisionService;
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
@RequestMapping("/api/budget-management/budget-revisions/{tenantId}")
@RequiredArgsConstructor
@Tag(name = "Budget Revision")
public class BudgetRevisionController {

    private final BudgetRevisionService budgetRevisionService;
    private final PermissionUtil permissionUtil;

    @PostMapping
    public ResponseEntity<BudgetRevisionResponse> create(@PathVariable UUID tenantId,
                                                           @Valid @RequestBody CreateRevisionRequest request) {
        if (!permissionUtil.hasPermission(tenantId, "budget-revision-create")) {
            throw new AccessDeniedException("You do not have permission to create budget revisions");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetRevisionService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<BudgetRevisionResponse>> listByPlan(@PathVariable UUID tenantId,
                                                                     @RequestParam UUID budgetPlanId) {
        return ResponseEntity.ok(budgetRevisionService.listByPlan(budgetPlanId));
    }
}