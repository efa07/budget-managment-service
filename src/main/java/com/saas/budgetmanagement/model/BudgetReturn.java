package com.saas.budgetmanagement.model;

import com.saas.budgetmanagement.enums.BudgetReturnStatus;
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
    name = "budget_returns",
    indexes = {
        @Index(name = "idx_budget_return_allocation", columnList = "allocationId"),
        @Index(name = "idx_budget_return_status", columnList = "status")
    }
)
@Data
@EqualsAndHashCode(callSuper = true)
public class BudgetReturn extends Base {

    @Column(nullable = false)
    private UUID allocationId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BudgetReturnStatus status;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private UUID requestedBy;

    private UUID approvedBy;
}