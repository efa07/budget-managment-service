package com.saas.budgetmanagement.dto.requestDto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class CreateBudgetPlanRequest {

    private UUID fiscalYearId;
    private BigDecimal requestedAmount;
    private List<BudgetLineItemRequest> lines;
}
