package com.saas.budgetmanagement.dto.eventDto.outbound;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class BudgetRevisionCreatedEvent {

    private UUID id;
    private UUID tenantId;
    private UUID budgetPlanId;
    private BigDecimal originalAmount;
    private BigDecimal revisedAmount;
    private String reason;
}