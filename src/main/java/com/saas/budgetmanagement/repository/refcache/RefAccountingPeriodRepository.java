package com.saas.budgetmanagement.repository.refcache;

import com.saas.budgetmanagement.model.refcache.RefAccountingPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RefAccountingPeriodRepository extends JpaRepository<RefAccountingPeriod, UUID> {
}