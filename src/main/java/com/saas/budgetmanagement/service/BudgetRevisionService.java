package com.saas.budgetmanagement.service;

import com.saas.budgetmanagement.dto.requestDto.CreateRevisionRequest;
import com.saas.budgetmanagement.dto.responseDto.BudgetRevisionResponse;

import java.util.List;
import java.util.UUID;

public interface BudgetRevisionService {

    BudgetRevisionResponse create(CreateRevisionRequest request);

    List<BudgetRevisionResponse> listByPlan(UUID budgetPlanId);
}