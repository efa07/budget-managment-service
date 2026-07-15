package com.saas.budgetmanagement.service;

import java.util.UUID;

public interface CommitmentService {
    void registerCommitment(UUID tenantId);
}