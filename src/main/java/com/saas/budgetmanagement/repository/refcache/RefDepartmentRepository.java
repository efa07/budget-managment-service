package com.saas.budgetmanagement.repository.refcache;

import com.saas.budgetmanagement.model.refcache.RefDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefDepartmentRepository extends JpaRepository<RefDepartment, UUID> {

    Optional<RefDepartment> findByTenantIdAndExternalId(UUID tenantId, UUID externalId);

    Optional<RefDepartment> findByTenantIdAndCode(UUID tenantId, String code);

    Optional<RefDepartment> findByTenantIdAndExternalIdAndActiveTrue(UUID tenantId, UUID externalId);

    List<RefDepartment> findByTenantIdAndActiveTrue(UUID tenantId);
}