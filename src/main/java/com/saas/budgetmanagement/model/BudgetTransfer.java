package com.saas.budgetmanagement.model;

import com.saas.budgetmanagement.enums.TransferStatus;
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
    name = "budget_transfers",
    indexes = {
        @Index(name = "idx_budget_transfer_from", columnList = "fromAllocationId"),
        @Index(name = "idx_budget_transfer_to", columnList = "toAllocationId"),
        @Index(name = "idx_budget_transfer_status", columnList = "status")
    }
)
@Data
@EqualsAndHashCode(callSuper = true)
public class BudgetTransfer extends Base {

    @Column(nullable = false)
    private UUID fromAllocationId;

    @Column(nullable = false)
    private UUID toAllocationId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferStatus status;

    private String justification;

    @Column(nullable = false)
    private UUID requestedBy;

    private UUID approvedBy;
}