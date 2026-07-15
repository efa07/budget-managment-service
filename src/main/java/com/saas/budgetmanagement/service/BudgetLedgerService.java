package com.saas.budgetmanagement.service;

import java.util.UUID;

public interface BudgetLedgerService {
    void recordLedgerEntry(UUID tenantId);
}