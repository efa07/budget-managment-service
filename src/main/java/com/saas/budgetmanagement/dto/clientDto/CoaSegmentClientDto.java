package com.saas.budgetmanagement.dto.clientDto;

import lombok.Data;

import java.util.UUID;

@Data
public class CoaSegmentClientDto {
    private UUID externalId;
    private String code;
    private String name;
    private String accountType;   // only relevant for accounts
    private boolean active;
}