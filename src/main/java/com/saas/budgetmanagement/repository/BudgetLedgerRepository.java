package com.saas.budgetmanagement.repository;

import com.saas.budgetmanagement.model.BudgetLedger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BudgetLedgerRepository extends JpaRepository<BudgetLedger, UUID> {
}