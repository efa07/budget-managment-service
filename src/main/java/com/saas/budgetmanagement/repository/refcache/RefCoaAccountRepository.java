package com.saas.budgetmanagement.repository.refcache;

import com.saas.budgetmanagement.model.refcache.RefCoaAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RefCoaAccountRepository extends JpaRepository<RefCoaAccount, UUID> {
}