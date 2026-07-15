package com.saas.budgetmanagement.service;

import com.saas.budgetmanagement.dto.requestDto.ApprovalDecisionRequest;
import java.util.UUID;

public interface BudgetApprovalService {
    void decideApproval(UUID tenantId, ApprovalDecisionRequest request);
}