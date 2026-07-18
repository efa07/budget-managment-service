package com.saas.budgetmanagement.dto.eventDto.inbound;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class FiscalYearSyncEvent {
    private UUID externalId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean open;
}