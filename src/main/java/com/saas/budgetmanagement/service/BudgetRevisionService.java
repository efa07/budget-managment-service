package com.saas.budgetmanagement.service;

import com.saas.budgetmanagement.dto.requestDto.CreateRevisionRequest;
import java.util.UUID;

public interface BudgetRevisionService {
    void createRevision(UUID tenantId, CreateRevisionRequest request);
}