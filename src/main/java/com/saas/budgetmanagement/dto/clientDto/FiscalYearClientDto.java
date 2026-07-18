package com.saas.budgetmanagement.dto.clientDto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class FiscalYearClientDto {
    private UUID externalId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean open;
}