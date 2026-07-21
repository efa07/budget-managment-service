package com.saas.budgetmanagement.service.impl;

import com.saas.budgetmanagement.dto.common.CoaSegmentReference;
import com.saas.budgetmanagement.dto.eventDto.inbound.CommitmentSourceEvent;
import com.saas.budgetmanagement.dto.responseDto.CommitmentResponse;
import com.saas.budgetmanagement.enums.CommitmentStatus;
import com.saas.budgetmanagement.enums.SourceService;
import com.saas.budgetmanagement.exception.BudgetNotFoundException;
import com.saas.budgetmanagement.exception.InvalidCoaSegmentException;
import com.saas.budgetmanagement.mapper.CommitmentMapper;
import com.saas.budgetmanagement.model.Commitment;
import com.saas.budgetmanagement.repository.CommitmentRepository;
import com.saas.budgetmanagement.service.BudgetEventPublisherService;
import com.saas.budgetmanagement.service.BudgetLedgerService;
import com.saas.budgetmanagement.service.CommitmentService;
import com.saas.budgetmanagement.utility.CoaSegmentDisplayResolver;
import com.saas.budgetmanagement.utility.CoaSegmentValidator;
import com.saas.budgetmanagement.utility.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommitmentServiceImpl implements CommitmentService {

    private static final String ROUTING_COMMITMENT_REGISTERED = "budget.commitment.registered";
    private static final String ROUTING_COMMITMENT_LIQUIDATED = "budget.commitment.liquidated";

    private final CommitmentRepository commitmentRepository;
    private final BudgetLedgerService budgetLedgerService;
    private final CoaSegmentValidator coaSegmentValidator;
    private final CoaSegmentDisplayResolver displayResolver;
    private final BudgetEventPublisherService eventPublisherService;
    private final CommitmentMapper commitmentMapper;
    private final SecurityUtil securityUtil;

    @Override
    @Transactional
    public void processCommitmentEvent(UUID tenantId, SourceService sourceService,
                                        CommitmentSourceEvent event, String correlationId) {

        boolean alreadyProcessed = commitmentRepository
                .findByTenantIdAndSourceServiceAndSourceEventId(tenantId, sourceService, event.getSourceEventId())
                .isPresent();

        if (alreadyProcessed) {
            log.info("Commitment event already processed, skipping (idempotent): sourceService={}, sourceEventId={}",
                    sourceService, event.getSourceEventId());
            return;
        }

        CoaSegmentReference scope = new CoaSegmentReference(
                event.getAccountId(), event.getCostCenterId(), event.getDepartmentId(),
                event.getFundSourceId(), event.getProjectId());

        try {
            coaSegmentValidator.validate(tenantId, scope);
        } catch (InvalidCoaSegmentException e) {
            log.error("Rejected commitment event due to invalid COA segment: sourceService={}, sourceReferenceId={}, error={}",
                    sourceService, event.getSourceReferenceId(), e.getMessage());
            throw e;
        }

        Commitment commitment = new Commitment();
        commitment.setTenantId(tenantId);
        commitment.setSourceService(sourceService);
        commitment.setSourceReferenceId(event.getSourceReferenceId());
        commitment.setSourceEventId(event.getSourceEventId());
        commitment.setFiscalYearId(event.getFiscalYearId());
        commitment.setPeriodId(event.getPeriodId());
        commitment.setAccountId(event.getAccountId());
        commitment.setCostCenterId(event.getCostCenterId());
        commitment.setDepartmentId(event.getDepartmentId());
        commitment.setFundSourceId(event.getFundSourceId());
        commitment.setProjectId(event.getProjectId());
        commitment.setAmount(event.getAmount());
        commitment.setStatus(mapActionToStatus(event.getAction()));

        Commitment saved = commitmentRepository.save(commitment);

        budgetLedgerService.recalculateCommitted(tenantId, event.getFiscalYearId(), event.getPeriodId(), scope);

        publishCommitmentEvent(saved, correlationId);
    }

    @Override
    @Transactional
    public CommitmentResponse adjustManually(UUID commitmentId, BigDecimal adjustmentAmount, String reason) {
        UUID tenantId = UUID.fromString(securityUtil.getTenantId());

        Commitment original = commitmentRepository.findByTenantIdAndId(tenantId, commitmentId)
                .orElseThrow(() -> new BudgetNotFoundException("Commitment not found: " + commitmentId));

        Commitment adjustment = new Commitment();
        adjustment.setTenantId(tenantId);
        adjustment.setSourceService(original.getSourceService());
        adjustment.setSourceReferenceId(original.getSourceReferenceId());
        adjustment.setSourceEventId("manual-adjustment-" + UUID.randomUUID());
        adjustment.setFiscalYearId(original.getFiscalYearId());
        adjustment.setPeriodId(original.getPeriodId());
        adjustment.setAccountId(original.getAccountId());
        adjustment.setCostCenterId(original.getCostCenterId());
        adjustment.setDepartmentId(original.getDepartmentId());
        adjustment.setFundSourceId(original.getFundSourceId());
        adjustment.setProjectId(original.getProjectId());
        adjustment.setAmount(adjustmentAmount);
        adjustment.setStatus(CommitmentStatus.ADJUSTED);

        Commitment saved = commitmentRepository.save(adjustment);

        CoaSegmentReference scope = new CoaSegmentReference(
                original.getAccountId(), original.getCostCenterId(), original.getDepartmentId(),
                original.getFundSourceId(), original.getProjectId());
        budgetLedgerService.recalculateCommitted(tenantId, original.getFiscalYearId(), original.getPeriodId(), scope);

        log.info("Manual commitment adjustment created: originalId={}, adjustmentId={}, amount={}, reason={}",
                commitmentId, saved.getId(), adjustmentAmount, reason);

        CommitmentResponse response = commitmentMapper.toResponse(saved);
        displayResolver.enrichCoaSegments(tenantId, List.of(response));
        return response;
    }

    @Override
    public Page<CommitmentResponse> list(SourceService sourceService, String status, Pageable pageable) {
        UUID tenantId = UUID.fromString(securityUtil.getTenantId());
        CommitmentStatus commitmentStatus = status != null ? CommitmentStatus.valueOf(status) : null;

        Page<CommitmentResponse> page = commitmentRepository
                .findByTenantIdAndSourceServiceAndStatus(tenantId, sourceService, commitmentStatus, pageable)
                .map(commitmentMapper::toResponse);

        displayResolver.enrichCoaSegments(tenantId, page.getContent());
        return page;
    }

    private CommitmentStatus mapActionToStatus(CommitmentSourceEvent.CommitmentAction action) {
        return switch (action) {
            case REGISTER -> CommitmentStatus.REGISTERED;
            case ADJUST -> CommitmentStatus.ADJUSTED;
            case LIQUIDATE -> CommitmentStatus.LIQUIDATED;
        };
    }

    private void publishCommitmentEvent(Commitment commitment, String correlationId) {
        if (commitment.getStatus() == CommitmentStatus.LIQUIDATED) {
            eventPublisherService.publish(
                    "budget.commitment.liquidated", ROUTING_COMMITMENT_LIQUIDATED,
                    commitmentMapper.toLiquidatedEvent(commitment), correlationId);
        } else {
            eventPublisherService.publish(
                    "budget.commitment.registered", ROUTING_COMMITMENT_REGISTERED,
                    commitmentMapper.toRegisteredEvent(commitment), correlationId);
        }
    }
}