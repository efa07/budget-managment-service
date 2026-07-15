package com.saas.budgetmanagement.model.refcache;

import com.saas.budgetmanagement.model.Base;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "ref_accounting_periods",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_ref_period_external", columnNames = {"tenantId", "externalId"})
    },
    indexes = {
        @Index(name = "idx_ref_period_fiscal_year", columnList = "fiscalYearExternalId")
    }
)
@Data
@EqualsAndHashCode(callSuper = true)
public class RefAccountingPeriod extends Base {

    @Column(nullable = false)
    private UUID externalId;

    @Column(nullable = false)
    private UUID fiscalYearExternalId;

    @Column(nullable = false)
    private Integer periodNumber;

    @Column(nullable = false)
    private String periodName;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private boolean open;

    @Column(nullable = false)
    private LocalDateTime syncedAt;
}