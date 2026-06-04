package com.ragulsj.eventmanagement.service;

import com.ragulsj.eventmanagement.collection.CollectionManager;
import com.ragulsj.eventmanagement.model.Event;
import com.ragulsj.eventmanagement.model.User;
import com.ragulsj.eventmanagement.util.AppLogger;
import com.ragulsj.eventmanagement.util.IdGenerator;

import java.time.LocalDate;
import java.time.LocalTime;

public class SeedDataService {

    private final CollectionManager cm;

    public SeedDataService(CollectionManager cm) {
        this.cm = cm;
    }

    public void loadSeedData() {
        try {
            IdGenerator.initUserCounter(0);
            IdGenerator.initEventCounter(0);
            IdGenerator.initRegistrationCounter(0);

            User u1 = new User(1, "Ragul SJ", "sjragul555@gmail.com", "9840000001", 24, "Chennai");
            User u2 = new User(2, "Priya Krishnan", "priya.k@example.com", "9840000002", 29, "Chennai");
            User u3 = new User(3, "Arjun Mehta", "arjun.m@example.com", "9840000003", 32, "Coimbatore");
            User u4 = new User(4, "Sneha Ramesh", "sneha.r@example.com", "9840000004", 26, "Chennai");
            User u5 = new User(5, "Vikram Nair", "vikram.n@example.com", "9840000005", 35, "Madurai");
            User u6 = new User(6, "Divya Suresh", "divya.s@example.com", "9840000006", 22, "Chennai");
            User u7 = new User(7, "Karthik Babu", "karthik.b@example.com", "9840000007", 27, "Trichy");
            User u8 = new User(8, "Anitha Selvan", "anitha.s@example.com", "9840000008", 31, "Chennai");

            for (User u : new User[]{u1, u2, u3, u4, u5, u6, u7, u8}) {
                cm.addUser(u);
            }
            IdGenerator.initUserCounter(8);

            Event e1 = new Event(1, "Marina Heritage Walk", Event.Category.CULTURAL,
                    LocalDate.now().plusDays(20), LocalTime.of(6, 30),
                    "Marina Beach, Chennai", 100, 0.0, "Chennai Heritage Trust",
                    "Guided heritage walk along Marina Beach.");
            Event e2 = new Event(2, "AI for Everyone Workshop", Event.Category.TECHNOLOGY,
                    LocalDate.now().plusDays(33), LocalTime.of(10, 0),
                    "TIDEL Park, OMR", 150, 499.0, "TechChennai Community",
                    "Hands-on intro to AI concepts.");
            Event e3 = new Event(3, "Sunrise Yoga Session", Event.Category.WELLNESS,
                    LocalDate.now().plusDays(26), LocalTime.of(5, 45),
                    "Nandanam Park, Adyar", 60, 150.0, "Wellness Chennai",
                    "Outdoor yoga for all skill levels.");
            Event e4 = new Event(4, "South Indian Food Carnival", Event.Category.FOOD,
                    LocalDate.now().plusDays(40), LocalTime.of(11, 0),
                    "Express Avenue Mall", 400, 299.0, "Flavours of South India",
                    "50+ stalls of authentic South Indian cuisine.");
            Event e5 = new Event(5, "Carnatic Fusion Night", Event.Category.MUSIC,
                    LocalDate.now().plusDays(47), LocalTime.of(19, 0),
                    "Music Academy, TTK Road", 300, 200.0, "Raaga Collective",
                    "Classical ragas blended with jazz and world music.");

            for (Event e : new Event[]{e1, e2, e3, e4, e5}) {
                cm.addEvent(e);
            }
            IdGenerator.initEventCounter(5);

            AppLogger.info("Seed data loaded: 8 users, 5 events.");
        } catch (Exception e) {
            AppLogger.error("Seed data loading failed", e);
        }
    }
}
