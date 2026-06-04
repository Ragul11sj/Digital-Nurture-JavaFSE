package com.ragulsj.eventmanagement.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppLogger {

    private static final String LOG_FILE = "event_management.log";
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void log(String level, String message) {
        String entry = String.format("[%s] [%s] %s", LocalDateTime.now().format(FMT), level, message);
        System.err.println(entry);
        try (PrintWriter pw = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            pw.println(entry);
        } catch (IOException ignored) {}
    }

    public static void info(String message) { log("INFO ", message); }
    public static void warn(String message) { log("WARN ", message); }
    public static void error(String message) { log("ERROR", message); }
    public static void error(String message, Throwable t) {
        log("ERROR", message + " | " + t.getClass().getSimpleName() + ": " + t.getMessage());
    }
    public static void debug(String message) { log("DEBUG", message); }
}
