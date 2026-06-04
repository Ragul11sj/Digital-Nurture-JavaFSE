package com.ragulsj.eventmanagement.model;

import java.time.LocalDate;

public record EventRecord(int id, String name, String category, LocalDate date, String location, double fee) {

    public EventRecord {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Event name cannot be blank");
        if (fee < 0) throw new IllegalArgumentException("Fee cannot be negative");
    }

    public String formattedFee() {
        return fee == 0 ? "Free" : String.format("INR %.2f", fee);
    }

    public boolean isUpcoming() {
        return date.isAfter(LocalDate.now());
    }
}
