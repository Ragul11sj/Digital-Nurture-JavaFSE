package com.ragulsj.eventmanagement.exception;

public class ValidationException extends EventManagementException {
    private final String fieldName;

    public ValidationException(String fieldName, String message) {
        super("Validation failed for [" + fieldName + "]: " + message, "VAL-001");
        this.fieldName = fieldName;
    }

    public String getFieldName() { return fieldName; }
}
