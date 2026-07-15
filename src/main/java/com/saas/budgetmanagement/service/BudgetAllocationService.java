package com.saas.budgetmanagement.service;

import com.saas.budgetmanagement.dto.requestDto.CreateAllocationRequest;
import java.util.UUID;

public interface BudgetAllocationService {
    void createAllocation(UUID tenantId, CreateAllocationRequest request);
}