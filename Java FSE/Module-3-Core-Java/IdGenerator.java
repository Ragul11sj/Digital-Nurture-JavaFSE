package com.ragulsj.eventmanagement.util;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {

    private static final AtomicInteger userCounter = new AtomicInteger(0);
    private static final AtomicInteger eventCounter = new AtomicInteger(0);
    private static final AtomicInteger registrationCounter = new AtomicInteger(0);

    public static int nextUserId() { return userCounter.incrementAndGet(); }
    public static int nextEventId() { return eventCounter.incrementAndGet(); }
    public static int nextRegistrationId() { return registrationCounter.incrementAndGet(); }

    public static void initUserCounter(int value) { userCounter.set(value); }
    public static void initEventCounter(int value) { eventCounter.set(value); }
    public static void initRegistrationCounter(int value) { registrationCounter.set(value); }
}
