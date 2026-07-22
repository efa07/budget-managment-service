package com.saas.budgetmanagement.service;

import com.saas.budgetmanagement.dto.requestDto.CreateCashRequirementEstimateRequest;
import com.saas.budgetmanagement.dto.responseDto.CashRequirementEstimateResponse;

import java.util.List;
import java.util.UUID;

public interface CashRequirementEstimateService {

    CashRequirementEstimateResponse create(CreateCashRequirementEstimateRequest request);

    CashRequirementEstimateResponse approve(UUID id);

    List<CashRequirementEstimateResponse> listByPlan(UUID budgetPlanId);
}