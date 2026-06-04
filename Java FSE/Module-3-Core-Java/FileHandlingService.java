package com.ragulsj.eventmanagement.filehandling;

import com.ragulsj.eventmanagement.model.Event;
import com.ragulsj.eventmanagement.model.User;
import com.ragulsj.eventmanagement.util.AppLogger;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileHandlingService {

    private static final String DATA_DIR = "data/";
    private static final String USERS_FILE = DATA_DIR + "users.txt";
    private static final String EVENTS_FILE = DATA_DIR + "events.txt";
    private static final String USERS_SER = DATA_DIR + "users.ser";
    private static final String EVENTS_SER = DATA_DIR + "events.ser";
    private static final String REPORT_FILE = DATA_DIR + "report.txt";

    public FileHandlingService() {
        new File(DATA_DIR).mkdirs();
    }

    public void exportUsersToText(List<User> users) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE))) {
            bw.write("COMMUNITY EVENT MANAGEMENT SYSTEM - USER EXPORT");
            bw.newLine();
            bw.write("Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            bw.newLine();
            bw.write("=".repeat(80));
            bw.newLine();
            bw.write(String.format("%-5s %-25s %-30s %-15s %-5s %-15s%n",
                    "ID", "NAME", "EMAIL", "PHONE", "AGE", "CITY"));
            bw.write("-".repeat(80));
            bw.newLine();
            for (User u : users) {
                bw.write(u.toDisplayString());
                bw.newLine();
            }
            bw.write("=".repeat(80));
            bw.newLine();
            bw.write("Total Users: " + users.size());
            bw.newLine();
            AppLogger.info("Exported " + users.size() + " users to " + USERS_FILE);
        } catch (IOException e) {
            AppLogger.error("Failed to export users", e);
        }
    }

    public void exportEventsToText(List<Event> events) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(EVENTS_FILE))) {
            bw.write("COMMUNITY EVENT MANAGEMENT SYSTEM - EVENT EXPORT");
            bw.newLine();
            bw.write("Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            bw.newLine();
            bw.write("=".repeat(100));
            bw.newLine();
            for (Event e : events) {
                bw.write(e.toDisplayString());
                bw.newLine();
            }
            bw.write("Total Events: " + events.size());
            bw.newLine();
            AppLogger.info("Exported " + events.size() + " events to " + EVENTS_FILE);
        } catch (IOException e) {
            AppLogger.error("Failed to export events", e);
        }
    }

    public List<String> readTextFile(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            AppLogger.error("Failed to read file: " + filePath, e);
        }
        return lines;
    }

    public void serializeUsers(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_SER))) {
            oos.writeObject(users);
            AppLogger.info("Serialized " + users.size() + " users to " + USERS_SER);
        } catch (IOException e) {
            AppLogger.error("Serialization of users failed", e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<User> deserializeUsers() {
        File file = new File(USERS_SER);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_SER))) {
            List<User> users = (List<User>) ois.readObject();
            AppLogger.info("Deserialized " + users.size() + " users from " + USERS_SER);
            return users;
        } catch (IOException | ClassNotFoundException e) {
            AppLogger.error("Deserialization of users failed", e);
            return new ArrayList<>();
        }
    }

    public void serializeEvents(List<Event> events) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(EVENTS_SER))) {
            oos.writeObject(events);
            AppLogger.info("Serialized " + events.size() + " events to " + EVENTS_SER);
        } catch (IOException e) {
            AppLogger.error("Serialization of events failed", e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Event> deserializeEvents() {
        File file = new File(EVENTS_SER);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(EVENTS_SER))) {
            List<Event> events = (List<Event>) ois.readObject();
            AppLogger.info("Deserialized " + events.size() + " events from " + EVENTS_SER);
            return events;
        } catch (IOException | ClassNotFoundException e) {
            AppLogger.error("Deserialization of events failed", e);
            return new ArrayList<>();
        }
    }

    public void writeReport(String content) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(REPORT_FILE, true))) {
            bw.write(content);
            bw.newLine();
        } catch (IOException e) {
            AppLogger.error("Failed to write report", e);
        }
    }

    public void printFileContents(String filePath) {
        List<String> lines = readTextFile(filePath);
        if (lines.isEmpty()) {
            System.out.println("File is empty or not found: " + filePath);
            return;
        }
        lines.forEach(System.out::println);
    }
}
