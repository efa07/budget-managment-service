package com.saas.budgetmanagement.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saas.budgetmanagement.dto.eventDto.envelope.EventEnvelope;
import com.saas.budgetmanagement.model.OutboxEvent;
import com.saas.budgetmanagement.repository.OutboxEventRepository;
import com.saas.budgetmanagement.service.BudgetEventPublisherService;
import com.saas.budgetmanagement.utility.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BudgetEventPublisherServiceImpl implements BudgetEventPublisherService {

    private static final int CURRENT_EVENT_VERSION = 1;

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void publish(String eventType, String routingKey, Object payload) {
        publish(eventType, routingKey, payload, UUID.randomUUID().toString());
    }

    @Override
    @Transactional
    public void publish(String eventType, String routingKey, Object payload, String correlationId) {
        SecurityUtil securityUtil = new SecurityUtil();
        UUID tenantId = UUID.fromString(securityUtil.getTenantId());

        EventEnvelope<Object> envelope = new EventEnvelope<>(
                UUID.randomUUID(),
                eventType,
                LocalDateTime.now(),
                tenantId,
                correlationId,
                CURRENT_EVENT_VERSION,
                payload
        );

        String serializedPayload;
        try {
            serializedPayload = objectMapper.writeValueAsString(envelope);
        } catch (Exception e) {
            log.error("Failed to serialize event payload for eventType={}, routingKey={}", eventType, routingKey, e);
            throw new IllegalStateException("Unable to serialize event envelope", e);
        }

        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setEventType(eventType);
        outboxEvent.setRoutingKey(routingKey);
        outboxEvent.setPayload(serializedPayload);
        outboxEvent.setCorrelationId(correlationId);
        outboxEvent.setPublished(false);
        outboxEvent.setRetryCount(0);

        outboxEventRepository.save(outboxEvent);
    }
}