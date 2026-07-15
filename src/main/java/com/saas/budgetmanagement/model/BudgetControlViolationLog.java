package com.saas.budgetmanagement.model;

import com.saas.budgetmanagement.enums.ControlMode;
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
    name = "budget_control_violation_logs",
    indexes = {
        @Index(name = "idx_control_violation_coa", columnList = "accountId, costCenterId"),
        @Index(name = "idx_control_violation_request_ref", columnList = "requestReference")
    }
)
@Data
@EqualsAndHashCode(callSuper = true)
public class BudgetControlViolationLog extends Base {

    @Column(nullable = false)
    private String requestReference;

    // --- COA segment references (external_id values from the ref_* cache tables, not live FKs) ---
    @Column(nullable = false)
    private UUID accountId;

    private UUID costCenterId;

    private UUID departmentId;

    private UUID fundSourceId;

    private UUID projectId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal requestedAmount;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal availableAmountAtTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ControlMode mode;
}