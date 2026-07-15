package com.saas.budgetmanagement.service.impl;

import com.saas.budgetmanagement.service.CommitmentService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CommitmentServiceImpl implements CommitmentService {

    @Override
    public void registerCommitment(UUID tenantId) {
        throw new UnsupportedOperationException("TODO");
    }
}