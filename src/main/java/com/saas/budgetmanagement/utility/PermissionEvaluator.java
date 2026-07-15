package com.saas.budgetmanagement.utility;

import com.saas.budgetmanagement.enums.ResourceName;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PermissionEvaluator {

    private final PermissionUtil permissionUtil;

    // ==================================================
    // Resource Permissions
    // ==================================================
    public void getAllResourcesPermission(UUID tenantId) {
        boolean isAdmin = permissionUtil.isAdmin();
        if (!isAdmin) {
            checkPermission(tenantId, ResourceName.GET_ALL_RESOURCES);
        }
    }

    public void getResourcesByRoleNamePermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.GET_RESOURCES_BY_ROLE_NAME);
    }

    public void getResourceByIdPermission(UUID tenantId) {
        boolean isAdmin = permissionUtil.isAdmin();
        if (!isAdmin) {
            checkPermission(tenantId, ResourceName.GET_RESOURCE_BY_ID);
        }
    }

    public void getResourceByNamePermission(UUID tenantId) {
        boolean isAdmin = permissionUtil.isAdmin();
        if (!isAdmin) {
            checkPermission(tenantId, ResourceName.GET_RESOURCE_BY_NAME);
        }
    }

    public void grantResourceAccessToRolePermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.GRANT_RESOURCE_ACCESS_TO_ROLE);
    }

    public void revokeResourceAccessFromRolePermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.REVOKE_RESOURCE_ACCESS_FROM_ROLE);
    }

    // ==================================================
    // Define Resource Work Week
    // ==================================================
    public void addResourceWorkWeekPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.ADD_RESOURCE_WORK_WEEK);
    }

    public void getResourceWorkWeekByIdPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.GET_RESOURCE_WORK_WEEK_BY_ID);
    }

    public void getAllResourceWorkWeeksPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.GET_ALL_RESOURCE_WORK_WEEKS);
    }

    public void updateResourceWorkWeekPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.UPDATE_RESOURCE_WORK_WEEK);
    }

    public void deleteResourceWorkWeekPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.DELETE_RESOURCE_WORK_WEEK);
    }

    public void approveResourceWorkWeekPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.APPROVE_RESOURCE_WORK_WEEK);
    }

    public void searchResourceWorkWeeksPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.SEARCH_RESOURCE_WORK_WEEKS);
    }

    // ==================================================
    // Project Resource
    // ==================================================
    public void addProjectResourcePermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.ADD_PROJECT_RESOURCE);
    }

    public void getProjectResourceByIdPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.GET_PROJECT_RESOURCE_BY_ID);
    }

    public void getAllProjectResourcesPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.GET_ALL_PROJECT_RESOURCES);
    }

    public void updateProjectResourcePermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.UPDATE_PROJECT_RESOURCE);
    }

    public void deleteProjectResourcePermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.DELETE_PROJECT_RESOURCE);
    }

    public void getProjectResourceByProjectIdPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.GET_PROJECT_RESOURCE_BY_PROJECT_ID);
    }

    // ==================================================
    // Base Calendar
    // ==================================================
    public void addBaseCalendarPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.ADD_BASE_CALENDAR);
    }

    public void getBaseCalendarByIdPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.GET_BASE_CALENDAR_BY_ID);
    }

    public void getAllBaseCalendarsPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.GET_ALL_BASE_CALENDARS);
    }

    public void updateBaseCalendarPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.UPDATE_BASE_CALENDAR);
    }

    public void deleteBaseCalendarPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.DELETE_BASE_CALENDAR);
    }

    // ==================================================
    // Assign Resource to Activity
    // ==================================================
    public void addAssignResourceToActivityPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.ADD_ASSIGN_RESOURCE_TO_ACTIVITY);
    }

    public void getAssignResourceToActivityByIdPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.GET_ASSIGN_RESOURCE_TO_ACTIVITY_BY_ID);
    }

    public void getAllAssignResourcesToActivityPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.GET_ALL_ASSIGN_RESOURCES_TO_ACTIVITY);
    }

    public void updateAssignResourceToActivityPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.UPDATE_ASSIGN_RESOURCE_TO_ACTIVITY);
    }

    public void deleteAssignResourceToActivityPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.DELETE_ASSIGN_RESOURCE_TO_ACTIVITY);
    }

    // ==================================================
    // Project Activity Sequence
    // ==================================================
    public void addProjectActivitySequencePermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.ADD_PROJECT_ACTIVITY_SEQUENCE);
    }

    public void getProjectActivitySequenceByIdPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.GET_PROJECT_ACTIVITY_SEQUENCE_BY_ID);
    }

    public void getAllProjectActivitySequencesPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.GET_ALL_PROJECT_ACTIVITY_SEQUENCES);
    }

    public void updateProjectActivitySequencePermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.UPDATE_PROJECT_ACTIVITY_SEQUENCE);
    }

    public void deleteProjectActivitySequencePermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.DELETE_PROJECT_ACTIVITY_SEQUENCE);
    }

    // ==================================================
    // Project Calendar
    // ==================================================
    public void addProjectCalendarPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.ADD_PROJECT_CALENDAR);
    }

    public void getProjectCalendarByIdPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.GET_PROJECT_CALENDAR_BY_ID);
    }

    public void getAllProjectCalendarsPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.GET_ALL_PROJECT_CALENDARS);
    }

    public void updateProjectCalendarPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.UPDATE_PROJECT_CALENDAR);
    }

    public void deleteProjectCalendarPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.DELETE_PROJECT_CALENDAR);
    }

    // ==================================================
    // Payment Schedule
    // ==================================================
    public void addPaymentSchedulePermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.ADD_PAYMENT_SCHEDULE);
    }

    public void getPaymentScheduleByIdPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.GET_PAYMENT_SCHEDULE_BY_ID);
    }

    public void getAllPaymentSchedulesPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.GET_ALL_PAYMENT_SCHEDULES);
    }

    public void updatePaymentSchedulePermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.UPDATE_PAYMENT_SCHEDULE);
    }

    public void deletePaymentSchedulePermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.DELETE_PAYMENT_SCHEDULE);
    }

    // ==================================================
    // Critical Path
    // ==================================================
    public void getCriticalPathByProjectIdPermission(UUID tenantId) {
        checkPermission(tenantId, ResourceName.GET_CRITICAL_PATH_BY_PROJECT_ID);
    }

    // ==================================================
    // Common Checker
    // ==================================================
    private void checkPermission(UUID tenantId, ResourceName resourceName) {
        boolean hasPermission = permissionUtil.hasPermission(tenantId, resourceName.getValue());
        if (!hasPermission) {
            throw new AccessDeniedException("Access Denied");
        }
    }
}
