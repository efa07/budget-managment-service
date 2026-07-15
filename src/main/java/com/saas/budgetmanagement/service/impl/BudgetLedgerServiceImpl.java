package com.saas.budgetmanagement.service.impl;

import com.saas.budgetmanagement.service.BudgetLedgerService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BudgetLedgerServiceImpl implements BudgetLedgerService {

    @Override
    public void recordLedgerEntry(UUID tenantId) {
        throw new UnsupportedOperationException("TODO");
    }
}