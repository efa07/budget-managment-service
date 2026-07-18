package com.saas.budgetmanagement.service.impl;

import com.saas.budgetmanagement.client.FinanceAdministrationClient;
import com.saas.budgetmanagement.dto.eventDto.inbound.AccountingPeriodSyncEvent;
import com.saas.budgetmanagement.dto.eventDto.inbound.CoaSyncEvent;
import com.saas.budgetmanagement.dto.eventDto.inbound.FiscalYearSyncEvent;
import com.saas.budgetmanagement.dto.clientDto.FiscalYearClientDto;
import com.saas.budgetmanagement.model.refcache.*;
import com.saas.budgetmanagement.repository.refcache.*;
import com.saas.budgetmanagement.service.ReferenceDataSyncService;
import com.saas.budgetmanagement.utility.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
public class ReferenceDataSyncServiceImpl implements ReferenceDataSyncService {

    private final RefFiscalYearRepository fiscalYearRepository;
    private final RefAccountingPeriodRepository accountingPeriodRepository;
    private final RefCoaAccountRepository coaAccountRepository;
    private final RefCostCenterRepository costCenterRepository;
    private final RefDepartmentRepository departmentRepository;
    private final RefFundSourceRepository fundSourceRepository;
    private final RefProjectRepository projectRepository;
    private final FinanceAdministrationClient financeAdministrationClient;
    private final SecurityUtil securityUtil;

    @Override
    @Transactional
    public void syncFiscalYear(FiscalYearSyncEvent event) {
        UUID tenantId = UUID.fromString(securityUtil.getTenantId());

        RefFiscalYear entity = fiscalYearRepository
                .findByTenantIdAndExternalId(tenantId, event.getExternalId())
                .orElseGet(RefFiscalYear::new);

        entity.setTenantId(tenantId);
        entity.setExternalId(event.getExternalId());
        entity.setName(event.getName());
        entity.setStartDate(event.getStartDate());
        entity.setEndDate(event.getEndDate());
        entity.setOpen(event.isOpen());
        entity.setSyncedAt(LocalDateTime.now());

        fiscalYearRepository.save(entity);
    }

    @Override
    @Transactional
    public void syncAccountingPeriod(AccountingPeriodSyncEvent event) {
        UUID tenantId = UUID.fromString(securityUtil.getTenantId());

        RefAccountingPeriod entity = accountingPeriodRepository
                .findByTenantIdAndExternalId(tenantId, event.getExternalId())
                .orElseGet(RefAccountingPeriod::new);

        entity.setTenantId(tenantId);
        entity.setExternalId(event.getExternalId());
        entity.setFiscalYearExternalId(event.getFiscalYearExternalId());
        entity.setPeriodNumber(event.getPeriodNumber());
        entity.setPeriodName(event.getPeriodName());
        entity.setStartDate(event.getStartDate());
        entity.setEndDate(event.getEndDate());
        entity.setOpen(event.isOpen());
        entity.setSyncedAt(LocalDateTime.now());

        accountingPeriodRepository.save(entity);
    }

    @Override
    @Transactional
    public void syncCoaSegment(CoaSyncEvent event) {
        UUID tenantId = UUID.fromString(securityUtil.getTenantId());

        switch (event.getSegmentType()) {
            case ACCOUNT -> {
                RefCoaAccount entity = coaAccountRepository
                        .findByTenantIdAndExternalId(tenantId, event.getExternalId())
                        .orElseGet(RefCoaAccount::new);
                entity.setTenantId(tenantId);
                entity.setExternalId(event.getExternalId());
                entity.setAccountCode(event.getCode());
                entity.setAccountName(event.getName());
                entity.setAccountType(event.getAccountType());
                entity.setActive(event.isActive());
                entity.setSyncedAt(LocalDateTime.now());
                coaAccountRepository.save(entity);
            }
            case COST_CENTER -> {
                RefCostCenter entity = costCenterRepository
                        .findByTenantIdAndExternalId(tenantId, event.getExternalId())
                        .orElseGet(RefCostCenter::new);
                entity.setTenantId(tenantId);
                entity.setExternalId(event.getExternalId());
                entity.setCode(event.getCode());
                entity.setName(event.getName());
                entity.setActive(event.isActive());
                entity.setSyncedAt(LocalDateTime.now());
                costCenterRepository.save(entity);
            }
            case DEPARTMENT -> {
                RefDepartment entity = departmentRepository
                        .findByTenantIdAndExternalId(tenantId, event.getExternalId())
                        .orElseGet(RefDepartment::new);
                entity.setTenantId(tenantId);
                entity.setExternalId(event.getExternalId());
                entity.setCode(event.getCode());
                entity.setName(event.getName());
                entity.setActive(event.isActive());
                entity.setSyncedAt(LocalDateTime.now());
                departmentRepository.save(entity);
            }
            case FUND_SOURCE -> {
                RefFundSource entity = fundSourceRepository
                        .findByTenantIdAndExternalId(tenantId, event.getExternalId())
                        .orElseGet(RefFundSource::new);
                entity.setTenantId(tenantId);
                entity.setExternalId(event.getExternalId());
                entity.setCode(event.getCode());
                entity.setName(event.getName());
                entity.setActive(event.isActive());
                entity.setSyncedAt(LocalDateTime.now());
                fundSourceRepository.save(entity);
            }
            case PROJECT -> {
                RefProject entity = projectRepository
                        .findByTenantIdAndExternalId(tenantId, event.getExternalId())
                        .orElseGet(RefProject::new);
                entity.setTenantId(tenantId);
                entity.setExternalId(event.getExternalId());
                entity.setCode(event.getCode());
                entity.setName(event.getName());
                entity.setActive(event.isActive());
                entity.setSyncedAt(LocalDateTime.now());
                projectRepository.save(entity);
            }
            default -> log.warn("Unhandled COA segment type: {}", event.getSegmentType());
        }
    }

@Override
public void reconcileAll() {
    UUID tenantId = UUID.fromString(securityUtil.getTenantId());
    log.info("Starting manual reference-data reconciliation for tenant {}", tenantId);

    reconcileFiscalYears(tenantId);
    reconcileAccountingPeriods(tenantId);
    reconcileCoaSegments(tenantId);

    log.info("Completed manual reference-data reconciliation for tenant {}", tenantId);
}

private void reconcileFiscalYears(UUID tenantId) {
    List<FiscalYearClientDto> fiscalYears = financeAdministrationClient.getAllFiscalYears(tenantId);
    for (FiscalYearClientDto dto : fiscalYears) {
        FiscalYearSyncEvent event = new FiscalYearSyncEvent();
        event.setExternalId(dto.getExternalId());
        event.setName(dto.getName());
        event.setStartDate(dto.getStartDate());
        event.setEndDate(dto.getEndDate());
        event.setOpen(dto.isOpen());
        syncFiscalYear(event);
    }
    log.info("Reconciled {} fiscal year(s) for tenant {}", fiscalYears.size(), tenantId);
}

private void reconcileAccountingPeriods(UUID tenantId) {
    List<AccountingPeriodClientDto> periods = financeAdministrationClient.getAllAccountingPeriods(tenantId);
    for (AccountingPeriodClientDto dto : periods) {
        AccountingPeriodSyncEvent event = new AccountingPeriodSyncEvent();
        event.setExternalId(dto.getExternalId());
        event.setFiscalYearExternalId(dto.getFiscalYearExternalId());
        event.setPeriodNumber(dto.getPeriodNumber());
        event.setPeriodName(dto.getPeriodName());
        event.setStartDate(dto.getStartDate());
        event.setEndDate(dto.getEndDate());
        event.setOpen(dto.isOpen());
        syncAccountingPeriod(event);
    }
    log.info("Reconciled {} accounting period(s) for tenant {}", periods.size(), tenantId);
}

private void reconcileCoaSegments(UUID tenantId) {
    reconcileSegmentType(tenantId, CoaSyncEvent.CoaSegmentType.ACCOUNT,
            financeAdministrationClient.getAllAccounts(tenantId));
    reconcileSegmentType(tenantId, CoaSyncEvent.CoaSegmentType.COST_CENTER,
            financeAdministrationClient.getAllCostCenters(tenantId));
    reconcileSegmentType(tenantId, CoaSyncEvent.CoaSegmentType.DEPARTMENT,
            financeAdministrationClient.getAllDepartments(tenantId));
    reconcileSegmentType(tenantId, CoaSyncEvent.CoaSegmentType.FUND_SOURCE,
            financeAdministrationClient.getAllFundSources(tenantId));
    reconcileSegmentType(tenantId, CoaSyncEvent.CoaSegmentType.PROJECT,
            financeAdministrationClient.getAllProjects(tenantId));
}

private void reconcileSegmentType(UUID tenantId, CoaSyncEvent.CoaSegmentType segmentType,
                                   List<CoaSegmentClientDto> segments) {
    for (CoaSegmentClientDto dto : segments) {
        CoaSyncEvent event = new CoaSyncEvent();
        event.setSegmentType(segmentType);
        event.setExternalId(dto.getExternalId());
        event.setCode(dto.getCode());
        event.setName(dto.getName());
        event.setAccountType(dto.getAccountType());
        event.setActive(dto.isActive());
        syncCoaSegment(event);
    }
    log.info("Reconciled {} {} record(s) for tenant {}", segments.size(), segmentType, tenantId);
}