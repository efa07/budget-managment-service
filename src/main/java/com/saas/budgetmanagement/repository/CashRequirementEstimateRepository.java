package com.saas.budgetmanagement.repository;

import com.saas.budgetmanagement.enums.CashRequirementEstimateStatus;
import com.saas.budgetmanagement.model.CashRequirementEstimate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CashRequirementEstimateRepository extends JpaRepository<CashRequirementEstimate, UUID> {

    Optional<CashRequirementEstimate> findByTenantIdAndId(UUID tenantId, UUID id);

    List<CashRequirementEstimate> findByTenantIdAndBudgetPlanId(UUID tenantId, UUID budgetPlanId);

    List<CashRequirementEstimate> findByTenantIdAndPeriodIdAndStatus(
            UUID tenantId, UUID periodId, CashRequirementEstimateStatus status);
}