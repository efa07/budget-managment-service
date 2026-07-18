package com.saas.budgetmanagement.messaging.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.saas.budgetmanagement.dto.eventDto.inbound.AccountingPeriodSyncEvent;
import com.saas.budgetmanagement.dto.eventDto.inbound.CoaSyncEvent;
import com.saas.budgetmanagement.dto.eventDto.inbound.FiscalYearSyncEvent;
import com.saas.budgetmanagement.service.ReferenceDataSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class FinanceAdminEventListener {

    private final ReferenceDataSyncService referenceDataSyncService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "${budget-management.rabbitmq.queues.finance-admin-sync}")
    public void onFinanceAdminEvent(String messageBody,
                                     Channel channel,
                                     @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            JsonNode envelope = objectMapper.readTree(messageBody);
            String eventType = envelope.path("eventType").asText(null);
            UUID tenantId = UUID.fromString(envelope.path("tenantId").asText());
            JsonNode payloadNode = envelope.path("payload");

            if (eventType == null) {
                log.error("Received Finance Administration event with no eventType — cannot route. Rejecting without requeue: {}", messageBody);
                channel.basicNack(deliveryTag, false, false);
                return;
            }

            switch (eventType) {
                case "finance-admin.fiscal-year.synced" -> {
                    FiscalYearSyncEvent event = objectMapper.treeToValue(payloadNode, FiscalYearSyncEvent.class);
                    referenceDataSyncService.syncFiscalYear(tenantId, event);
                }
                case "finance-admin.period.synced" -> {
                    AccountingPeriodSyncEvent event = objectMapper.treeToValue(payloadNode, AccountingPeriodSyncEvent.class);
                    referenceDataSyncService.syncAccountingPeriod(tenantId, event);
                }
                case "finance-admin.coa.synced" -> {
                    CoaSyncEvent event = objectMapper.treeToValue(payloadNode, CoaSyncEvent.class);
                    referenceDataSyncService.syncCoaSegment(tenantId, event);
                }
                default -> {
                    log.warn("Received unrecognized Finance Administration eventType '{}' — acknowledging without action", eventType);
                }
            }

            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            log.error("Failed to process Finance Administration event, sending to dead-letter queue: {}", e.getMessage(), e);
            channel.basicNack(deliveryTag, false, false);
        }
    }
}