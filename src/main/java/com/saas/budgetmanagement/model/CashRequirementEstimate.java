package com.saas.budgetmanagement.model;

import com.saas.budgetmanagement.enums.CashRequirementEstimateStatus;
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
    name = "cash_requirement_estimates",
    indexes = {
        @Index(name = "idx_cash_req_est_plan_period", columnList = "budgetPlanId, periodId")
    }
)
@Data
@EqualsAndHashCode(callSuper = true)
public class CashRequirementEstimate extends Base {

    @Column(nullable = false)
    private UUID budgetPlanId;

    @Column(nullable = false)
    private UUID periodId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal estimatedAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CashRequirementEstimateStatus status;

    private UUID approvedBy;
}