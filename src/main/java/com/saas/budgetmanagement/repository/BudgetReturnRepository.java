package com.saas.budgetmanagement.repository;

import com.saas.budgetmanagement.enums.BudgetReturnStatus;
import com.saas.budgetmanagement.model.BudgetReturn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BudgetReturnRepository extends JpaRepository<BudgetReturn, UUID> {

    Optional<BudgetReturn> findByTenantIdAndId(UUID tenantId, UUID id);

    Page<BudgetReturn> findByTenantId(UUID tenantId, Pageable pageable);

    Page<BudgetReturn> findByTenantIdAndStatus(UUID tenantId, BudgetReturnStatus status, Pageable pageable);
}