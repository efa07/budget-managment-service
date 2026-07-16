package com.saas.budgetmanagement.repository;

import com.saas.budgetmanagement.enums.BudgetPlanStatus;
import com.saas.budgetmanagement.model.BudgetPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BudgetPlanRepository extends JpaRepository<BudgetPlan, UUID> {

    Optional<BudgetPlan> findByTenantIdAndId(UUID tenantId, UUID id);

    Page<BudgetPlan> findByTenantId(UUID tenantId, Pageable pageable);

    Page<BudgetPlan> findByTenantIdAndFiscalYearId(UUID tenantId, UUID fiscalYearId, Pageable pageable);

    Page<BudgetPlan> findByTenantIdAndStatus(UUID tenantId, BudgetPlanStatus status, Pageable pageable);

    List<BudgetPlan> findByTenantIdAndRequestedBy(UUID tenantId, UUID requestedBy);
}