package com.saas.budgetmanagement.service;

import com.saas.budgetmanagement.dto.requestDto.CreateAllocationRequest;
import com.saas.budgetmanagement.dto.responseDto.BudgetAllocationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BudgetAllocationService {

    BudgetAllocationResponse create(CreateAllocationRequest request);

    BudgetAllocationResponse getById(UUID id);

    Page<BudgetAllocationResponse> list(UUID budgetPlanId, Pageable pageable);
}