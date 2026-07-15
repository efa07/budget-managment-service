package com.saas.budgetmanagement.service.impl;

import com.saas.budgetmanagement.dto.requestDto.CreateAllocationRequest;
import com.saas.budgetmanagement.service.BudgetAllocationService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BudgetAllocationServiceImpl implements BudgetAllocationService {

    @Override
    public void createAllocation(UUID tenantId, CreateAllocationRequest request) {
        throw new UnsupportedOperationException("TODO");
    }
}