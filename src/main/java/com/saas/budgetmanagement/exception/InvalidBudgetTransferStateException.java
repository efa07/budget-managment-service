package com.saas.budgetmanagement.exception;

public class InvalidBudgetTransferStateException extends InvalidBudgetStateException {
    public InvalidBudgetTransferStateException(String message) {
        super(message);
    }
}