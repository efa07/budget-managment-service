package com.saas.budgetmanagement.exception;

public abstract class InvalidBudgetStateException extends RuntimeException {
    protected InvalidBudgetStateException(String message) {
        super(message);
    }
}