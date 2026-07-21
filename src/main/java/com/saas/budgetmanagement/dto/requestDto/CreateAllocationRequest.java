package com.saas.budgetmanagement.dto.requestDto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateAllocationRequest {

    @NotNull(message = "budgetPlanId is required")
    private UUID budgetPlanId;

    private UUID budgetLineId;

    @NotNull(message = "periodId is required")
    private UUID periodId;

    @NotNull(message = "accountId is required")
    private UUID accountId;

    private UUID costCenterId;
    private UUID departmentId;
    private UUID fundSourceId;
    private UUID projectId;

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "amount must be greater than zero")
    private BigDecimal amount;
}