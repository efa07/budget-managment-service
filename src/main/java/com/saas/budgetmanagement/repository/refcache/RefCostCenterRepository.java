package com.saas.budgetmanagement.repository.refcache;

import com.saas.budgetmanagement.model.refcache.RefCostCenter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefCostCenterRepository extends JpaRepository<RefCostCenter, UUID> {

    Optional<RefCostCenter> findByTenantIdAndExternalId(UUID tenantId, UUID externalId);

    Optional<RefCostCenter> findByTenantIdAndCode(UUID tenantId, String code);

    Optional<RefCostCenter> findByTenantIdAndExternalIdAndActiveTrue(UUID tenantId, UUID externalId);

    List<RefCostCenter> findByTenantIdAndActiveTrue(UUID tenantId);

    List<RefCostCenter> findByTenantIdAndExternalIdIn(UUID tenantId, List<UUID> externalIds);
}