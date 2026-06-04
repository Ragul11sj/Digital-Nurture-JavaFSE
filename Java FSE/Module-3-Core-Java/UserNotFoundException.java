package com.ragulsj.eventmanagement.exception;

public class UserNotFoundException extends EventManagementException {
    public UserNotFoundException(int userId) {
        super("User not found with ID: " + userId, "USR-001");
    }
    public UserNotFoundException(String message) {
        super(message, "USR-001");
    }
}
