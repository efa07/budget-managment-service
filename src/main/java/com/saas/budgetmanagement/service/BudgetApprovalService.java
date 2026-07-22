package com.saas.budgetmanagement.service;

import com.saas.budgetmanagement.model.BudgetPlan;

import java.util.UUID;

public interface BudgetApprovalService {

    void assertCanApprove(UUID tenantId, UUID approverId, BudgetPlan plan);
}