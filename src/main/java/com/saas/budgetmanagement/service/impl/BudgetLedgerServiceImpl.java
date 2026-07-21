package com.saas.budgetmanagement.service.impl;

import com.saas.budgetmanagement.dto.common.CoaSegmentReference;
import com.saas.budgetmanagement.dto.responseDto.BudgetLedgerResponse;
import com.saas.budgetmanagement.mapper.BudgetLedgerMapper;
import com.saas.budgetmanagement.model.BudgetLedger;
import com.saas.budgetmanagement.repository.BudgetLedgerRepository;
import com.saas.budgetmanagement.repository.CommitmentRepository;
import com.saas.budgetmanagement.service.BudgetLedgerService;
import com.saas.budgetmanagement.utility.CoaSegmentDisplayResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BudgetLedgerServiceImpl implements BudgetLedgerService {

    private final BudgetLedgerRepository budgetLedgerRepository;
    private final CommitmentRepository commitmentRepository;
    private final BudgetLedgerMapper budgetLedgerMapper;
    private final CoaSegmentDisplayResolver displayResolver;

    @Override
    @Transactional
    public void applyAllocationDelta(UUID tenantId, UUID fiscalYearId, UUID periodId,
                                      CoaSegmentReference scope, BigDecimal amountDelta) {
        BudgetLedger ledger = findOrCreate(tenantId, fiscalYearId, periodId, scope);
        ledger.setAllocatedAmount(ledger.getAllocatedAmount().add(amountDelta));
        recalculateAvailable(ledger);
        budgetLedgerRepository.save(ledger);
    }

    @Override
    @Transactional
    public void recalculateCommitted(UUID tenantId, UUID fiscalYearId, UUID periodId, CoaSegmentReference scope) {
        BudgetLedger ledger = findOrCreate(tenantId, fiscalYearId, periodId, scope);

        BigDecimal committed = commitmentRepository.sumCommittedByCoaScope(
                tenantId, scope.getAccountId(), scope.getCostCenterId(),
                scope.getDepartmentId(), scope.getFundSourceId(), scope.getProjectId());

        ledger.setCommittedAmount(committed);
        recalculateAvailable(ledger);
        budgetLedgerRepository.save(ledger);
    }

    @Override
    @Transactional
    public void applyActualAmount(UUID tenantId, UUID fiscalYearId, UUID periodId,
                                   CoaSegmentReference scope, BigDecimal actualAmount) {
        BudgetLedger ledger = findOrCreate(tenantId, fiscalYearId, periodId, scope);
        ledger.setActualAmount(actualAmount);
        recalculateAvailable(ledger);
        budgetLedgerRepository.save(ledger);
    }

    @Override
    public BudgetLedgerResponse getLedger(UUID tenantId, UUID fiscalYearId, UUID periodId, CoaSegmentReference scope) {
        BudgetLedger ledger = budgetLedgerRepository
                .findByTenantIdAndFiscalYearIdAndPeriodIdAndAccountIdAndCostCenterIdAndDepartmentIdAndFundSourceIdAndProjectId(
                        tenantId, fiscalYearId, periodId, scope.getAccountId(), scope.getCostCenterId(),
                        scope.getDepartmentId(), scope.getFundSourceId(), scope.getProjectId())
                .orElseGet(() -> zeroLedgerFor(tenantId, fiscalYearId, periodId, scope));

        BudgetLedgerResponse response = budgetLedgerMapper.toResponse(ledger);
        displayResolver.enrichCoaSegments(tenantId, List.of(response));
        return response;
    }

    private BudgetLedger findOrCreate(UUID tenantId, UUID fiscalYearId, UUID periodId, CoaSegmentReference scope) {
        return budgetLedgerRepository.findForUpdateByScope(
                        tenantId, fiscalYearId, periodId, scope.getAccountId(), scope.getCostCenterId(),
                        scope.getDepartmentId(), scope.getFundSourceId(), scope.getProjectId())
                .orElseGet(() -> {
                    BudgetLedger newLedger = zeroLedgerFor(tenantId, fiscalYearId, periodId, scope);
                    return budgetLedgerRepository.save(newLedger);
                });
    }

    private BudgetLedger zeroLedgerFor(UUID tenantId, UUID fiscalYearId, UUID periodId, CoaSegmentReference scope) {
        BudgetLedger ledger = new BudgetLedger();
        ledger.setTenantId(tenantId);
        ledger.setFiscalYearId(fiscalYearId);
        ledger.setPeriodId(periodId);
        ledger.setAccountId(scope.getAccountId());
        ledger.setCostCenterId(scope.getCostCenterId());
        ledger.setDepartmentId(scope.getDepartmentId());
        ledger.setFundSourceId(scope.getFundSourceId());
        ledger.setProjectId(scope.getProjectId());
        ledger.setAllocatedAmount(BigDecimal.ZERO);
        ledger.setCommittedAmount(BigDecimal.ZERO);
        ledger.setActualAmount(BigDecimal.ZERO);
        ledger.setAvailableAmount(BigDecimal.ZERO);
        ledger.setLastUpdatedAt(LocalDateTime.now());
        return ledger;
    }

    private void recalculateAvailable(BudgetLedger ledger) {
        BigDecimal available = ledger.getAllocatedAmount()
                .subtract(ledger.getCommittedAmount())
                .subtract(ledger.getActualAmount());
        ledger.setAvailableAmount(available);
        ledger.setLastUpdatedAt(LocalDateTime.now());
    }
}