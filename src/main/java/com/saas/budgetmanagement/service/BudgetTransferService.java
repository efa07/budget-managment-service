package com.saas.budgetmanagement.service;

import com.saas.budgetmanagement.dto.requestDto.ApprovalDecisionRequest;
import com.saas.budgetmanagement.dto.requestDto.CreateTransferRequest;
import com.saas.budgetmanagement.dto.responseDto.BudgetTransferResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BudgetTransferService {

    BudgetTransferResponse requestTransfer(CreateTransferRequest request);

    BudgetTransferResponse approve(UUID id, ApprovalDecisionRequest request);

    BudgetTransferResponse reject(UUID id, ApprovalDecisionRequest request);

    Page<BudgetTransferResponse> list(String status, Pageable pageable);
}