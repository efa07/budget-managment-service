package com.saas.budgetmanagement.repository.refcache;

import com.saas.budgetmanagement.model.refcache.RefFiscalYear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefFiscalYearRepository extends JpaRepository<RefFiscalYear, UUID> {

    Optional<RefFiscalYear> findByTenantIdAndExternalId(UUID tenantId, UUID externalId);

    Optional<RefFiscalYear> findByTenantIdAndOpenTrue(UUID tenantId);

    List<RefFiscalYear> findByTenantIdAndExternalIdIn(UUID tenantId, List<UUID> externalIds);
}