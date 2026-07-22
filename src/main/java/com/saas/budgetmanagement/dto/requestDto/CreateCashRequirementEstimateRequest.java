package com.saas.budgetmanagement.dto.requestDto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateCashRequirementEstimateRequest {

    @NotNull(message = "budgetPlanId is required")
    private UUID budgetPlanId;

    @NotNull(message = "periodId is required")
    private UUID periodId;

    @NotNull(message = "estimatedAmount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "estimatedAmount must be greater than zero")
    private BigDecimal estimatedAmount;
}