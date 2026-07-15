package com.saas.budgetmanagement.service.impl;

import com.saas.budgetmanagement.dto.requestDto.CreateTransferRequest;
import com.saas.budgetmanagement.service.BudgetTransferService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BudgetTransferServiceImpl implements BudgetTransferService {

    @Override
    public void createTransfer(UUID tenantId, CreateTransferRequest request) {
        throw new UnsupportedOperationException("TODO");
    }
}