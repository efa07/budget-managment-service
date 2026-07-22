package com.saas.budgetmanagement.service.impl;

import com.saas.budgetmanagement.model.BudgetPlan;
import com.saas.budgetmanagement.service.BudgetApprovalService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BudgetApprovalServiceImpl implements BudgetApprovalService {

    @Override
    public void assertCanApprove(UUID tenantId, UUID approverId, BudgetPlan plan) {
        // TODO: replace with real rule evaluation once RefApprovalWorkflowConfig exists —
        // e.g. check approverId's role/permissions via Keycloak claims (RoleConverter/PermissionEvaluator,
        // per the org template's utility/ package) against the plan's requestedAmount threshold,
        // and reject if the approver is the same person as requestedBy (separation-of-duties).
        //
        // For now this is a placeholder that allows any authenticated caller through, matching
        // current behavior — but it gives BudgetPlanServiceImpl one call site to depend on instead
        // of hardcoding "no check" directly in the approve() method.
    }
}