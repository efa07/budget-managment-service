package com.saas.budgetmanagement.repository;

import com.saas.budgetmanagement.model.BudgetLedger;

import jakarta.persistence.LockModeType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface BudgetLedgerRepository extends JpaRepository<BudgetLedger, UUID> {

    Optional<BudgetLedger> findByTenantIdAndId(UUID tenantId, UUID id);

    Optional<BudgetLedger> findByTenantIdAndFiscalYearIdAndPeriodIdAndAccountIdAndCostCenterIdAndDepartmentIdAndFundSourceIdAndProjectId(
            UUID tenantId, UUID fiscalYearId, UUID periodId,
            UUID accountId, UUID costCenterId, UUID departmentId, UUID fundSourceId, UUID projectId);

    Page<BudgetLedger> findByTenantIdAndFiscalYearIdAndPeriodId(
            UUID tenantId, UUID fiscalYearId, UUID periodId, Pageable pageable);

    List<BudgetLedger> findByTenantIdAndAccountIdAndCostCenterId(
            UUID tenantId, UUID accountId, UUID costCenterId);



@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT l FROM BudgetLedger l WHERE l.tenantId = :tenantId AND l.fiscalYearId = :fiscalYearId " +
       "AND l.periodId = :periodId AND l.accountId = :accountId " +
       "AND (:costCenterId IS NULL AND l.costCenterId IS NULL OR l.costCenterId = :costCenterId) " +
       "AND (:departmentId IS NULL AND l.departmentId IS NULL OR l.departmentId = :departmentId) " +
       "AND (:fundSourceId IS NULL AND l.fundSourceId IS NULL OR l.fundSourceId = :fundSourceId) " +
       "AND (:projectId IS NULL AND l.projectId IS NULL OR l.projectId = :projectId)")
Optional<BudgetLedger> findForUpdateByScope(
        @Param("tenantId") UUID tenantId, @Param("fiscalYearId") UUID fiscalYearId, @Param("periodId") UUID periodId,
        @Param("accountId") UUID accountId, @Param("costCenterId") UUID costCenterId,
        @Param("departmentId") UUID departmentId, @Param("fundSourceId") UUID fundSourceId, @Param("projectId") UUID projectId);
}