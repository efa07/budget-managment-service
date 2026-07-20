package com.saas.budgetmanagement.dto.responseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class BudgetLedgerResponse extends BaseResponse {

    private UUID fiscalYearId;
    private UUID periodId;

    private UUID accountId;
    private UUID costCenterId;
    private UUID departmentId;
    private UUID fundSourceId;
    private UUID projectId;

    private BigDecimal allocatedAmount;
    private BigDecimal committedAmount;
    private BigDecimal actualAmount;
    private BigDecimal availableAmount;

    private LocalDateTime lastUpdatedAt;
}