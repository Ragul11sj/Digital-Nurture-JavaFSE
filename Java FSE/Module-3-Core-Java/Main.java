package com.ragulsj.eventmanagement;

import com.ragulsj.eventmanagement.collection.CollectionManager;
import com.ragulsj.eventmanagement.filehandling.FileHandlingService;
import com.ragulsj.eventmanagement.functional.FunctionalService;
import com.ragulsj.eventmanagement.jdbc.JdbcService;
import com.ragulsj.eventmanagement.multithreading.ThreadPoolManager;
import com.ragulsj.eventmanagement.service.*;
import com.ragulsj.eventmanagement.util.AppLogger;
import com.ragulsj.eventmanagement.util.ConsoleUtil;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        AppLogger.info("=== Community Event Management System starting ===");

        CollectionManager collectionManager = new CollectionManager();

        UserService         userService         = new UserService(collectionManager);
        EventService        eventService        = new EventService(collectionManager);
        RegistrationService registrationService = new RegistrationService(collectionManager);
        FunctionalService   functionalService   = new FunctionalService();
        FileHandlingService fileHandlingService = new FileHandlingService();
        JdbcService         jdbcService         = new JdbcService();
        SeedDataService     seedDataService     = new SeedDataService(collectionManager);

        ThreadPoolManager.getInstance().runSeedDataLoader(seedDataService::loadSeedData);

        Scanner scanner = new Scanner(System.in);

        MenuHandler menuHandler = new MenuHandler(
                scanner, userService, eventService, registrationService,
                functionalService, fileHandlingService, jdbcService);

        try {
            menuHandler.showMainMenu();
        } catch (Exception e) {
            ConsoleUtil.printError("Unexpected error: " + e.getMessage());
            AppLogger.error("Fatal error in main", e);
        } finally {
            scanner.close();
            ThreadPoolManager.getInstance().shutdown();
            AppLogger.info("=== Community Event Management System stopped ===");
            System.out.println(ConsoleUtil.CYAN + "\nThank you for using CivicPulse. Goodbye!" + ConsoleUtil.RESET);
        }
    }
}
