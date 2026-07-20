package com.saas.budgetmanagement.dto.requestDto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class BudgetLineItemRequest {

    @NotNull(message = "accountId is required")
    private UUID accountId;

    private UUID costCenterId;

    private UUID departmentId;

    private UUID fundSourceId;

    private UUID projectId;

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "amount must be greater than zero")
    private BigDecimal amount;

    private String notes;
}