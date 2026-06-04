package com.ragulsj.eventmanagement.service;

import com.ragulsj.eventmanagement.exception.*;
import com.ragulsj.eventmanagement.filehandling.FileHandlingService;
import com.ragulsj.eventmanagement.functional.FunctionalService;
import com.ragulsj.eventmanagement.jdbc.JdbcService;
import com.ragulsj.eventmanagement.model.*;
import com.ragulsj.eventmanagement.multithreading.ThreadPoolManager;
import com.ragulsj.eventmanagement.util.*;

import java.util.Scanner;

public class MenuHandler {

    private final Scanner scanner;
    private final UserService userService;
    private final EventService eventService;
    private final RegistrationService registrationService;
    private final FunctionalService functionalService;
    private final FileHandlingService fileHandlingService;
    private final JdbcService jdbcService;

    public MenuHandler(Scanner scanner, UserService userService, EventService eventService,
                       RegistrationService registrationService, FunctionalService functionalService,
                       FileHandlingService fileHandlingService, JdbcService jdbcService) {
        this.scanner = scanner;
        this.userService = userService;
        this.eventService = eventService;
        this.registrationService = registrationService;
        this.functionalService = functionalService;
        this.fileHandlingService = fileHandlingService;
        this.jdbcService = jdbcService;
    }

    public void showMainMenu() {
        boolean running = true;
        while (running) {
            ConsoleUtil.printBanner();
            System.out.println();
            System.out.println("  1. User Management");
            System.out.println("  2. Event Management");
            System.out.println("  3. Registration Management");
            System.out.println("  4. Statistics & Reports");
            System.out.println("  5. File Operations");
            System.out.println("  6. Java Feature Demos");
            System.out.println("  0. Exit");
            System.out.println();
            int choice = ConsoleUtil.readInt(scanner, "  Select option: ");
            switch (choice) {
                case 1 -> showUserMenu();
                case 2 -> showEventMenu();
                case 3 -> showRegistrationMenu();
                case 4 -> showStatisticsMenu();
                case 5 -> showFileMenu();
                case 6 -> showDemoMenu();
                case 0 -> {
                    running = false;
                    ConsoleUtil.printInfo("Shutting down...");
                    ThreadPoolManager.getInstance().shutdown();
                }
                default -> ConsoleUtil.printError("Invalid option. Please select 0-6.");
            }
        }
    }

    private void showUserMenu() {
        boolean back = false;
        while (!back) {
            ConsoleUtil.printHeader("User Management");
            System.out.println("  1. Add User");
            System.out.println("  2. View All Users");
            System.out.println("  3. Search Users");
            System.out.println("  4. View User Details");
            System.out.println("  5. Update User");
            System.out.println("  6. Delete User");
            System.out.println("  0. Back");
            int choice = ConsoleUtil.readInt(scanner, "\n  Select: ");
            switch (choice) {
                case 1 -> handleAddUser();
                case 2 -> { userService.printAllUsers(); ConsoleUtil.pause(scanner); }
                case 3 -> handleSearchUsers();
                case 4 -> handleViewUserDetails();
                case 5 -> handleUpdateUser();
                case 6 -> handleDeleteUser();
                case 0 -> back = true;
                default -> ConsoleUtil.printError("Invalid option.");
            }
        }
    }

    private void handleAddUser() {
        ConsoleUtil.printHeader("Add New User");
        try {
            String name  = ConsoleUtil.readString(scanner, "  Full Name  : ");
            String email = ConsoleUtil.readString(scanner, "  Email      : ");
            String phone = ConsoleUtil.readString(scanner, "  Phone      : ");
            int    age   = ConsoleUtil.readInt(scanner,    "  Age        : ");
            String city  = ConsoleUtil.readString(scanner, "  City       : ");
            User user = userService.addUser(name, email, phone, age, city);
            ConsoleUtil.printSuccess("User added! ID: " + user.getUserId());
        } catch (ValidationException e) {
            ConsoleUtil.printError(e.toString());
            AppLogger.error("Add user failed", e);
        }
        ConsoleUtil.pause(scanner);
    }

    private void handleSearchUsers() {
        String kw = ConsoleUtil.readString(scanner, "  Search keyword: ");
        var results = userService.searchUsers(kw);
        if (results.isEmpty()) {
            ConsoleUtil.printWarning("No users found for: " + kw);
        } else {
            results.forEach(u -> System.out.println(u.toDisplayString()));
        }
        ConsoleUtil.pause(scanner);
    }

    private void handleViewUserDetails() {
        int id = ConsoleUtil.readInt(scanner, "  User ID: ");
        try {
            userService.printUserDetails(id);
            var regs = registrationService.getRegistrationsForUser(id);
            System.out.println("Registrations (" + regs.size() + "):");
            regs.forEach(r -> System.out.println("  - " + r.getEventName() + " | " + r.getAttendanceStatus()));
        } catch (UserNotFoundException e) {
            ConsoleUtil.printError(e.toString());
        }
        ConsoleUtil.pause(scanner);
    }

    private void handleUpdateUser() {
        int id = ConsoleUtil.readInt(scanner, "  User ID to update: ");
        try {
            userService.printUserDetails(id);
            ConsoleUtil.printInfo("Leave blank to skip a field.");
            String name  = ConsoleUtil.readString(scanner, "  New Name  (blank=skip): ");
            String email = ConsoleUtil.readString(scanner, "  New Email (blank=skip): ");
            String phone = ConsoleUtil.readString(scanner, "  New Phone (blank=skip): ");
            String ageStr = ConsoleUtil.readString(scanner, "  New Age   (0=skip)   : ");
            String city  = ConsoleUtil.readString(scanner, "  New City  (blank=skip): ");
            int age = ageStr.isBlank() ? 0 : Integer.parseInt(ageStr);
            userService.updateUser(id,
                    name.isBlank() ? null : name,
                    email.isBlank() ? null : email,
                    phone.isBlank() ? null : phone,
                    age, city.isBlank() ? null : city);
            ConsoleUtil.printSuccess("User updated successfully.");
        } catch (UserNotFoundException | ValidationException e) {
            ConsoleUtil.printError(e.toString());
        } catch (NumberFormatException e) {
            ConsoleUtil.printError("Invalid age input.");
        }
        ConsoleUtil.pause(scanner);
    }

    private void handleDeleteUser() {
        int id = ConsoleUtil.readInt(scanner, "  User ID to delete: ");
        try {
            userService.printUserDetails(id);
            if (ConsoleUtil.confirm(scanner, "  Confirm deletion?")) {
                userService.deleteUser(id);
                ConsoleUtil.printSuccess("User deleted.");
            } else {
                ConsoleUtil.printInfo("Deletion cancelled.");
            }
        } catch (UserNotFoundException e) {
            ConsoleUtil.printError(e.toString());
        }
        ConsoleUtil.pause(scanner);
    }

    private void showEventMenu() {
        boolean back = false;
        while (!back) {
            ConsoleUtil.printHeader("Event Management");
            System.out.println("  1. Add Event");
            System.out.println("  2. View All Events");
            System.out.println("  3. View Upcoming Events");
            System.out.println("  4. View Event Details");
            System.out.println("  5. Update Event Status");
            System.out.println("  6. Delete Event");
            System.out.println("  0. Back");
            int choice = ConsoleUtil.readInt(scanner, "\n  Select: ");
            switch (choice) {
                case 1 -> handleAddEvent();
                case 2 -> { eventService.printAllEvents(); ConsoleUtil.pause(scanner); }
                case 3 -> { printUpcomingEvents(); ConsoleUtil.pause(scanner); }
                case 4 -> handleViewEventDetails();
                case 5 -> handleUpdateEventStatus();
                case 6 -> handleDeleteEvent();
                case 0 -> back = true;
                default -> ConsoleUtil.printError("Invalid option.");
            }
        }
    }

    private void handleAddEvent() {
        ConsoleUtil.printHeader("Add New Event");
        try {
            String name   = ConsoleUtil.readString(scanner, "  Event Name  : ");
            System.out.println("  Categories  : " + java.util.Arrays.toString(Event.Category.values()));
            String catStr = ConsoleUtil.readString(scanner, "  Category    : ").toUpperCase();
            Event.Category cat = Event.Category.valueOf(catStr);
            String date   = ConsoleUtil.readString(scanner, "  Date (YYYY-MM-DD): ");
            String time   = ConsoleUtil.readString(scanner, "  Time (HH:MM)     : ");
            String loc    = ConsoleUtil.readString(scanner, "  Location    : ");
            int seats     = ConsoleUtil.readInt(scanner,    "  Total Seats : ");
            double fee    = ConsoleUtil.readDouble(scanner, "  Fee (0=Free): ");
            String org    = ConsoleUtil.readString(scanner, "  Organizer   : ");
            String desc   = ConsoleUtil.readString(scanner, "  Description : ");
            Event event = eventService.addEvent(name, cat, date, time, loc, seats, fee, org, desc);
            ConsoleUtil.printSuccess("Event added! ID: " + event.getEventId());
        } catch (ValidationException | IllegalArgumentException e) {
            ConsoleUtil.printError("Add event failed: " + e.getMessage());
        }
        ConsoleUtil.pause(scanner);
    }

    private void printUpcomingEvents() {
        var events = eventService.getUpcomingEvents();
        if (events.isEmpty()) { ConsoleUtil.printWarning("No upcoming events."); return; }
        System.out.printf("%n%-5s %-30s %-12s %-12s %-6s%n", "ID", "NAME", "DATE", "CATEGORY", "SEATS");
        ConsoleUtil.printDivider();
        events.forEach(e -> System.out.printf("%-5d %-30s %-12s %-12s %-6d%n",
                e.getEventId(), e.getEventName(), e.getEventDate(), e.getCategory(), e.getAvailableSeats()));
    }

    private void handleViewEventDetails() {
        int id = ConsoleUtil.readInt(scanner, "  Event ID: ");
        try {
            eventService.printEventDetails(id);
            System.out.println("Registered users: " + registrationService.getRegistrationsForEvent(id).size());
        } catch (EventNotFoundException e) {
            ConsoleUtil.printError(e.toString());
        }
        ConsoleUtil.pause(scanner);
    }

    private void handleUpdateEventStatus() {
        int id = ConsoleUtil.readInt(scanner, "  Event ID: ");
        System.out.println("  Statuses: " + java.util.Arrays.toString(Event.Status.values()));
        String statusStr = ConsoleUtil.readString(scanner, "  New Status: ").toUpperCase();
        try {
            Event.Status status = Event.Status.valueOf(statusStr);
            eventService.updateEventStatus(id, status);
            ConsoleUtil.printSuccess("Event status updated.");
        } catch (EventNotFoundException e) {
            ConsoleUtil.printError(e.toString());
        } catch (IllegalArgumentException e) {
            ConsoleUtil.printError("Invalid status: " + statusStr);
        }
        ConsoleUtil.pause(scanner);
    }

    private void handleDeleteEvent() {
        int id = ConsoleUtil.readInt(scanner, "  Event ID to delete: ");
        try {
            eventService.printEventDetails(id);
            if (ConsoleUtil.confirm(scanner, "  Confirm deletion?")) {
                eventService.deleteEvent(id);
                ConsoleUtil.printSuccess("Event deleted.");
            }
        } catch (EventNotFoundException | RegistrationException e) {
            ConsoleUtil.printError(e.toString());
        }
        ConsoleUtil.pause(scanner);
    }

    private void showRegistrationMenu() {
        boolean back = false;
        while (!back) {
            ConsoleUtil.printHeader("Registration Management");
            System.out.println("  1. Register for Event");
            System.out.println("  2. Cancel Registration");
            System.out.println("  3. View All Registrations");
            System.out.println("  4. View Registrations by Event");
            System.out.println("  5. View My Registrations");
            System.out.println("  0. Back");
            int choice = ConsoleUtil.readInt(scanner, "\n  Select: ");
            switch (choice) {
                case 1 -> handleRegister();
                case 2 -> handleCancelRegistration();
                case 3 -> { registrationService.printAllRegistrations(); ConsoleUtil.pause(scanner); }
                case 4 -> handleViewByEvent();
                case 5 -> handleViewByUser();
                case 0 -> back = true;
                default -> ConsoleUtil.printError("Invalid option.");
            }
        }
    }

    private void handleRegister() {
        ConsoleUtil.printHeader("Register for Event");
        try {
            userService.printAllUsers();
            int userId = ConsoleUtil.readInt(scanner, "\n  User ID : ");
            eventService.printAllEvents();
            int eventId = ConsoleUtil.readInt(scanner, "\n  Event ID: ");
            Registration reg = registrationService.registerUserForEvent(userId, eventId);
            ConsoleUtil.printSuccess("Registration confirmed! ID: " + reg.getRegistrationId());
        } catch (UserNotFoundException | EventNotFoundException | RegistrationException e) {
            ConsoleUtil.printError(e.toString());
            AppLogger.error("Registration failed", e);
        }
        ConsoleUtil.pause(scanner);
    }

    private void handleCancelRegistration() {
        ConsoleUtil.printHeader("Cancel Registration");
        try {
            int userId  = ConsoleUtil.readInt(scanner, "  User ID : ");
            int eventId = ConsoleUtil.readInt(scanner, "  Event ID: ");
            if (ConsoleUtil.confirm(scanner, "  Confirm cancellation?")) {
                registrationService.cancelRegistration(userId, eventId);
                ConsoleUtil.printSuccess("Registration cancelled.");
            }
        } catch (UserNotFoundException | EventNotFoundException | RegistrationException e) {
            ConsoleUtil.printError(e.toString());
        }
        ConsoleUtil.pause(scanner);
    }

    private void handleViewByEvent() {
        int eventId = ConsoleUtil.readInt(scanner, "  Event ID: ");
        registrationService.printRegistrationsForEvent(eventId);
        ConsoleUtil.pause(scanner);
    }

    private void handleViewByUser() {
        int userId = ConsoleUtil.readInt(scanner, "  User ID: ");
        var regs = registrationService.getRegistrationsForUser(userId);
        if (regs.isEmpty()) ConsoleUtil.printWarning("No registrations found.");
        else regs.forEach(r -> System.out.println(r.toDisplayString()));
        ConsoleUtil.pause(scanner);
    }

    private void showStatisticsMenu() {
        ConsoleUtil.printHeader("Statistics & Reports");
        try {
            var users = userService.getAllUsers();
            var events = eventService.getAllEvents();
            var regs = registrationService.getAllRegistrations();

            System.out.printf("%n  Total Users        : %d%n", userService.getTotalUsers());
            System.out.printf("  Total Events       : %d%n", eventService.getTotalEvents());
            System.out.printf("  Total Registrations: %d%n", registrationService.getTotalRegistrations());
            System.out.printf("  Available Seats    : %d%n", eventService.getTotalAvailableSeats());

            functionalService.printStatistics(events, users, regs);

            System.out.println("\n  Recursive Factorial(5) = " + UserService.factorial(5));
            System.out.println("  Recursive Fibonacci(8) = " + UserService.fibonacci(8));

        } catch (Exception e) {
            ConsoleUtil.printError("Statistics error: " + e.getMessage());
        }
        ConsoleUtil.pause(scanner);
    }

    private void showFileMenu() {
        boolean back = false;
        while (!back) {
            ConsoleUtil.printHeader("File Operations");
            System.out.println("  1. Export Users to Text File");
            System.out.println("  2. Export Events to Text File");
            System.out.println("  3. Serialize Users");
            System.out.println("  4. Deserialize Users");
            System.out.println("  5. Serialize Events");
            System.out.println("  6. Deserialize Events");
            System.out.println("  0. Back");
            int choice = ConsoleUtil.readInt(scanner, "\n  Select: ");
            switch (choice) {
                case 1 -> { fileHandlingService.exportUsersToText(userService.getAllUsers()); ConsoleUtil.printSuccess("Exported to data/users.txt"); }
                case 2 -> { fileHandlingService.exportEventsToText(eventService.getAllEvents()); ConsoleUtil.printSuccess("Exported to data/events.txt"); }
                case 3 -> { fileHandlingService.serializeUsers(userService.getAllUsers()); ConsoleUtil.printSuccess("Users serialized."); }
                case 4 -> { var u = fileHandlingService.deserializeUsers(); ConsoleUtil.printSuccess("Deserialized " + u.size() + " users."); }
                case 5 -> { fileHandlingService.serializeEvents(eventService.getAllEvents()); ConsoleUtil.printSuccess("Events serialized."); }
                case 6 -> { var e = fileHandlingService.deserializeEvents(); ConsoleUtil.printSuccess("Deserialized " + e.size() + " events."); }
                case 0 -> back = true;
                default -> ConsoleUtil.printError("Invalid option.");
            }
            if (choice != 0) ConsoleUtil.pause(scanner);
        }
    }

    private void showDemoMenu() {
        boolean back = false;
        while (!back) {
            ConsoleUtil.printHeader("Java Feature Demonstrations");
            System.out.println("  1. Arrays & Strings Demo");
            System.out.println("  2. Type Casting Demo");
            System.out.println("  3. Java Records Demo (Java 17)");
            System.out.println("  4. Text Blocks Demo (Java 17)");
            System.out.println("  5. Functional / Streams Demo");
            System.out.println("  6. Multithreading Demo");
            System.out.println("  7. JDBC Connection Test");
            System.out.println("  8. Network Connectivity Test");
            System.out.println("  9. Pattern Matching Demo");
            System.out.println("  0. Back");
            int choice = ConsoleUtil.readInt(scanner, "\n  Select: ");
            switch (choice) {
                case 1 -> { eventService.demonstrateArrays(); ConsoleUtil.pause(scanner); }
                case 2 -> { eventService.demonstrateTypeCasting(); ConsoleUtil.pause(scanner); }
                case 3 -> demoRecords();
                case 4 -> demoTextBlocks();
                case 5 -> { functionalService.printStatistics(eventService.getAllEvents(), userService.getAllUsers(), registrationService.getAllRegistrations()); ConsoleUtil.pause(scanner); }
                case 6 -> { ThreadPoolManager.demonstrateSynchronization(); ConsoleUtil.printSuccess("Threads launched (check log)."); ConsoleUtil.pause(scanner); }
                case 7 -> { boolean ok = jdbcService.testConnection(); System.out.println(ok ? ConsoleUtil.GREEN + "  DB Connected." + ConsoleUtil.RESET : ConsoleUtil.YELLOW + "  DB not available (run in standalone mode)." + ConsoleUtil.RESET); ConsoleUtil.pause(scanner); }
                case 8 -> demoNetwork();
                case 9 -> demoPatternMatching();
                case 0 -> back = true;
                default -> ConsoleUtil.printError("Invalid option.");
            }
        }
    }

    private void demoRecords() {
        ConsoleUtil.printHeader("Java Records Demo");
        EventRecord rec = new EventRecord(99, "Demo Event", "Technology",
                java.time.LocalDate.now().plusDays(10), "Chennai", 299.0);
        System.out.println("Record     : " + rec);
        System.out.println("Name       : " + rec.name());
        System.out.println("Fee        : " + rec.formattedFee());
        System.out.println("Upcoming?  : " + rec.isUpcoming());
        ConsoleUtil.pause(scanner);
    }

    private void demoTextBlocks() {
        ConsoleUtil.printHeader("Text Blocks Demo (Java 17)");
        String json = """
                {
                  "system": "Community Event Management System",
                  "author": "Ragul SJ",
                  "email": "sjragul555@gmail.com",
                  "version": "1.0.0"
                }
                """;
        System.out.println("JSON Text Block:");
        System.out.println(json);

        String html = """
                <html>
                  <body>
                    <h1>CivicPulse Portal</h1>
                    <p>Event registration system by Ragul SJ</p>
                  </body>
                </html>
                """;
        System.out.println("HTML Text Block:");
        System.out.println(html);
        ConsoleUtil.pause(scanner);
    }

    private void demoNetwork() {
        ConsoleUtil.printHeader("Network Demo");
        NetworkService ns = new NetworkService();
        System.out.println("Server Time : " + ns.getServerTime());
        System.out.println("Checking connectivity...");
        boolean online = ns.checkConnectivity();
        System.out.println("Online      : " + online);
        if (online) {
            System.out.println("Fetching from public API...");
            String result = ns.fetchFromUrl("https://httpbin.org/get");
            System.out.println(result.length() > 200 ? result.substring(0, 200) + "..." : result);
        }
        ConsoleUtil.pause(scanner);
    }

    private void demoPatternMatching() {
        ConsoleUtil.printHeader("Pattern Matching Demo (Java 17+)");
        Object[] objects = {"CivicPulse", 42, 3.14, true, null,
                new User(0, "Demo", "d@e.com", "000", 20, "City"),
                new Event()};
        for (Object obj : objects) {
            String desc = switch (obj) {
                case String s  -> "String of length " + s.length() + ": \"" + s + "\"";
                case Integer i -> "Integer: " + i;
                case Double d  -> "Double: " + d;
                case Boolean b -> "Boolean: " + b;
                case User u    -> "User: " + u.getName();
                case Event e   -> "Event: " + (e.getEventName() == null ? "(unnamed)" : e.getEventName());
                case null      -> "null reference";
                default        -> "Unknown: " + obj.getClass().getSimpleName();
            };
            System.out.println("  " + desc);
        }
        ConsoleUtil.pause(scanner);
    }
}
