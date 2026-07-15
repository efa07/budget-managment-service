package com.saas.budgetmanagement.repository;

import com.saas.budgetmanagement.model.BudgetLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BudgetLineRepository extends JpaRepository<BudgetLine, UUID> {
}