package com.ragulsj.eventmanagement.exception;

public class RegistrationException extends EventManagementException {
    public RegistrationException(String message) {
        super(message, "REG-001");
    }
}
