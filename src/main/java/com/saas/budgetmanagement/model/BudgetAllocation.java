package com.saas.budgetmanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "budget_allocations",
    indexes = {
        @Index(name = "idx_budget_allocation_coa", columnList = "accountId, costCenterId, budgetPlanId")
    }
)
@Data
@EqualsAndHashCode(callSuper = true)
public class BudgetAllocation extends Base {

    @Column(nullable = false)
    private UUID budgetPlanId;

    private UUID budgetLineId;

    // --- COA segment references (external_id values from the ref_* cache tables, not live FKs) ---
    @Column(nullable = false)
    private UUID accountId;
    @Column(nullable = false)
    private UUID periodId;
    private UUID costCenterId;

    private UUID departmentId;

    private UUID fundSourceId;

    private UUID projectId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal allocatedAmount;

    @Column(nullable = false)
    private LocalDateTime allocatedAt;

    @Column(nullable = false)
    private UUID allocatedBy;
}