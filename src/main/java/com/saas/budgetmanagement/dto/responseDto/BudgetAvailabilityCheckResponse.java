package com.saas.budgetmanagement.dto.responseDto;

import com.saas.budgetmanagement.enums.ControlMode;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class BudgetAvailabilityCheckResponse {

    private boolean allowed;
    private BigDecimal availableAmount;
    private BigDecimal requestedAmount;
    private ControlMode mode;
    private UUID violationId;
}