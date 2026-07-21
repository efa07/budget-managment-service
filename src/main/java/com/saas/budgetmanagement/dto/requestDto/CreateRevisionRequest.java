package com.saas.budgetmanagement.dto.requestDto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateRevisionRequest {

    @NotNull(message = "budgetPlanId is required")
    private UUID budgetPlanId;

    @NotNull(message = "revisedAmount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "revisedAmount must be greater than zero")
    private BigDecimal revisedAmount;

    @NotBlank(message = "reason is required")
    private String reason;
}