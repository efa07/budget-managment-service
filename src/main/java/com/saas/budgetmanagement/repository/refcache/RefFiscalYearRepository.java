package com.saas.budgetmanagement.repository.refcache;

import com.saas.budgetmanagement.model.refcache.RefFiscalYear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefFiscalYearRepository extends JpaRepository<RefFiscalYear, UUID> {

    Optional<RefFiscalYear> findByTenantIdAndExternalId(UUID tenantId, UUID externalId);

    Optional<RefFiscalYear> findByTenantIdAndOpenTrue(UUID tenantId);
}