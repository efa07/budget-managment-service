package com.saas.budgetmanagement.dto.eventDto.outbound;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetAllocationCreatedEvent {

    private UUID id;
    private UUID tenantId;
    private UUID budgetPlanId;
    private UUID budgetLineId;
    private UUID accountId;
    private UUID periodId;
    private UUID costCenterId;
    private UUID departmentId;
    private UUID fundSourceId;
    private UUID projectId;
    private BigDecimal allocatedAmount;
    private LocalDateTime allocatedAt;
    private UUID allocatedBy;

}