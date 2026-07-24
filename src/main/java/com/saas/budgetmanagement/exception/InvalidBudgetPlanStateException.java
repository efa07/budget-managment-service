package com.saas.budgetmanagement.exception;

public class InvalidBudgetPlanStateException extends InvalidBudgetStateException {
    public InvalidBudgetPlanStateException(String message) {
        super(message);
    }
}