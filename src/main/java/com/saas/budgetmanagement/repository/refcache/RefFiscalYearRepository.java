package com.saas.budgetmanagement.repository.refcache;

import com.saas.budgetmanagement.model.refcache.RefFiscalYear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RefFiscalYearRepository extends JpaRepository<RefFiscalYear, UUID> {
}