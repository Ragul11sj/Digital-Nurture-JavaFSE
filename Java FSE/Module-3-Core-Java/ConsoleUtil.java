package com.ragulsj.eventmanagement.util;

import java.util.Scanner;

public class ConsoleUtil {

    public static final String RESET  = "\033[0m";
    public static final String RED    = "\033[0;31m";
    public static final String GREEN  = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String CYAN   = "\033[0;36m";
    public static final String BOLD   = "\033[1m";
    public static final String WHITE  = "\033[0;37m";

    private static final String LINE  = "=".repeat(80);
    private static final String DLINE = "-".repeat(80);

    public static void printBanner() {
        System.out.println(CYAN + LINE);
        System.out.println("   COMMUNITY EVENT MANAGEMENT SYSTEM");
        System.out.println("   Developed by: Ragul SJ | sjragul555@gmail.com");
        System.out.println(LINE + RESET);
    }

    public static void printHeader(String title) {
        System.out.println("\n" + CYAN + LINE);
        System.out.printf("   %s%n", title.toUpperCase());
        System.out.println(LINE + RESET);
    }

    public static void printDivider() {
        System.out.println(WHITE + DLINE + RESET);
    }

    public static void printSuccess(String message) {
        System.out.println(GREEN + "[SUCCESS] " + message + RESET);
    }

    public static void printError(String message) {
        System.out.println(RED + "[ERROR]   " + message + RESET);
    }

    public static void printWarning(String message) {
        System.out.println(YELLOW + "[WARNING] " + message + RESET);
    }

    public static void printInfo(String message) {
        System.out.println(CYAN + "[INFO]    " + message + RESET);
    }

    public static void printTableHeader(String... headers) {
        StringBuilder sb = new StringBuilder();
        for (String h : headers) {
            sb.append(String.format("%-20s", h));
        }
        System.out.println(BOLD + sb + RESET);
        printDivider();
    }

    public static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String line = scanner.nextLine().trim();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                printError("Please enter a valid integer.");
            }
        }
    }

    public static double readDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String line = scanner.nextLine().trim();
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                printError("Please enter a valid number.");
            }
        }
    }

    public static String readString(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static boolean confirm(Scanner scanner, String prompt) {
        System.out.print(prompt + " (y/n): ");
        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("y") || input.equals("yes");
    }

    public static void pause(Scanner scanner) {
        System.out.print("\nPress ENTER to continue...");
        scanner.nextLine();
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
