package com.saas.budgetmanagement.service;

import com.saas.budgetmanagement.dto.requestDto.CreateBudgetPlanRequest;
import com.saas.budgetmanagement.dto.requestDto.UpdateBudgetPlanRequest;
import com.saas.budgetmanagement.dto.requestDto.ApprovalDecisionRequest;
import com.saas.budgetmanagement.dto.responseDto.BudgetPlanResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BudgetPlanService {

    BudgetPlanResponse create(CreateBudgetPlanRequest request);

    BudgetPlanResponse update(UUID id, UpdateBudgetPlanRequest request);

    BudgetPlanResponse getById(UUID id);

    Page<BudgetPlanResponse> list(UUID fiscalYearId, String status, Pageable pageable);

    BudgetPlanResponse submit(UUID id);

    BudgetPlanResponse approve(UUID id, ApprovalDecisionRequest request);

    BudgetPlanResponse reject(UUID id, ApprovalDecisionRequest request);
}