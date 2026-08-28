package com.saas.budgetmanagement.dto.eventDto.outbound;

import com.saas.budgetmanagement.enums.BudgetReturnStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class BudgetReturnRequestedEvent {

    private UUID id;
    private UUID tenantId;
    private UUID allocationId;
    private BigDecimal amount;
    private BudgetReturnStatus status;
    private String reason;
    private UUID requestedBy;
}