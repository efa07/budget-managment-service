package com.saas.budgetmanagement.utility;

public class ResourceEventContext {

    private static final ThreadLocal<String> context = new ThreadLocal<>();

    public static void set(String createdBy) {
        context.set(createdBy);
    }

    public static String get() {
        return context.get();
    }

    public static void clear() {
        context.remove();
    }
}