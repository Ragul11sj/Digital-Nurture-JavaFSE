package com.ragulsj.eventmanagement.multithreading;

import com.ragulsj.eventmanagement.util.AppLogger;

public class NotificationThread extends Thread {

    private final String recipientEmail;
    private final String eventName;
    private final String messageType;

    public NotificationThread(String recipientEmail, String eventName, String messageType) {
        this.recipientEmail = recipientEmail;
        this.eventName = eventName;
        this.messageType = messageType;
        setName("Notifier-" + recipientEmail.split("@")[0]);
        setDaemon(true);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(200);
            String msg = buildMessage();
            AppLogger.info("[NOTIFY] " + msg);
            System.out.printf("  [Thread: %s] Notification sent: %s%n", Thread.currentThread().getName(), msg);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            AppLogger.warn("Notification thread interrupted for: " + recipientEmail);
        }
    }

    private String buildMessage() {
        return switch (messageType) {
            case "REGISTER" -> String.format("To: %s | You are registered for '%s'", recipientEmail, eventName);
            case "CANCEL"   -> String.format("To: %s | Your registration for '%s' is cancelled", recipientEmail, eventName);
            case "REMINDER" -> String.format("To: %s | Reminder: '%s' is coming up soon", recipientEmail, eventName);
            default         -> String.format("To: %s | Notification for '%s'", recipientEmail, eventName);
        };
    }
}
