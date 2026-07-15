package com.saas.budgetmanagement.service.impl;

import com.saas.budgetmanagement.dto.requestDto.CreateCashRequirementEstimateRequest;
import com.saas.budgetmanagement.service.CashRequirementEstimateService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CashRequirementEstimateServiceImpl implements CashRequirementEstimateService {

    @Override
    public void createEstimate(UUID tenantId, CreateCashRequirementEstimateRequest request) {
        throw new UnsupportedOperationException("TODO");
    }
}