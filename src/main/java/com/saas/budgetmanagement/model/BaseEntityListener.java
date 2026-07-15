package com.saas.budgetmanagement.model;

import com.saas.budgetmanagement.utility.ResourceEventContext;
import com.saas.budgetmanagement.utility.SecurityUtil;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BaseEntityListener {

    private final SecurityUtil securityUtil;

    @PrePersist
    public void setCreatedByAndUpdatedBy(Base base) {
        String createdBy = ResourceEventContext.get();
        if (createdBy != null) {
            base.setCreatedBy(createdBy);
        } else {
            String name = securityUtil.getName();
            base.setCreatedBy(name != null ? name : "unknown");
        }
    }

    @PreUpdate
    public void setUpdatedBy(Base base) {
        String name = securityUtil.getName();
        base.setUpdatedBy(name != null ? name : "unknown");
    }
}
