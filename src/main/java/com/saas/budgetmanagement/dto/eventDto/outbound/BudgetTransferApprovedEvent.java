package com.saas.budgetmanagement.dto.eventDto.outbound;

import com.saas.budgetmanagement.enums.TransferStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class BudgetTransferApprovedEvent {

    private UUID id;
    private UUID tenantId;
    private UUID fromAllocationId;
    private UUID toAllocationId;
    private BigDecimal amount;
    private TransferStatus status;
    private String justification;
    private UUID requestedBy;
    private UUID approvedBy;
}
