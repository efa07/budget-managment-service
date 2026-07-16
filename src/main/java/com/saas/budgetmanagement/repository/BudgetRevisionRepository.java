package com.saas.budgetmanagement.repository;

import com.saas.budgetmanagement.model.BudgetRevision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BudgetRevisionRepository extends JpaRepository<BudgetRevision, UUID> {

    Optional<BudgetRevision> findByTenantIdAndId(UUID tenantId, UUID id);

    List<BudgetRevision> findByTenantIdAndBudgetPlanId(UUID tenantId, UUID budgetPlanId);
}