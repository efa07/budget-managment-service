package com.saas.budgetmanagement.model;

import com.saas.budgetmanagement.enums.BudgetPlanStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(
    name = "budget_plans",
    indexes = {
        @Index(name = "idx_budget_plan_fiscal_year", columnList = "fiscalYearId"),
        @Index(name = "idx_budget_plan_status", columnList = "status")
    }
)
@Data
@EqualsAndHashCode(callSuper = true)
public class BudgetPlan extends Base {

    @Column(nullable = false)
    private UUID fiscalYearId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal requestedAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BudgetPlanStatus status;

    @Column(nullable = false)
    private UUID requestedBy;

    private UUID approvedBy;

    private String rejectionReason;
}