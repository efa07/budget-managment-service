package com.saas.budgetmanagement.model.refcache;

import com.saas.budgetmanagement.model.Base;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "ref_fund_sources",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_ref_fund_source_external", columnNames = {"tenantId", "externalId"})
    },
    indexes = {
        @Index(name = "idx_ref_fund_source_code", columnList = "tenantId, code")
    }
)
@Data
@EqualsAndHashCode(callSuper = true)
public class RefFundSource extends Base {

    @Column(nullable = false)
    private UUID externalId;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private LocalDateTime syncedAt;
}