package com.saas.budgetmanagement.dto.responseDto;

import com.saas.budgetmanagement.utility.CoaSegmentDisplayAware;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class BudgetLineResponse extends BaseResponse implements CoaSegmentDisplayAware {

    private UUID budgetPlanId;

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

    private BigDecimal amount;
    private String notes;
}