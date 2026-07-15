package com.saas.budgetmanagement.repository;

import com.saas.budgetmanagement.model.CashRequirementEstimate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CashRequirementEstimateRepository extends JpaRepository<CashRequirementEstimate, UUID> {
}