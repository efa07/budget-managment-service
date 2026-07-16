package com.saas.budgetmanagement.repository;

import com.saas.budgetmanagement.enums.CommitmentStatus;
import com.saas.budgetmanagement.enums.SourceService;
import com.saas.budgetmanagement.model.Commitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommitmentRepository extends JpaRepository<Commitment, UUID> {

    Optional<Commitment> findByTenantIdAndId(UUID tenantId, UUID id);

    Optional<Commitment> findByTenantIdAndSourceServiceAndSourceEventId(
            UUID tenantId, SourceService sourceService, String sourceEventId);

    List<Commitment> findByTenantIdAndSourceServiceAndSourceReferenceId(
            UUID tenantId, SourceService sourceService, String sourceReferenceId);

    Page<Commitment> findByTenantIdAndSourceServiceAndStatus(
            UUID tenantId, SourceService sourceService, CommitmentStatus status, Pageable pageable);

    @Query(
        "SELECT COALESCE(SUM(c.amount), 0) FROM Commitment c " +
        "WHERE c.tenantId = :tenantId AND c.accountId = :accountId " +
        "AND (:costCenterId IS NULL OR c.costCenterId = :costCenterId) " +
        "AND (:departmentId IS NULL OR c.departmentId = :departmentId) " +
        "AND (:fundSourceId IS NULL OR c.fundSourceId = :fundSourceId) " +
        "AND (:projectId IS NULL OR c.projectId = :projectId)"
    )
    BigDecimal sumCommittedByCoaScope(
            @Param("tenantId") UUID tenantId,
            @Param("accountId") UUID accountId,
            @Param("costCenterId") UUID costCenterId,
            @Param("departmentId") UUID departmentId,
            @Param("fundSourceId") UUID fundSourceId,
            @Param("projectId") UUID projectId);
}