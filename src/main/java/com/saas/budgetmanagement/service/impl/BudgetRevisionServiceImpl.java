package com.saas.budgetmanagement.service.impl;

import com.saas.budgetmanagement.dto.requestDto.CreateRevisionRequest;
import com.saas.budgetmanagement.service.BudgetRevisionService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BudgetRevisionServiceImpl implements BudgetRevisionService {

    @Override
    public void createRevision(UUID tenantId, CreateRevisionRequest request) {
        throw new UnsupportedOperationException("TODO");
    }
}