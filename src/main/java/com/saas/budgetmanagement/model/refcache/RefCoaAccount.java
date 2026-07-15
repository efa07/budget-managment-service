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
    name = "ref_coa_accounts",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_ref_coa_account_external", columnNames = {"tenantId", "externalId"})
    },
    indexes = {
        @Index(name = "idx_ref_coa_account_code", columnList = "tenantId, accountCode")
    }
)
@Data
@EqualsAndHashCode(callSuper = true)
public class RefCoaAccount extends Base {

    @Column(nullable = false)
    private UUID externalId;

    @Column(nullable = false)
    private String accountCode;

    @Column(nullable = false)
    private String accountName;

    @Column(nullable = false)
    private String accountType;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private LocalDateTime syncedAt;
}