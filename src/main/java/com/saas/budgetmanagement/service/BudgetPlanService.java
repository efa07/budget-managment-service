package com.saas.budgetmanagement.service;

import com.saas.budgetmanagement.dto.requestDto.CreateBudgetPlanRequest;
import java.util.UUID;

public interface BudgetPlanService {
    void createBudgetPlan(UUID tenantId, CreateBudgetPlanRequest request);
}