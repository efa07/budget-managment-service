package com.saas.budgetmanagement.repository.refcache;

import com.saas.budgetmanagement.model.refcache.RefFundSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RefFundSourceRepository extends JpaRepository<RefFundSource, UUID> {
}