package com.ragulsj.eventmanagement.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Registration implements Serializable {

    private static final long serialVersionUID = 3L;

    public enum PaymentStatus {
        PENDING, PAID, REFUNDED
    }

    public enum AttendanceStatus {
        REGISTERED, ATTENDED, ABSENT, CANCELLED
    }

    private int registrationId;
    private int userId;
    private int eventId;
    private LocalDateTime registrationTime;
    private PaymentStatus paymentStatus;
    private double amountPaid;
    private AttendanceStatus attendanceStatus;
    private String userName;
    private String eventName;

    public Registration() {}

    public Registration(int registrationId, int userId, int eventId, String userName, String eventName) {
        this.registrationId = registrationId;
        this.userId = userId;
        this.eventId = eventId;
        this.userName = userName;
        this.eventName = eventName;
        this.registrationTime = LocalDateTime.now();
        this.paymentStatus = PaymentStatus.PENDING;
        this.attendanceStatus = AttendanceStatus.REGISTERED;
        this.amountPaid = 0.0;
    }

    public int getRegistrationId() { return registrationId; }
    public void setRegistrationId(int registrationId) { this.registrationId = registrationId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }
    public LocalDateTime getRegistrationTime() { return registrationTime; }
    public void setRegistrationTime(LocalDateTime registrationTime) { this.registrationTime = registrationTime; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    public double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }
    public AttendanceStatus getAttendanceStatus() { return attendanceStatus; }
    public void setAttendanceStatus(AttendanceStatus attendanceStatus) { this.attendanceStatus = attendanceStatus; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    @Override
    public String toString() {
        return String.format("Registration[id=%d, userId=%d, eventId=%d, status=%s]",
                registrationId, userId, eventId, attendanceStatus);
    }

    public String toDisplayString() {
        return String.format("%-5d %-25s %-30s %-20s %-10s %s",
                registrationId, userName, eventName, registrationTime.toLocalDate(), paymentStatus, attendanceStatus);
    }
}
