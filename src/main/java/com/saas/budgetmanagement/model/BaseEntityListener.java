package com.saas.budgetmanagement.model;

import com.saas.budgetmanagement.utility.SpringContextHolder;
import com.saas.budgetmanagement.utility.ResourceEventContext;
import com.saas.budgetmanagement.utility.SecurityUtil;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.util.UUID;

public class BaseEntityListener {

    @PrePersist
    public void setTenantId(Base base) {
        SecurityUtil securityUtil = SpringContextHolder.getBean(SecurityUtil.class);
        base.setTenantId(UUID.fromString(securityUtil.getTenantId()));
    }

    @PrePersist
    public void setCreatedByAndUpdatedBy(Base base) {
        String createdBy = ResourceEventContext.get();
        if (createdBy != null) {
            base.setCreatedBy(createdBy);
        } else {
            String name = SpringContextHolder.getBean(SecurityUtil.class).getName();
            base.setCreatedBy(name != null ? name : "unknown");
        }
    }

    @PreUpdate
    public void setUpdatedBy(Base base) {
        String name = SpringContextHolder.getBean(SecurityUtil.class).getName();
        base.setUpdatedBy(name != null ? name : "unknown");
    }
}
