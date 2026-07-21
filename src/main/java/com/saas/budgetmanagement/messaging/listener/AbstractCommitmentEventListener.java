package com.saas.budgetmanagement.messaging.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.saas.budgetmanagement.dto.eventDto.inbound.CommitmentSourceEvent;
import com.saas.budgetmanagement.enums.SourceService;
import com.saas.budgetmanagement.service.CommitmentService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public abstract class AbstractCommitmentEventListener {

    protected final CommitmentService commitmentService;
    protected final ObjectMapper objectMapper;

    protected AbstractCommitmentEventListener(CommitmentService commitmentService, ObjectMapper objectMapper) {
        this.commitmentService = commitmentService;
        this.objectMapper = objectMapper;
    }

    protected void handle(String messageBody, Channel channel, long deliveryTag, SourceService sourceService) throws IOException {
        try {
            JsonNode envelope = objectMapper.readTree(messageBody);
            UUID tenantId = UUID.fromString(envelope.path("tenantId").asText());
            String correlationId = envelope.path("correlationId").asText(null);
            JsonNode payloadNode = envelope.path("payload");

            CommitmentSourceEvent event = objectMapper.treeToValue(payloadNode, CommitmentSourceEvent.class);

            commitmentService.processCommitmentEvent(tenantId, sourceService, event, correlationId);

            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            log.error("Failed to process {} commitment event, sending to dead-letter queue: {}",
                    sourceService, e.getMessage(), e);
            channel.basicNack(deliveryTag, false, false);
        }
    }
}