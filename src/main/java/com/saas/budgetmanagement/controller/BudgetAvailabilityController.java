package com.saas.budgetmanagement.controller;

import com.saas.budgetmanagement.dto.requestDto.BudgetAvailabilityCheckRequest;
import com.saas.budgetmanagement.dto.responseDto.BudgetAvailabilityCheckResponse;
import com.saas.budgetmanagement.service.BudgetAvailabilityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/budget-management/budget-availability/{tenantId}")
@RequiredArgsConstructor
@Tag(name = "Budget Availability")
public class BudgetAvailabilityController {

    private final BudgetAvailabilityService budgetAvailabilityService;

    @PostMapping("/check")
    public ResponseEntity<BudgetAvailabilityCheckResponse> check(@PathVariable UUID tenantId,
                                                                   @Valid @RequestBody BudgetAvailabilityCheckRequest request) {
        return ResponseEntity.ok(budgetAvailabilityService.checkAvailability(request));
    }
}