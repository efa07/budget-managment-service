package com.saas.budgetmanagement.dto.responseDto;

import com.saas.budgetmanagement.enums.CashRequirementEstimateStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class CashRequirementEstimateResponse extends BaseResponse {

    private UUID budgetPlanId;
    private UUID periodId;
    private BigDecimal estimatedAmount;
    private CashRequirementEstimateStatus status;
    private UUID approvedBy;
    private String advisoryNote;
}