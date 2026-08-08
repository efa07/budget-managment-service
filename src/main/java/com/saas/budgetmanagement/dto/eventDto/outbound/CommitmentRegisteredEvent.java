package com.saas.budgetmanagement.dto.eventDto.outbound;

import com.saas.budgetmanagement.enums.CommitmentStatus;
import com.saas.budgetmanagement.enums.SourceService;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CommitmentRegisteredEvent {

    private UUID id;
    private UUID tenantId;

    private SourceService sourceService;
    private String sourceReferenceId;
    private String sourceEventId;

    private UUID fiscalYearId;
    private UUID periodId;

    private UUID accountId;
    private UUID costCenterId;
    private UUID departmentId;
    private UUID fundSourceId;
    private UUID projectId;

    private BigDecimal amount;
    private CommitmentStatus status;
}