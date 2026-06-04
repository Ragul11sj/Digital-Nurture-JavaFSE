package com.ragulsj.eventmanagement.service;

import com.ragulsj.eventmanagement.collection.CollectionManager;
import com.ragulsj.eventmanagement.exception.*;
import com.ragulsj.eventmanagement.model.*;
import com.ragulsj.eventmanagement.multithreading.ThreadPoolManager;
import com.ragulsj.eventmanagement.util.*;

import java.util.*;
import java.util.stream.Collectors;

public class RegistrationService {

    private final CollectionManager cm;

    public RegistrationService(CollectionManager cm) {
        this.cm = cm;
    }

    public Registration registerUserForEvent(int userId, int eventId)
            throws UserNotFoundException, EventNotFoundException, RegistrationException {

        Optional<User> userOpt = cm.findUserById(userId);
        if (userOpt.isEmpty()) throw new UserNotFoundException(userId);

        Optional<Event> eventOpt = cm.findEventById(eventId);
        if (eventOpt.isEmpty()) throw new EventNotFoundException(eventId);

        User user = userOpt.get();
        Event event = eventOpt.get();

        if (event.isFull()) {
            throw new RegistrationException("Event '" + event.getEventName() + "' is fully booked.");
        }
        if (event.getStatus() == Event.Status.CANCELLED) {
            throw new RegistrationException("Event '" + event.getEventName() + "' is cancelled.");
        }
        if (event.getStatus() == Event.Status.COMPLETED) {
            throw new RegistrationException("Event '" + event.getEventName() + "' is already completed.");
        }
        if (cm.isAlreadyRegistered(userId, eventId)) {
            throw new RegistrationException("User '" + user.getName() + "' is already registered for this event.");
        }

        int regId = IdGenerator.nextRegistrationId();
        Registration reg = new Registration(regId, userId, eventId, user.getName(), event.getEventName());
        reg.setAmountPaid(event.getFee());
        reg.setPaymentStatus(event.getFee() == 0
                ? Registration.PaymentStatus.PAID
                : Registration.PaymentStatus.PENDING);

        cm.addRegistration(reg);
        event.setAvailableSeats(event.getAvailableSeats() - 1);

        ThreadPoolManager.getInstance().sendNotification(user.getEmail(), event.getEventName(), "REGISTER");

        AppLogger.info("Registration: user=" + userId + " event=" + eventId + " reg=" + regId);
        return reg;
    }

    public void cancelRegistration(int userId, int eventId)
            throws UserNotFoundException, EventNotFoundException, RegistrationException {

        if (cm.findUserById(userId).isEmpty()) throw new UserNotFoundException(userId);
        Optional<Event> eventOpt = cm.findEventById(eventId);
        if (eventOpt.isEmpty()) throw new EventNotFoundException(eventId);

        if (!cm.isAlreadyRegistered(userId, eventId)) {
            throw new RegistrationException("No registration found for user " + userId + " on event " + eventId);
        }

        Event event = eventOpt.get();
        boolean removed = cm.cancelRegistration(userId, eventId);
        if (removed) {
            event.setAvailableSeats(event.getAvailableSeats() + 1);
            cm.findUserById(userId).ifPresent(u ->
                ThreadPoolManager.getInstance().sendNotification(u.getEmail(), event.getEventName(), "CANCEL")
            );
            AppLogger.info("Registration cancelled: user=" + userId + " event=" + eventId);
        }
    }

    public List<Registration> getAllRegistrations() {
        return new ArrayList<>(cm.getAllRegistrations());
    }

    public List<Registration> getRegistrationsForEvent(int eventId) {
        return new ArrayList<>(cm.getRegistrationsForEvent(eventId));
    }

    public List<Registration> getRegistrationsForUser(int userId) {
        return cm.getAllRegistrations().stream()
                .filter(r -> r.getUserId() == userId)
                .collect(Collectors.toList());
    }

    public void printAllRegistrations() {
        List<Registration> regs = getAllRegistrations();
        if (regs.isEmpty()) {
            ConsoleUtil.printWarning("No registrations found.");
            return;
        }
        System.out.printf("%n%-5s %-25s %-30s %-12s %-10s %s%n",
                "ID", "USER", "EVENT", "DATE", "PAYMENT", "ATTENDANCE");
        ConsoleUtil.printDivider();
        regs.forEach(r -> System.out.println(r.toDisplayString()));
        ConsoleUtil.printDivider();
        System.out.println("Total: " + regs.size() + " registration(s)");
    }

    public void printRegistrationsForEvent(int eventId) {
        List<Registration> regs = getRegistrationsForEvent(eventId);
        if (regs.isEmpty()) {
            ConsoleUtil.printWarning("No registrations for event ID " + eventId);
            return;
        }
        regs.forEach(r -> System.out.println(r.toDisplayString()));
    }

    public int getTotalRegistrations() {
        return cm.getAllRegistrations().size();
    }
}
