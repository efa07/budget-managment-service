package com.saas.budgetmanagement.service.impl;

import com.saas.budgetmanagement.service.BudgetEventPublisherService;
import org.springframework.stereotype.Service;

@Service
public class BudgetEventPublisherServiceImpl implements BudgetEventPublisherService {

    @Override
    public void publishEvent(Object event) {
        throw new UnsupportedOperationException("TODO");
    }
}