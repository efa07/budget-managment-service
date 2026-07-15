package com.saas.budgetmanagement.model.refcache;

import com.saas.budgetmanagement.model.Base;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "ref_fiscal_years",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_ref_fiscal_year_external", columnNames = {"tenantId", "externalId"})
    }
)
@Data
@EqualsAndHashCode(callSuper = true)
public class RefFiscalYear extends Base {

    @Column(nullable = false)
    private UUID externalId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private boolean open;

    @Column(nullable = false)
    private LocalDateTime syncedAt;
}