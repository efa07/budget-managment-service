package com.saas.budgetmanagement.repository;

import com.saas.budgetmanagement.model.BudgetTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BudgetTransferRepository extends JpaRepository<BudgetTransfer, UUID> {
}