package com.saas.budgetmanagement.repository;

import com.saas.budgetmanagement.model.BudgetAllocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;

public interface BudgetAllocationRepository extends JpaRepository<BudgetAllocation, UUID> {

    Optional<BudgetAllocation> findByTenantIdAndId(UUID tenantId, UUID id);

    Page<BudgetAllocation> findByTenantIdAndBudgetPlanId(UUID tenantId, UUID budgetPlanId, Pageable pageable);

    List<BudgetAllocation> findByTenantIdAndAccountIdAndCostCenterId(
            UUID tenantId, UUID accountId, UUID costCenterId);

    List<BudgetAllocation> findByTenantIdAndAccountIdAndCostCenterIdAndDepartmentIdAndFundSourceIdAndProjectId(
            UUID tenantId, UUID accountId, UUID costCenterId, UUID departmentId, UUID fundSourceId, UUID projectId);


        @Query("SELECT COALESCE(SUM(a.allocatedAmount), 0) FROM BudgetAllocation a " +
        "WHERE a.tenantId = :tenantId AND a.budgetPlanId = :budgetPlanId")
        BigDecimal sumAllocatedAmountByPlan(@Param("tenantId") UUID tenantId, @Param("budgetPlanId") UUID budgetPlanId);
}