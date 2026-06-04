package com.ragulsj.eventmanagement.service;

import com.ragulsj.eventmanagement.collection.CollectionManager;
import com.ragulsj.eventmanagement.exception.*;
import com.ragulsj.eventmanagement.model.Event;
import com.ragulsj.eventmanagement.util.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class EventService {

    private final CollectionManager cm;

    public EventService(CollectionManager cm) {
        this.cm = cm;
    }

    public Event addEvent(String name, Event.Category category, String dateStr, String timeStr,
                          String location, int seats, double fee, String organizer, String description)
            throws ValidationException {
        Validator.validateNotBlank(name, "eventName");
        Validator.validateNotBlank(location, "location");
        Validator.validateNotBlank(organizer, "organizer");
        Validator.validateSeats(seats);
        Validator.validatePositive(fee, "fee");

        LocalDate date;
        LocalTime time;
        try {
            date = LocalDate.parse(dateStr);
            time = LocalTime.parse(timeStr);
        } catch (Exception e) {
            throw new ValidationException("date/time", "invalid format. Use YYYY-MM-DD and HH:MM");
        }
        Validator.validateFutureDate(date);

        int id = IdGenerator.nextEventId();
        Event event = new Event(id, name, category, date, time, location, seats, fee, organizer, description);
        cm.addEvent(event);
        AppLogger.info("Event added: " + event);
        return event;
    }

    public Event getEventById(int eventId) throws EventNotFoundException {
        return cm.findEventById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
    }

    public List<Event> getAllEvents() {
        return new ArrayList<>(cm.getAllEvents());
    }

    public List<Event> getUpcomingEvents() {
        return cm.getAllEvents().stream()
                .filter(e -> e.getEventDate().isAfter(LocalDate.now()))
                .filter(e -> e.getStatus() == Event.Status.UPCOMING)
                .sorted(Comparator.comparing(Event::getEventDate))
                .collect(Collectors.toList());
    }

    public List<Event> getEventsByCategory(Event.Category category) {
        return cm.getAllEvents().stream()
                .filter(e -> e.getCategory() == category)
                .collect(Collectors.toList());
    }

    public void updateEventStatus(int eventId, Event.Status status) throws EventNotFoundException {
        Event event = getEventById(eventId);
        event.setStatus(status);
        event.touch();
        AppLogger.info("Event " + eventId + " status -> " + status);
    }

    public void deleteEvent(int eventId) throws EventNotFoundException, RegistrationException {
        Event event = getEventById(eventId);
        if (!cm.getRegistrationsForEvent(eventId).isEmpty()) {
            throw new RegistrationException("Cannot delete event '" + event.getEventName() +
                    "' — it has active registrations. Cancel registrations first.");
        }
        cm.removeEvent(eventId);
        AppLogger.info("Event deleted: " + eventId);
    }

    public void printAllEvents() {
        List<Event> events = getAllEvents();
        if (events.isEmpty()) {
            ConsoleUtil.printWarning("No events available.");
            return;
        }
        System.out.printf("%n%-5s %-30s %-12s %-12s %-25s %-6s %-12s %s%n",
                "ID", "NAME", "CATEGORY", "DATE", "LOCATION", "SEATS", "FEE", "STATUS");
        ConsoleUtil.printDivider();
        events.stream()
                .sorted(Comparator.comparing(Event::getEventDate))
                .forEach(e -> System.out.println(e.toDisplayString()));
        ConsoleUtil.printDivider();
        System.out.println("Total: " + events.size() + " event(s)");
    }

    public void printEventDetails(int eventId) throws EventNotFoundException {
        Event e = getEventById(eventId);
        System.out.println("\nEvent Details:");
        ConsoleUtil.printDivider();
        System.out.println("ID          : " + e.getEventId());
        System.out.println("Name        : " + e.getEventName());
        System.out.println("Category    : " + e.getCategory());
        System.out.println("Date        : " + e.getEventDate());
        System.out.println("Time        : " + e.getStartTime());
        System.out.println("Location    : " + e.getLocation());
        System.out.println("Total Seats : " + e.getTotalSeats());
        System.out.println("Available   : " + e.getAvailableSeats());
        System.out.println("Fee         : " + e.getFormattedFee());
        System.out.println("Organizer   : " + e.getOrganizer());
        System.out.println("Status      : " + e.getStatus());
        System.out.println("Description : " + e.getDescription());
        ConsoleUtil.printDivider();
    }

    public int getTotalEvents() {
        return cm.getAllEvents().size();
    }

    public int getTotalAvailableSeats() {
        return cm.getAllEvents().stream().mapToInt(Event::getAvailableSeats).sum();
    }

    public void demonstrateArrays() {
        int[] seats = {100, 200, 50, 300, 150};
        System.out.println("\n--- Array Demo ---");
        System.out.print("Seat counts: ");
        for (int s : seats) System.out.print(s + " ");
        System.out.println();

        int total = 0;
        for (int s : seats) total += s;
        System.out.println("Total seats (array sum): " + total);

        int[][] grid = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        System.out.println("2D Array [1][2] = " + grid[1][2]);

        String[] names = {"Marina Walk", "AI Workshop", "Yoga Session"};
        StringBuilder sb = new StringBuilder();
        StringBuffer sbuf = new StringBuffer();
        for (String n : names) {
            sb.append(n).append(" | ");
            sbuf.append(n).append(", ");
        }
        System.out.println("StringBuilder: " + sb);
        System.out.println("StringBuffer : " + sbuf);
        System.out.println("String.format: " + String.format("Events: %d", names.length));
        System.out.println("--- End Array Demo ---\n");
    }

    public void demonstrateTypeCasting() {
        double fee = 499.99;
        int intFee = (int) fee;
        long longFee = (long) fee;
        float floatFee = (float) fee;

        System.out.println("\n--- Type Casting Demo ---");
        System.out.println("double fee   : " + fee);
        System.out.println("int (cast)   : " + intFee);
        System.out.println("long (cast)  : " + longFee);
        System.out.println("float (cast) : " + floatFee);
        System.out.println("--- End Type Casting Demo ---\n");
    }
}
