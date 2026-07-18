package com.saas.budgetmanagement.dto.clientDto;

import lombok.Data;

import java.util.UUID;

@Data
public class TenantClientDto {
    private UUID tenantId;
    private String tenantName;
    private boolean active;
}