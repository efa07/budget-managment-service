package com.saas.budgetmanagement.repository;

import com.saas.budgetmanagement.model.BudgetRevision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BudgetRevisionRepository extends JpaRepository<BudgetRevision, UUID> {
}