package com.saas.budgetmanagement.dto.eventDto.inbound;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CommitmentSourceEvent {

    private String sourceEventId;
    private String sourceReferenceId;

    private UUID fiscalYearId;
    private UUID periodId;

    private UUID accountId;
    private UUID costCenterId;
    private UUID departmentId;
    private UUID fundSourceId;
    private UUID projectId;

    private BigDecimal amount;
    private CommitmentAction action;

    public enum CommitmentAction {
        REGISTER, ADJUST, LIQUIDATE
    }
}