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
    private String accountName;

    private UUID costCenterId;
    private String costCenterName;

    private UUID departmentId;
    private String departmentName;

    private UUID fundSourceId;
    private String fundSourceName;

    private UUID projectId;
    private String projectName;

    private BigDecimal allocatedAmount;
    private BigDecimal committedAmount;
    private BigDecimal actualAmount;
    private BigDecimal availableAmount;

    private LocalDateTime lastUpdatedAt;
}