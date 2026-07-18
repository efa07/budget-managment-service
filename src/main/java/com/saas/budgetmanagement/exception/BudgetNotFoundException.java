package com.saas.budgetmanagement.exception;

public class BudgetNotFoundException extends ResourceNotFoundException {

    public BudgetNotFoundException(String message) {
        super(message);
    }
}
