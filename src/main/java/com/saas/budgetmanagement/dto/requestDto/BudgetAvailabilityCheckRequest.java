package com.saas.budgetmanagement.dto.requestDto;

import com.saas.budgetmanagement.enums.SourceService;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class BudgetAvailabilityCheckRequest {

    @NotNull(message = "fiscalYearId is required")
    private UUID fiscalYearId;

    @NotNull(message = "periodId is required")
    private UUID periodId;

    @NotNull(message = "accountId is required")
    private UUID accountId;

    private UUID costCenterId;
    private UUID departmentId;
    private UUID fundSourceId;
    private UUID projectId;

    @NotNull(message = "requestedAmount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "requestedAmount must be greater than zero")
    private BigDecimal requestedAmount;

    @NotNull(message = "sourceService is required")
    private SourceService sourceService;

    @NotNull(message = "sourceReferenceId is required")
    private String sourceReferenceId;
}