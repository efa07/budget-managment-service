package com.saas.budgetmanagement.service.impl;

import com.saas.budgetmanagement.dto.requestDto.ApprovalDecisionRequest;
import com.saas.budgetmanagement.service.BudgetApprovalService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BudgetApprovalServiceImpl implements BudgetApprovalService {

    @Override
    public void decideApproval(UUID tenantId, ApprovalDecisionRequest request) {
        throw new UnsupportedOperationException("TODO");
    }
}