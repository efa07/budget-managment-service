package com.saas.budgetmanagement.repository;

import com.saas.budgetmanagement.model.BudgetLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BudgetLineRepository extends JpaRepository<BudgetLine, UUID> {

    List<BudgetLine> findByTenantIdAndBudgetPlanId(UUID tenantId, UUID budgetPlanId);

    List<BudgetLine> findByTenantIdAndBudgetPlanIdAndAccountIdAndCostCenterId(
            UUID tenantId, UUID budgetPlanId, UUID accountId, UUID costCenterId);

    void deleteByTenantIdAndBudgetPlanId(UUID tenantId, UUID budgetPlanId);
}