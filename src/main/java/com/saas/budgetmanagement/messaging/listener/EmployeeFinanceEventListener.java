package com.saas.budgetmanagement.messaging.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.saas.budgetmanagement.enums.SourceService;
import com.saas.budgetmanagement.service.CommitmentService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EmployeeFinanceEventListener extends AbstractCommitmentEventListener {

    public EmployeeFinanceEventListener(CommitmentService commitmentService, ObjectMapper objectMapper) {
        super(commitmentService, objectMapper);
    }

    @RabbitListener(queues = "${budget-management.rabbitmq.queues.employee-finance-sync}")
    public void onEmployeeFinanceEvent(String messageBody,
                                        Channel channel,
                                        @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        handle(messageBody, channel, deliveryTag, SourceService.EMPLOYEE_FINANCE);
    }
}