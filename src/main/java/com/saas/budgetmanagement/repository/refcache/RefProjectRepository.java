package com.saas.budgetmanagement.repository.refcache;

import com.saas.budgetmanagement.model.refcache.RefProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefProjectRepository extends JpaRepository<RefProject, UUID> {

    Optional<RefProject> findByTenantIdAndExternalId(UUID tenantId, UUID externalId);

    Optional<RefProject> findByTenantIdAndCode(UUID tenantId, String code);

    Optional<RefProject> findByTenantIdAndExternalIdAndActiveTrue(UUID tenantId, UUID externalId);

    List<RefProject> findByTenantIdAndActiveTrue(UUID tenantId);
}