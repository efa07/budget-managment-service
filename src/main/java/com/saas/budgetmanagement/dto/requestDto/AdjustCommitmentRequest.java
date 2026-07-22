package com.saas.budgetmanagement.dto.requestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdjustCommitmentRequest {

    @NotNull(message = "adjustmentAmount is required")
    private BigDecimal adjustmentAmount;

    @NotBlank(message = "reason is required")
    private String reason;
}