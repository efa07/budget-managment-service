package com.saas.budgetmanagement.dto.responseDto;

import com.saas.budgetmanagement.enums.BudgetReturnStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class BudgetReturnResponse extends BaseResponse {

    private UUID allocationId;
    private BigDecimal amount;
    private BudgetReturnStatus status;
    private String reason;
    private UUID requestedBy;
    private UUID approvedBy;
}