package com.saas.budgetmanagement.repository.refcache;

import com.saas.budgetmanagement.model.refcache.RefProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RefProjectRepository extends JpaRepository<RefProject, UUID> {
}