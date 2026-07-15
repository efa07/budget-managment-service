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
    name = "budget_revisions",
    indexes = {
        @Index(name = "idx_budget_revision_plan", columnList = "budgetPlanId")
    }
)
@Data
@EqualsAndHashCode(callSuper = true)
public class BudgetRevision extends Base {

    @Column(nullable = false)
    private UUID budgetPlanId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal originalAmount;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal revisedAmount;

    @Column(nullable = false)
    private String reason;
}