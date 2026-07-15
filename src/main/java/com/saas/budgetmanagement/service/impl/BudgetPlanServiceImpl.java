package com.saas.budgetmanagement.service.impl;

import com.saas.budgetmanagement.dto.requestDto.CreateBudgetPlanRequest;
import com.saas.budgetmanagement.service.BudgetPlanService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BudgetPlanServiceImpl implements BudgetPlanService {

    @Override
    public void createBudgetPlan(UUID tenantId, CreateBudgetPlanRequest request) {
        throw new UnsupportedOperationException("TODO");
    }
}