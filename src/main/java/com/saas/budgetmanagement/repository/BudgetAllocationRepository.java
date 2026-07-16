package com.saas.budgetmanagement.repository;

import com.saas.budgetmanagement.model.BudgetAllocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BudgetAllocationRepository extends JpaRepository<BudgetAllocation, UUID> {

    List<BudgetAllocation> findByTenantIdAndBudgetPlanId(UUID tenantId, UUID budgetPlanId);

    List<BudgetAllocation> findByTenantIdAndAccountIdAndCostCenterId(
            UUID tenantId, UUID accountId, UUID costCenterId);

    List<BudgetAllocation> findByTenantIdAndAccountIdAndCostCenterIdAndDepartmentIdAndFundSourceIdAndProjectId(
            UUID tenantId, UUID accountId, UUID costCenterId, UUID departmentId, UUID fundSourceId, UUID projectId);
}