package com.saas.budgetmanagement.dto.responseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class BudgetRevisionResponse extends BaseResponse {

    private UUID budgetPlanId;
    private BigDecimal originalAmount;
    private BigDecimal revisedAmount;
    private String reason;
}