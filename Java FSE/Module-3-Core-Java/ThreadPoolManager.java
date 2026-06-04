package com.ragulsj.eventmanagement.multithreading;

import com.ragulsj.eventmanagement.util.AppLogger;

import java.util.concurrent.*;

public class ThreadPoolManager {

    private static ThreadPoolManager instance;
    private final ExecutorService executorService;
    private final ScheduledExecutorService scheduler;
    private static final Object LOCK = new Object();

    private ThreadPoolManager() {
        this.executorService = Executors.newFixedThreadPool(4);
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public static ThreadPoolManager getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new ThreadPoolManager();
                }
            }
        }
        return instance;
    }

    public void sendNotification(String email, String eventName, String type) {
        executorService.submit(new NotificationThread(email, eventName, type));
    }

    public void submitTask(Runnable task) {
        executorService.submit(task);
    }

    public <T> Future<T> submitCallable(Callable<T> callable) {
        return executorService.submit(callable);
    }

    public void scheduleReminder(String email, String eventName, long delaySeconds) {
        scheduler.schedule(() -> {
            NotificationThread n = new NotificationThread(email, eventName, "REMINDER");
            n.run();
        }, delaySeconds, TimeUnit.SECONDS);
    }

    public void runSeedDataLoader(Runnable loader) {
        Thread thread = new Thread(loader, "SeedDataLoader");
        thread.setDaemon(true);
        thread.start();
        try {
            thread.join(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            AppLogger.warn("SeedDataLoader interrupted");
        }
    }

    public void shutdown() {
        executorService.shutdown();
        scheduler.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        AppLogger.info("ThreadPoolManager shut down.");
    }

    public static synchronized void demonstrateSynchronization() {
        for (int i = 1; i <= 3; i++) {
            final int taskId = i;
            Thread t = new Thread(() -> {
                synchronized (LOCK) {
                    AppLogger.debug("Synchronized task " + taskId + " executed by " + Thread.currentThread().getName());
                }
            }, "Worker-" + i);
            t.start();
        }
    }
}
