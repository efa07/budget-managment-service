package com.saas.budgetmanagement.service;

import com.saas.budgetmanagement.dto.eventDto.inbound.*;


import java.util.UUID;

public interface ReferenceDataSyncService {

    void syncFiscalYear(UUID tenantId, FiscalYearSyncEvent event);

    void syncAccountingPeriod(UUID tenantId, AccountingPeriodSyncEvent event);

    void syncCoaSegment(UUID tenantId, CoaSyncEvent event);

    void reconcileAll(UUID tenantId);
}