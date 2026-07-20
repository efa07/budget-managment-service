package com.saas.budgetmanagement.dto.requestDto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateBudgetPlanRequest {

    private BigDecimal requestedAmount;
    private List<BudgetLineItemRequest> lines;
}
