package com.saas.budgetmanagement.dto.responseDto;

import com.saas.budgetmanagement.enums.BudgetPlanStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class BudgetPlanResponse extends BaseResponse {

    private UUID fiscalYearId;
    private String fiscalYearName;

    private BigDecimal requestedAmount;
    private BudgetPlanStatus status;
    private UUID requestedBy;
    private UUID approvedBy;
    private String rejectionReason;

    private List<BudgetLineResponse> lines;
}