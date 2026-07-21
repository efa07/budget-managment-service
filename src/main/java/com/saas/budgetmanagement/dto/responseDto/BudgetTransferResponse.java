package com.saas.budgetmanagement.dto.responseDto;

import com.saas.budgetmanagement.enums.TransferStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class BudgetTransferResponse extends BaseResponse {

    private UUID fromAllocationId;
    private UUID toAllocationId;
    private BigDecimal amount;
    private TransferStatus status;
    private String justification;
    private UUID requestedBy;
    private UUID approvedBy;
}