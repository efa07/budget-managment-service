package com.saas.budgetmanagement.repository.refcache;

import com.saas.budgetmanagement.model.refcache.RefCoaAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefCoaAccountRepository extends JpaRepository<RefCoaAccount, UUID> {

    Optional<RefCoaAccount> findByTenantIdAndExternalId(UUID tenantId, UUID externalId);

    Optional<RefCoaAccount> findByTenantIdAndAccountCode(UUID tenantId, String accountCode);

    Optional<RefCoaAccount> findByTenantIdAndExternalIdAndActiveTrue(UUID tenantId, UUID externalId);

    List<RefCoaAccount> findByTenantIdAndActiveTrue(UUID tenantId);
}