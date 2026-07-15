package com.saas.budgetmanagement.service;

public interface BudgetEventPublisherService {
    void publishEvent(Object event);
}