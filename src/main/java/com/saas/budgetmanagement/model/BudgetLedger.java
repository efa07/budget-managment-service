package com.saas.budgetmanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "budget_ledger",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_budget_ledger_scope",
            columnNames = {"fiscalYearId", "periodId", "accountId", "costCenterId", "departmentId", "fundSourceId", "projectId"}
        )
    },
    indexes = {
        @Index(name = "idx_budget_ledger_coa_period", columnList = "accountId, costCenterId, periodId")
    }
)
@Data
@EqualsAndHashCode(callSuper = true)
public class BudgetLedger extends Base {

    @Column(nullable = false)
    private UUID fiscalYearId;

    @Column(nullable = false)
    private UUID periodId;

    // --- COA segment references (external_id values from the ref_* cache tables, not live FKs) ---
    @Column(nullable = false)
    private UUID accountId;

    private UUID costCenterId;

    private UUID departmentId;

    private UUID fundSourceId;

    private UUID projectId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal allocatedAmount;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal committedAmount;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal actualAmount;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal availableAmount;

    @Column(nullable = false)
    private LocalDateTime lastUpdatedAt;
}