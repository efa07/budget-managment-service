package com.saas.budgetmanagement.service;

import java.util.UUID;

public interface ReferenceDataSyncService {
    void syncReferenceData(UUID tenantId);
}