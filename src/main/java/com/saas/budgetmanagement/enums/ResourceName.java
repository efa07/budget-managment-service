package com.saas.budgetmanagement.enums;

import lombok.Getter;

@Getter
public enum ResourceName {

    // Resource Permissions
    GET_ALL_RESOURCES("Get All Resources"),
    GET_RESOURCES_BY_ROLE_NAME("Get Resources by Role"),
    GET_RESOURCE_BY_ID("Get Resource Details"),
    GET_RESOURCE_BY_NAME("Get Resource by Name"),
    GRANT_RESOURCE_ACCESS_TO_ROLE("Grant Resource Access to Role"),
    REVOKE_RESOURCE_ACCESS_FROM_ROLE("Revoke Resource Access from Role"),

    // define-resource-work-week-controller
    ADD_RESOURCE_WORK_WEEK("Add Resource Work Week"),
    GET_RESOURCE_WORK_WEEK_BY_ID("Get Resource Work Week by ID"),
    GET_ALL_RESOURCE_WORK_WEEKS("Get All Resource Work Weeks"),
    UPDATE_RESOURCE_WORK_WEEK("Update Resource Work Week"),
    DELETE_RESOURCE_WORK_WEEK("Delete Resource Work Week"),
    APPROVE_RESOURCE_WORK_WEEK("Approve Resource Work Week"),
    SEARCH_RESOURCE_WORK_WEEKS("Search Resource Work Weeks"),


    // Project resource
    ADD_PROJECT_RESOURCE("Add Project Resource"),
    GET_PROJECT_RESOURCE_BY_ID("Get Project Resource by ID"),
    GET_ALL_PROJECT_RESOURCES("Get All Project Resources"),
    UPDATE_PROJECT_RESOURCE("Update Project Resource"),
    DELETE_PROJECT_RESOURCE("Delete Project Resource"),
    GET_PROJECT_RESOURCE_BY_PROJECT_ID("Get Project Resource by ProjectId"),


    // Base Calendar
    ADD_BASE_CALENDAR("Add Base Calendar"),
    GET_BASE_CALENDAR_BY_ID("Get Base Calendar by ID"),
    GET_ALL_BASE_CALENDARS("Get All Base Calendars"),
    UPDATE_BASE_CALENDAR("Update Base Calendar"),
    DELETE_BASE_CALENDAR("Delete Base Calendar"),


    //Assign Resource to Activity
    ADD_ASSIGN_RESOURCE_TO_ACTIVITY("Add Assign Resource to Activity"),
    GET_ASSIGN_RESOURCE_TO_ACTIVITY_BY_ID("Get Assign Resource to Activity by ID"),
    GET_ALL_ASSIGN_RESOURCES_TO_ACTIVITY("Get All Assign Resources to Activity"),
    UPDATE_ASSIGN_RESOURCE_TO_ACTIVITY("Update Assign Resource to Activity"),
    DELETE_ASSIGN_RESOURCE_TO_ACTIVITY("Delete Assign Resource to Activity"),



    //Project Activity Sequence

    ADD_PROJECT_ACTIVITY_SEQUENCE("Add Project Activity Sequence"),
    GET_PROJECT_ACTIVITY_SEQUENCE_BY_ID("Get Project Activity Sequence by ID"),
    GET_ALL_PROJECT_ACTIVITY_SEQUENCES("Get All Project Activity Sequences"),
    UPDATE_PROJECT_ACTIVITY_SEQUENCE("Update Project Activity Sequence"),
    DELETE_PROJECT_ACTIVITY_SEQUENCE("Delete Project Activity Sequence"),




    // project Calendar
    ADD_PROJECT_CALENDAR("Add Project Calendar"),
    GET_PROJECT_CALENDAR_BY_ID("Get Project Calendar by ID"),
    GET_ALL_PROJECT_CALENDARS("Get All Project Calendars"),
    UPDATE_PROJECT_CALENDAR("Update Project Calendar"),
    DELETE_PROJECT_CALENDAR("Delete Project Calendar"),



    // Add payment schedule
    ADD_PAYMENT_SCHEDULE("Add Payment Schedule"),
    GET_PAYMENT_SCHEDULE_BY_ID("Get Payment Schedule by ID"),
    GET_ALL_PAYMENT_SCHEDULES("Get All Payment Schedules"),
    UPDATE_PAYMENT_SCHEDULE("Update Payment Schedule"),
    DELETE_PAYMENT_SCHEDULE("Delete Payment Schedule"),

    GET_CRITICAL_PATH_BY_PROJECT_ID("Get Critical Path by ProjectId");

    private final String value;

    private ResourceName(String value) {
        this.value = value;
    }
}
