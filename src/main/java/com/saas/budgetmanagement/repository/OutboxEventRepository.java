package com.saas.budgetmanagement.repository;

import com.saas.budgetmanagement.model.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import jakarta.persistence.LockModeType;
import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, java.util.UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM OutboxEvent o WHERE o.published = false ORDER BY o.createdAt ASC")
    List<OutboxEvent> findUnpublishedOrderByCreatedAtAsc(org.springframework.data.domain.Pageable pageable);

    List<OutboxEvent> findByTenantIdAndPublishedFalseAndRetryCountLessThan(
            java.util.UUID tenantId, int maxRetryCount);
}