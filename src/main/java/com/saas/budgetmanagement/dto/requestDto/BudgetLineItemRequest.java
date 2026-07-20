package com.saas.budgetmanagement.dto.requestDto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class BudgetLineItemRequest {

    private UUID accountId;
    private UUID costCenterId;
    private UUID departmentId;
    private UUID fundSourceId;
    private UUID projectId;
    private BigDecimal amount;
    private String notes;
}
