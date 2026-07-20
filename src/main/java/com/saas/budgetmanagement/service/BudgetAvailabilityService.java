package com.saas.budgetmanagement.service;

import com.saas.budgetmanagement.dto.requestDto.BudgetAvailabilityCheckRequest;
import com.saas.budgetmanagement.dto.responseDto.BudgetAvailabilityCheckResponse;

public interface BudgetAvailabilityService {

    BudgetAvailabilityCheckResponse checkAvailability(BudgetAvailabilityCheckRequest request);
}