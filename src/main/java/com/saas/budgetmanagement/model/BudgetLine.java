package com.saas.budgetmanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(
    name = "budget_lines",
    indexes = {
        @Index(name = "idx_budget_line_plan_coa", columnList = "budgetPlanId, accountId, costCenterId")
    }
)
@Data
@EqualsAndHashCode(callSuper = true)
public class BudgetLine extends Base {

    @Column(nullable = false)
    private UUID budgetPlanId;

    // --- COA segment references (external_id values from the ref_* cache tables, not live FKs) ---
    @Column(nullable = false)
    private UUID accountId;

    private UUID costCenterId;

    private UUID departmentId;

    private UUID fundSourceId;

    private UUID projectId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    private String notes;
}