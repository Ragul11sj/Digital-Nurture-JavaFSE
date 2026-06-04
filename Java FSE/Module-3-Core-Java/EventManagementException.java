package com.ragulsj.eventmanagement.exception;

public class EventManagementException extends Exception {

    private final String errorCode;

    public EventManagementException(String message) {
        super(message);
        this.errorCode = "EME-000";
    }

    public EventManagementException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public EventManagementException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "EME-000";
    }

    public String getErrorCode() { return errorCode; }

    @Override
    public String toString() {
        return String.format("[%s] %s", errorCode, getMessage());
    }
}
