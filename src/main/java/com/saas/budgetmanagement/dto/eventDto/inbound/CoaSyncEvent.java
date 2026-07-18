package com.saas.budgetmanagement.dto.eventDto.inbound;

import lombok.Data;

import java.util.UUID;

@Data
public class CoaSyncEvent {
    private CoaSegmentType segmentType;
    private UUID externalId;
    private String code;
    private String name;
    private String accountType;   // only populated when segmentType == ACCOUNT, null otherwise
    private boolean active;

    public enum CoaSegmentType {
        ACCOUNT, COST_CENTER, DEPARTMENT, FUND_SOURCE, PROJECT
    }
}