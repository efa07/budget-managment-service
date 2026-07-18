package com.saas.budgetmanagement.dto.eventDto.inbound;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class AccountingPeriodSyncEvent {
    private UUID externalId;
    private UUID fiscalYearExternalId;
    private Integer periodNumber;
    private String periodName;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean open;
}