package com.saas.budgetmanagement.service;

public interface BudgetEventPublisherService {

    void publish(String eventType, String routingKey, Object payload);

    void publish(String eventType, String routingKey, Object payload, String correlationId);
}