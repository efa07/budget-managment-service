package com.saas.budgetmanagement.utility;

import com.saas.budgetmanagement.model.refcache.*;
import com.saas.budgetmanagement.repository.refcache.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CoaSegmentDisplayResolver {

    private final RefCoaAccountRepository accountRepository;
    private final RefCostCenterRepository costCenterRepository;
    private final RefDepartmentRepository departmentRepository;
    private final RefFundSourceRepository fundSourceRepository;
    private final RefProjectRepository projectRepository;
    private final RefFiscalYearRepository fiscalYearRepository;


    public Map<UUID, String> resolveAccountNames(UUID tenantId, Set<UUID> ids) {
        return accountRepository.findByTenantIdAndExternalIdIn(tenantId, new ArrayList<>(ids)).stream()
                .collect(Collectors.toMap(RefCoaAccount::getExternalId, RefCoaAccount::getAccountName));
    }

    public Map<UUID, String> resolveCostCenterNames(UUID tenantId, Set<UUID> ids) {
        return costCenterRepository.findByTenantIdAndExternalIdIn(tenantId, new ArrayList<>(ids)).stream()
                .collect(Collectors.toMap(RefCostCenter::getExternalId, RefCostCenter::getName));
    }

    public Map<UUID, String> resolveDepartmentNames(UUID tenantId, Set<UUID> ids) {
        return departmentRepository.findByTenantIdAndExternalIdIn(tenantId, new ArrayList<>(ids)).stream()
                .collect(Collectors.toMap(RefDepartment::getExternalId, RefDepartment::getName));
    }

    public Map<UUID, String> resolveFundSourceNames(UUID tenantId, Set<UUID> ids) {
        return fundSourceRepository.findByTenantIdAndExternalIdIn(tenantId, new ArrayList<>(ids)).stream()
                .collect(Collectors.toMap(RefFundSource::getExternalId, RefFundSource::getName));
    }

    public Map<UUID, String> resolveProjectNames(UUID tenantId, Set<UUID> ids) {
        return projectRepository.findByTenantIdAndExternalIdIn(tenantId, new ArrayList<>(ids)).stream()
                .collect(Collectors.toMap(RefProject::getExternalId, RefProject::getName));
    }

    public <T extends CoaSegmentDisplayAware> void enrichCoaSegments(UUID tenantId, List<T> items) {
        if (items == null || items.isEmpty()) {
            return;
        }

        Set<UUID> accountIds = collectIds(items, CoaSegmentDisplayAware::getAccountId);
        Set<UUID> costCenterIds = collectIds(items, CoaSegmentDisplayAware::getCostCenterId);
        Set<UUID> departmentIds = collectIds(items, CoaSegmentDisplayAware::getDepartmentId);
        Set<UUID> fundSourceIds = collectIds(items, CoaSegmentDisplayAware::getFundSourceId);
        Set<UUID> projectIds = collectIds(items, CoaSegmentDisplayAware::getProjectId);

        Map<UUID, String> accountNames = resolveAccountNames(tenantId, accountIds);
        Map<UUID, String> costCenterNames = resolveCostCenterNames(tenantId, costCenterIds);
        Map<UUID, String> departmentNames = resolveDepartmentNames(tenantId, departmentIds);
        Map<UUID, String> fundSourceNames = resolveFundSourceNames(tenantId, fundSourceIds);
        Map<UUID, String> projectNames = resolveProjectNames(tenantId, projectIds);

        for (T item : items) {
            item.setAccountName(accountNames.get(item.getAccountId()));
            item.setCostCenterName(costCenterNames.get(item.getCostCenterId()));
            item.setDepartmentName(departmentNames.get(item.getDepartmentId()));
            item.setFundSourceName(fundSourceNames.get(item.getFundSourceId()));
            item.setProjectName(projectNames.get(item.getProjectId()));
        }
    }

    private <T extends CoaSegmentDisplayAware> Set<UUID> collectIds(List<T> items, java.util.function.Function<T, UUID> extractor) {
        return items.stream().map(extractor).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public Map<UUID, String> resolveFiscalYearNames(UUID tenantId, Set<UUID> ids) {
        return fiscalYearRepository.findByTenantIdAndExternalIdIn(tenantId, new ArrayList<>(ids)).stream()
                .collect(Collectors.toMap(RefFiscalYear::getExternalId, RefFiscalYear::getName));
    }


}