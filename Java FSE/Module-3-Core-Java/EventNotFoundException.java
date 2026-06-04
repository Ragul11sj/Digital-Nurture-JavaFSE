package com.ragulsj.eventmanagement.exception;

public class EventNotFoundException extends EventManagementException {
    public EventNotFoundException(int eventId) {
        super("Event not found with ID: " + eventId, "EVT-001");
    }
    public EventNotFoundException(String message) {
        super(message, "EVT-001");
    }
}
