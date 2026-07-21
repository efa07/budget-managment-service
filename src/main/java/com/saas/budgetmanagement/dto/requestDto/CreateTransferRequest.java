package com.saas.budgetmanagement.dto.requestDto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateTransferRequest {

    @NotNull(message = "fromAllocationId is required")
    private UUID fromAllocationId;

    @NotNull(message = "toAllocationId is required")
    private UUID toAllocationId;

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "amount must be greater than zero")
    private BigDecimal amount;

    private String justification;
}