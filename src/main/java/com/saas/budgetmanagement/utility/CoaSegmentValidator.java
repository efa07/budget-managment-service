package com.saas.budgetmanagement.utility;

import com.saas.budgetmanagement.dto.common.CoaSegmentReference;
import com.saas.budgetmanagement.exception.InvalidCoaSegmentException;
import com.saas.budgetmanagement.repository.refcache.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CoaSegmentValidator {

    private final RefCoaAccountRepository accountRepository;
    private final RefCostCenterRepository costCenterRepository;
    private final RefDepartmentRepository departmentRepository;
    private final RefFundSourceRepository fundSourceRepository;
    private final RefProjectRepository projectRepository;

    public void validate(UUID tenantId, CoaSegmentReference scope) {
        if (scope.getAccountId() == null) {
            throw new InvalidCoaSegmentException("accountId is required");
        }

        accountRepository.findByTenantIdAndExternalIdAndActiveTrue(tenantId, scope.getAccountId())
                .orElseThrow(() -> new InvalidCoaSegmentException(
                        "Account not found or inactive: " + scope.getAccountId()));

        if (scope.getCostCenterId() != null) {
            costCenterRepository.findByTenantIdAndExternalIdAndActiveTrue(tenantId, scope.getCostCenterId())
                    .orElseThrow(() -> new InvalidCoaSegmentException(
                            "Cost center not found or inactive: " + scope.getCostCenterId()));
        }

        if (scope.getDepartmentId() != null) {
            departmentRepository.findByTenantIdAndExternalIdAndActiveTrue(tenantId, scope.getDepartmentId())
                    .orElseThrow(() -> new InvalidCoaSegmentException(
                            "Department not found or inactive: " + scope.getDepartmentId()));
        }

        if (scope.getFundSourceId() != null) {
            fundSourceRepository.findByTenantIdAndExternalIdAndActiveTrue(tenantId, scope.getFundSourceId())
                    .orElseThrow(() -> new InvalidCoaSegmentException(
                            "Fund source not found or inactive: " + scope.getFundSourceId()));
        }

        if (scope.getProjectId() != null) {
            projectRepository.findByTenantIdAndExternalIdAndActiveTrue(tenantId, scope.getProjectId())
                    .orElseThrow(() -> new InvalidCoaSegmentException(
                            "Project not found or inactive: " + scope.getProjectId()));
        }
    }
}