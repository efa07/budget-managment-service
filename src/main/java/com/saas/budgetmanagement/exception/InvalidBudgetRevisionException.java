package com.saas.budgetmanagement.exception;

public class InvalidBudgetRevisionException extends InvalidBudgetStateException {
    public InvalidBudgetRevisionException(String message) {
        super(message);
    }
}