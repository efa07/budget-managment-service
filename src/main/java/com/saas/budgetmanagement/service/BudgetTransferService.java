package com.saas.budgetmanagement.service;

import com.saas.budgetmanagement.dto.requestDto.CreateTransferRequest;
import java.util.UUID;

public interface BudgetTransferService {
    void createTransfer(UUID tenantId, CreateTransferRequest request);
}