package com.saas.budgetmanagement.repository;

import com.saas.budgetmanagement.model.BudgetPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BudgetPlanRepository extends JpaRepository<BudgetPlan, UUID> {
}