package com.saas.budgetmanagement.repository.refcache;

import com.saas.budgetmanagement.model.refcache.RefDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RefDepartmentRepository extends JpaRepository<RefDepartment, UUID> {
}