package com.saas.budgetmanagement.repository;

import com.saas.budgetmanagement.enums.TransferStatus;
import com.saas.budgetmanagement.model.BudgetTransfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BudgetTransferRepository extends JpaRepository<BudgetTransfer, UUID> {

    Optional<BudgetTransfer> findByTenantIdAndId(UUID tenantId, UUID id);

    Page<BudgetTransfer> findByTenantId(UUID tenantId, Pageable pageable);

    Page<BudgetTransfer> findByTenantIdAndStatus(UUID tenantId, TransferStatus status, Pageable pageable);

    List<BudgetTransfer> findByTenantIdAndFromAllocationId(UUID tenantId, UUID fromAllocationId);

    List<BudgetTransfer> findByTenantIdAndToAllocationId(UUID tenantId, UUID toAllocationId);
}