package com.saas.budgetmanagement.service;

import com.saas.budgetmanagement.dto.common.CoaSegmentReference;
import com.saas.budgetmanagement.dto.responseDto.BudgetLedgerResponse;

import java.math.BigDecimal;
import java.util.UUID;

public interface BudgetLedgerService {

    void applyAllocationDelta(UUID tenantId, UUID fiscalYearId, UUID periodId,
                               CoaSegmentReference scope, BigDecimal amountDelta);

    void recalculateCommitted(UUID tenantId, UUID fiscalYearId, UUID periodId, CoaSegmentReference scope);

    void applyActualAmount(UUID tenantId, UUID fiscalYearId, UUID periodId,
                            CoaSegmentReference scope, BigDecimal actualAmount);

    BudgetLedgerResponse getLedger(UUID tenantId, UUID fiscalYearId, UUID periodId, CoaSegmentReference scope);
}