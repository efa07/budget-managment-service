package com.saas.budgetmanagement.exception;

public class InvalidBudgetRevisionException extends RuntimeException {
    public InvalidBudgetRevisionException(String message) {
        super(message);
    }
}