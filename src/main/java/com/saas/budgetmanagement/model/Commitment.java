package com.saas.budgetmanagement.model;

import com.saas.budgetmanagement.enums.CommitmentStatus;
import com.saas.budgetmanagement.enums.SourceService;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(
    name = "commitments",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_commitment_event",
            columnNames = {"sourceService", "sourceEventId"}
        )
    },
    indexes = {
        @Index(name = "idx_commitment_source_ref", columnList = "sourceService, sourceReferenceId"),
        @Index(name = "idx_commitment_coa", columnList = "accountId, costCenterId"),
        @Index(name = "idx_commitment_status", columnList = "status")
    }
)
@Data
@EqualsAndHashCode(callSuper = true)
public class Commitment extends Base {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SourceService sourceService;

    @Column(nullable = false)
    private String sourceReferenceId;

    @Column(nullable = false)
    private String sourceEventId;

    // --- COA segment references (external_id values from the ref_* cache tables, not live FKs) ---
    @Column(nullable = false)
    private UUID accountId;

    @Column(nullable = false)
    private UUID fiscalYearId;

    @Column(nullable = false)
    private UUID periodId;

    private UUID costCenterId;

    private UUID departmentId;

    private UUID fundSourceId;

    private UUID projectId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommitmentStatus status;
}