package com.saas.budgetmanagement.dto.responseDto;

import com.saas.budgetmanagement.utility.CoaSegmentDisplayAware;
import com.saas.budgetmanagement.enums.CommitmentStatus;
import com.saas.budgetmanagement.enums.SourceService;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommitmentResponse extends BaseResponse implements CoaSegmentDisplayAware{

    private SourceService sourceService;
    private String sourceReferenceId;
    private String sourceEventId;

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

    private BigDecimal amount;
    private CommitmentStatus status;
}