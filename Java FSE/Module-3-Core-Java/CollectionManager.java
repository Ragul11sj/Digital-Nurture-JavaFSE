package com.ragulsj.eventmanagement.collection;

import com.ragulsj.eventmanagement.model.Event;
import com.ragulsj.eventmanagement.model.Registration;
import com.ragulsj.eventmanagement.model.User;

import java.util.*;

public class CollectionManager {

    private final ArrayList<User> userList = new ArrayList<>();
    private final LinkedList<Event> eventLinkedList = new LinkedList<>();
    private final HashSet<String> emailSet = new HashSet<>();
    private final HashMap<Integer, List<Registration>> registrationMap = new HashMap<>();
    private final Queue<Registration> pendingQueue = new LinkedList<>();
    private final TreeMap<Integer, User> userTreeMap = new TreeMap<>();
    private final Stack<String> operationHistory = new Stack<>();

    public void addUser(User user) {
        userList.add(user);
        emailSet.add(user.getEmail());
        userTreeMap.put(user.getUserId(), user);
        operationHistory.push("ADD_USER:" + user.getUserId());
    }

    public boolean removeUser(int userId) {
        User u = userTreeMap.remove(userId);
        if (u != null) {
            userList.removeIf(user -> user.getUserId() == userId);
            emailSet.remove(u.getEmail());
            operationHistory.push("REMOVE_USER:" + userId);
            return true;
        }
        return false;
    }

    public Optional<User> findUserById(int userId) {
        return userList.stream().filter(u -> u.getUserId() == userId).findFirst();
    }

    public boolean emailExists(String email) {
        return emailSet.contains(email);
    }

    public List<User> getAllUsers() {
        return Collections.unmodifiableList(userList);
    }

    public void addEvent(Event event) {
        eventLinkedList.addLast(event);
        registrationMap.put(event.getEventId(), new ArrayList<>());
        operationHistory.push("ADD_EVENT:" + event.getEventId());
    }

    public boolean removeEvent(int eventId) {
        boolean removed = eventLinkedList.removeIf(e -> e.getEventId() == eventId);
        if (removed) {
            registrationMap.remove(eventId);
            operationHistory.push("REMOVE_EVENT:" + eventId);
        }
        return removed;
    }

    public Optional<Event> findEventById(int eventId) {
        return eventLinkedList.stream().filter(e -> e.getEventId() == eventId).findFirst();
    }

    public List<Event> getAllEvents() {
        return Collections.unmodifiableList(new ArrayList<>(eventLinkedList));
    }

    public void addRegistration(Registration reg) {
        registrationMap.computeIfAbsent(reg.getEventId(), k -> new ArrayList<>()).add(reg);
        pendingQueue.offer(reg);
        operationHistory.push("REGISTER:" + reg.getRegistrationId());
    }

    public boolean cancelRegistration(int userId, int eventId) {
        List<Registration> regs = registrationMap.get(eventId);
        if (regs != null) {
            boolean removed = regs.removeIf(r -> r.getUserId() == userId);
            if (removed) {
                operationHistory.push("CANCEL_REG:u" + userId + "e" + eventId);
                return true;
            }
        }
        return false;
    }

    public boolean isAlreadyRegistered(int userId, int eventId) {
        List<Registration> regs = registrationMap.getOrDefault(eventId, Collections.emptyList());
        return regs.stream().anyMatch(r -> r.getUserId() == userId);
    }

    public List<Registration> getRegistrationsForEvent(int eventId) {
        return Collections.unmodifiableList(registrationMap.getOrDefault(eventId, Collections.emptyList()));
    }

    public List<Registration> getAllRegistrations() {
        List<Registration> all = new ArrayList<>();
        registrationMap.values().forEach(all::addAll);
        return Collections.unmodifiableList(all);
    }

    public Registration pollPending() {
        return pendingQueue.poll();
    }

    public String undoLastOperation() {
        return operationHistory.isEmpty() ? "No operations to undo" : operationHistory.pop();
    }

    public Map<String, Integer> getStatistics() {
        Map<String, Integer> stats = new LinkedHashMap<>();
        stats.put("totalUsers", userList.size());
        stats.put("totalEvents", eventLinkedList.size());
        stats.put("totalRegistrations", getAllRegistrations().size());
        stats.put("pendingQueue", pendingQueue.size());
        return stats;
    }

    public void updateUserEmail(int userId, String newEmail) {
        findUserById(userId).ifPresent(u -> {
            emailSet.remove(u.getEmail());
            u.setEmail(newEmail);
            emailSet.add(newEmail);
        });
    }
}
