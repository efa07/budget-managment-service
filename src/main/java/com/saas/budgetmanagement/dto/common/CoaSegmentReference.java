package com.saas.budgetmanagement.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoaSegmentReference {

    private UUID accountId;
    private UUID costCenterId;
    private UUID departmentId;
    private UUID fundSourceId;
    private UUID projectId;
}