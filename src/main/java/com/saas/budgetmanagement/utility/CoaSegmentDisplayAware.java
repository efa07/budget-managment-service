package com.saas.budgetmanagement.utility;

import java.util.UUID;

public interface CoaSegmentDisplayAware {

    UUID getAccountId();
    void setAccountName(String name);

    UUID getCostCenterId();
    void setCostCenterName(String name);

    UUID getDepartmentId();
    void setDepartmentName(String name);

    UUID getFundSourceId();
    void setFundSourceName(String name);

    UUID getProjectId();
    void setProjectName(String name);
}