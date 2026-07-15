package com.saas.budgetmanagement.repository;

import com.saas.budgetmanagement.model.Commitment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommitmentRepository extends JpaRepository<Commitment, UUID> {
}