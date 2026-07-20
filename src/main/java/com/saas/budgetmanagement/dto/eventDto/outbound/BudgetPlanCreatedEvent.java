package com.saas.budgetmanagement.dto.eventDto.outbound;

import com.saas.budgetmanagement.enums.BudgetPlanStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class BudgetPlanCreatedEvent {

    private UUID id;
    private UUID tenantId;
    private UUID fiscalYearId;
    private BigDecimal requestedAmount;
    private BudgetPlanStatus status;
    private UUID requestedBy;
}
