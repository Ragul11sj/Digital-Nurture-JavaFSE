package com.ragulsj.eventmanagement.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Event implements Serializable, Comparable<Event> {

    private static final long serialVersionUID = 2L;

    public enum Category {
        CULTURAL, TECHNOLOGY, WELLNESS, FOOD, MUSIC, SPORTS, OTHER
    }

    public enum Status {
        UPCOMING, ONGOING, COMPLETED, CANCELLED
    }

    private int eventId;
    private String eventName;
    private Category category;
    private LocalDate eventDate;
    private LocalTime startTime;
    private String location;
    private int totalSeats;
    private int availableSeats;
    private double fee;
    private String organizer;
    private Status status;
    private String description;

    public Event() {}

    public Event(int eventId, String eventName, Category category, LocalDate eventDate,
                 LocalTime startTime, String location, int totalSeats, double fee,
                 String organizer, String description) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.category = category;
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.location = location;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.fee = fee;
        this.organizer = organizer;
        this.description = description;
        this.status = Status.UPCOMING;
    }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }
    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
    public double getFee() { return fee; }
    public void setFee(double fee) { this.fee = fee; }
    public String getOrganizer() { return organizer; }
    public void setOrganizer(String organizer) { this.organizer = organizer; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isFull() { return availableSeats <= 0; }
    public boolean isLowSeats() { return availableSeats > 0 && availableSeats <= 10; }

    public String getFormattedFee() {
        return fee == 0 ? "Free" : String.format("INR %.2f", fee);
    }

    @Override
    public int compareTo(Event other) {
        return this.eventDate.compareTo(other.eventDate);
    }

    @Override
    public String toString() {
        return String.format("Event[id=%d, name=%s, category=%s, date=%s, location=%s, seats=%d, fee=%.2f]",
                eventId, eventName, category, eventDate, location, availableSeats, fee);
    }

    public String toDisplayString() {
        return String.format("%-5d %-30s %-12s %-12s %-25s %-6d %-10s %s",
                eventId, eventName, category, eventDate, location, availableSeats, getFormattedFee(), status);
    }
}
