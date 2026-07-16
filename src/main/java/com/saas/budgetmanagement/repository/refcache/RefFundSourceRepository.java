package com.saas.budgetmanagement.repository.refcache;

import com.saas.budgetmanagement.model.refcache.RefFundSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefFundSourceRepository extends JpaRepository<RefFundSource, UUID> {

    Optional<RefFundSource> findByTenantIdAndExternalId(UUID tenantId, UUID externalId);

    Optional<RefFundSource> findByTenantIdAndCode(UUID tenantId, String code);

    Optional<RefFundSource> findByTenantIdAndExternalIdAndActiveTrue(UUID tenantId, UUID externalId);

    List<RefFundSource> findByTenantIdAndActiveTrue(UUID tenantId);
}