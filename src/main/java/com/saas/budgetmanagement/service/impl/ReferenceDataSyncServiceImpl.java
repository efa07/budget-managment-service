package com.saas.budgetmanagement.service.impl;

import com.saas.budgetmanagement.data.ReferenceDataSyncData;
import com.saas.budgetmanagement.service.ReferenceDataSyncService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReferenceDataSyncServiceImpl implements ReferenceDataSyncService {

    private final ReferenceDataSyncData referenceDataSyncData;

    public ReferenceDataSyncServiceImpl(ReferenceDataSyncData referenceDataSyncData) {
        this.referenceDataSyncData = referenceDataSyncData;
    }

    @Override
    public void syncReferenceData(UUID tenantId) {
        referenceDataSyncData.sync(tenantId);
    }
}