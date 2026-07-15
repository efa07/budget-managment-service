package com.saas.budgetmanagement.service;

import com.saas.budgetmanagement.dto.requestDto.CreateCashRequirementEstimateRequest;
import java.util.UUID;

public interface CashRequirementEstimateService {
    void createEstimate(UUID tenantId, CreateCashRequirementEstimateRequest request);
}