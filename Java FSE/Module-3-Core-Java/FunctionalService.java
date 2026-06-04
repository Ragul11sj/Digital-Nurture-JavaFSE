package com.ragulsj.eventmanagement.functional;

import com.ragulsj.eventmanagement.model.Event;
import com.ragulsj.eventmanagement.model.Registration;
import com.ragulsj.eventmanagement.model.User;

import java.time.LocalDate;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class FunctionalService {

    @FunctionalInterface
    public interface EventFilter {
        boolean test(Event event);
    }

    @FunctionalInterface
    public interface UserProcessor {
        String process(User user);
    }

    private final Predicate<Event> upcomingFilter =
            event -> event.getEventDate().isAfter(LocalDate.now());

    private final Predicate<Event> freeFilter =
            event -> event.getFee() == 0.0;

    private final Predicate<Event> hasSeatsFilter =
            event -> event.getAvailableSeats() > 0;

    private final Function<Event, String> eventSummary =
            event -> String.format("%s (%s) on %s at %s — %s",
                    event.getEventName(), event.getCategory(),
                    event.getEventDate(), event.getLocation(),
                    event.getFormattedFee());

    private final Comparator<Event> byDate = Comparator.comparing(Event::getEventDate);
    private final Comparator<Event> bySeats = Comparator.comparingInt(Event::getAvailableSeats).reversed();

    public List<Event> filterEvents(List<Event> events, EventFilter filter) {
        return events.stream()
                .filter(filter::test)
                .collect(Collectors.toList());
    }

    public List<Event> getUpcomingEvents(List<Event> events) {
        return events.stream()
                .filter(upcomingFilter)
                .filter(hasSeatsFilter)
                .sorted(byDate)
                .collect(Collectors.toList());
    }

    public List<Event> getFreeEvents(List<Event> events) {
        return events.stream()
                .filter(freeFilter)
                .collect(Collectors.toList());
    }

    public Map<Event.Category, Long> groupByCategory(List<Event> events) {
        return events.stream()
                .collect(Collectors.groupingBy(Event::getCategory, Collectors.counting()));
    }

    public OptionalDouble averageFee(List<Event> events) {
        return events.stream()
                .mapToDouble(Event::getFee)
                .average();
    }

    public Optional<Event> getMostPopularEvent(List<Event> events) {
        return events.stream()
                .max(Comparator.comparingInt(e -> e.getTotalSeats() - e.getAvailableSeats()));
    }

    public Optional<Event> findEventByName(List<Event> events, String name) {
        return events.stream()
                .filter(e -> e.getEventName().toLowerCase().contains(name.toLowerCase()))
                .findFirst();
    }

    public List<String> getEventSummaries(List<Event> events) {
        return events.stream()
                .map(eventSummary)
                .collect(Collectors.toList());
    }

    public double getTotalRevenuePotential(List<Event> events) {
        return events.stream()
                .mapToDouble(e -> e.getFee() * e.getTotalSeats())
                .sum();
    }

    public List<User> getUsersFromCity(List<User> users, String city) {
        return users.stream()
                .filter(u -> u.getCity().equalsIgnoreCase(city))
                .sorted(Comparator.comparing(User::getName))
                .collect(Collectors.toList());
    }

    public Map<String, Long> getUserCountByCity(List<User> users) {
        return users.stream()
                .collect(Collectors.groupingBy(User::getCity, Collectors.counting()));
    }

    public long countRegistrationsForEvent(List<Registration> registrations, int eventId) {
        return registrations.stream()
                .filter(r -> r.getEventId() == eventId)
                .count();
    }

    public List<String> getUserNamesForEvent(List<Registration> registrations, int eventId) {
        return registrations.stream()
                .filter(r -> r.getEventId() == eventId)
                .map(Registration::getUserName)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public Supplier<List<Event>> lazyEventLoader(List<Event> source) {
        return () -> source.stream()
                .filter(upcomingFilter)
                .collect(Collectors.toList());
    }

    public Consumer<Event> printEventDetails() {
        return event -> System.out.println(event.toDisplayString());
    }

    public UnaryOperator<String> normalizeCategory() {
        return s -> s.trim().toUpperCase();
    }

    public BinaryOperator<Double> totalFees() {
        return Double::sum;
    }

    public void printStatistics(List<Event> events, List<User> users, List<Registration> registrations) {
        System.out.println("\n--- Functional Statistics ---");

        long upcoming = events.stream().filter(upcomingFilter).count();
        System.out.println("Upcoming Events   : " + upcoming);

        OptionalDouble avgFee = averageFee(events);
        avgFee.ifPresent(f -> System.out.printf("Average Event Fee : INR %.2f%n", f));

        Optional<Event> popular = getMostPopularEvent(events);
        popular.ifPresent(e -> System.out.println("Most Popular      : " + e.getEventName()));

        Map<Event.Category, Long> catMap = groupByCategory(events);
        System.out.println("By Category       : " + catMap);

        Map<String, Long> cityMap = getUserCountByCity(users);
        System.out.println("Users by City     : " + cityMap);

        double revenue = getTotalRevenuePotential(events);
        System.out.printf("Revenue Potential : INR %.2f%n", revenue);

        String allNames = users.stream()
                .map(User::getName)
                .collect(Collectors.joining(", "));
        System.out.println("All Users         : " + allNames);

        System.out.println("--- End Statistics ---\n");
    }
}
