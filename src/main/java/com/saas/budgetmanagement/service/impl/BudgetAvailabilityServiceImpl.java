package com.saas.budgetmanagement.service.impl;

import com.saas.budgetmanagement.dto.requestDto.BudgetAvailabilityCheckRequest;
import com.saas.budgetmanagement.service.BudgetAvailabilityService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BudgetAvailabilityServiceImpl implements BudgetAvailabilityService {

    @Override
    public void checkAvailability(UUID tenantId, BudgetAvailabilityCheckRequest request) {
        throw new UnsupportedOperationException("TODO");
    }
}