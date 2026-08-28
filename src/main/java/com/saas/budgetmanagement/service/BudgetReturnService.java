package com.saas.budgetmanagement.service;

import com.saas.budgetmanagement.dto.requestDto.ApprovalDecisionRequest;
import com.saas.budgetmanagement.dto.requestDto.CreateBudgetReturnRequest;
import com.saas.budgetmanagement.dto.responseDto.BudgetReturnResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BudgetReturnService {

    BudgetReturnResponse requestReturn(CreateBudgetReturnRequest request);

    BudgetReturnResponse getById(UUID id);

    BudgetReturnResponse approve(UUID id, ApprovalDecisionRequest request);

    BudgetReturnResponse reject(UUID id, ApprovalDecisionRequest request);

    Page<BudgetReturnResponse> list(String status, Pageable pageable);
}