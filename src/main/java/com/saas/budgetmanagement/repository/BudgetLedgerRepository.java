package com.saas.budgetmanagement.repository;

import com.saas.budgetmanagement.model.BudgetLedger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BudgetLedgerRepository extends JpaRepository<BudgetLedger, UUID> {

    Optional<BudgetLedger> findByTenantIdAndId(UUID tenantId, UUID id);

    Optional<BudgetLedger> findByTenantIdAndFiscalYearIdAndPeriodIdAndAccountIdAndCostCenterIdAndDepartmentIdAndFundSourceIdAndProjectId(
            UUID tenantId, UUID fiscalYearId, UUID periodId,
            UUID accountId, UUID costCenterId, UUID departmentId, UUID fundSourceId, UUID projectId);

    Page<BudgetLedger> findByTenantIdAndFiscalYearIdAndPeriodId(
            UUID tenantId, UUID fiscalYearId, UUID periodId, Pageable pageable);

    List<BudgetLedger> findByTenantIdAndAccountIdAndCostCenterId(
            UUID tenantId, UUID accountId, UUID costCenterId);
}