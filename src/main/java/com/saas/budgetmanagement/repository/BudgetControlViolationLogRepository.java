package com.saas.budgetmanagement.repository;

import com.saas.budgetmanagement.enums.ControlMode;
import com.saas.budgetmanagement.model.BudgetControlViolationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BudgetControlViolationLogRepository extends JpaRepository<BudgetControlViolationLog, UUID> {

    Page<BudgetControlViolationLog> findByTenantId(UUID tenantId, Pageable pageable);

    Page<BudgetControlViolationLog> findByTenantIdAndMode(UUID tenantId, ControlMode mode, Pageable pageable);

    List<BudgetControlViolationLog> findByTenantIdAndAccountIdAndCostCenterId(
            UUID tenantId, UUID accountId, UUID costCenterId);

    List<BudgetControlViolationLog> findByTenantIdAndRequestReference(UUID tenantId, String requestReference);
}