package com.saas.budgetmanagement.repository.refcache;

import com.saas.budgetmanagement.model.refcache.RefAccountingPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefAccountingPeriodRepository extends JpaRepository<RefAccountingPeriod, UUID> {

    Optional<RefAccountingPeriod> findByTenantIdAndExternalId(UUID tenantId, UUID externalId);

    List<RefAccountingPeriod> findByTenantIdAndFiscalYearExternalId(UUID tenantId, UUID fiscalYearExternalId);

    Optional<RefAccountingPeriod> findByTenantIdAndExternalIdAndOpenTrue(UUID tenantId, UUID externalId);
}