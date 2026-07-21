package com.saas.budgetmanagement.service;

import com.saas.budgetmanagement.dto.eventDto.inbound.CommitmentSourceEvent;
import com.saas.budgetmanagement.dto.responseDto.CommitmentResponse;
import com.saas.budgetmanagement.enums.SourceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.UUID;

public interface CommitmentService {

    void processCommitmentEvent(UUID tenantId, SourceService sourceService,
                                 CommitmentSourceEvent event, String correlationId);

    CommitmentResponse adjustManually(UUID commitmentId, BigDecimal adjustmentAmount, String reason);

    Page<CommitmentResponse> list(SourceService sourceService, String status, Pageable pageable);
}