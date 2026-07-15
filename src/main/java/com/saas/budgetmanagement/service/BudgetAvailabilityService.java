package com.saas.budgetmanagement.service;

import com.saas.budgetmanagement.dto.requestDto.BudgetAvailabilityCheckRequest;
import java.util.UUID;

public interface BudgetAvailabilityService {
    void checkAvailability(UUID tenantId, BudgetAvailabilityCheckRequest request);
}