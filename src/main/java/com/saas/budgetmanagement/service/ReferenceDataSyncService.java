package com.saas.budgetmanagement.service;

import com.saas.budgetmanagement.dto.eventDto.inbound.AccountingPeriodSyncEvent;
import com.saas.budgetmanagement.dto.eventDto.inbound.CoaSyncEvent;
import com.saas.budgetmanagement.dto.eventDto.inbound.FiscalYearSyncEvent;

public interface ReferenceDataSyncService {

    void syncFiscalYear(FiscalYearSyncEvent event);

    void syncAccountingPeriod(AccountingPeriodSyncEvent event);

    void syncCoaSegment(CoaSyncEvent event);

    void reconcileAll();
}