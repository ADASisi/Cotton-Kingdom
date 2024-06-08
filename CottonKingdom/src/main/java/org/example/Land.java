package org.example;

import java.util.concurrent.locks.ReentrantLock;

public class Land {
    private StatusLand status;
    private boolean enoughWater;
    private int careLevel;
    private int daysToPacking;
    private final ReentrantLock lock;

    public Land() {
        this.status = StatusLand.FREE;
        this.enoughWater = false;
        this.careLevel = 0;
        this.daysToPacking = 0;
        this.lock = new ReentrantLock();
    }

    public void plant() {
        lock.lock();
        try {
            if (status == StatusLand.FREE) {
                status = StatusLand.SEEDED;
                daysToPacking = 10; // Example value
                System.out.println("Land planted.");
            }
        } finally {
            lock.unlock();
        }
    }

    public void water() {
        lock.lock();
        try {
            if (status == StatusLand.SEEDED) {
                enoughWater = true;
                careLevel++;
                System.out.println("Land watered.");
            }
        } finally {
            lock.unlock();
        }
    }

    public void harvest() {
        lock.lock();
        try {
            if (status == StatusLand.SEEDED && daysToPacking <= 0) {
                status = StatusLand.FREE;
                enoughWater = false;
                careLevel = 0;
                System.out.println("Land harvested and reset to free.");
            }
        } finally {
            lock.unlock();
        }
    }

    public void decrementDaysToPacking() {
        lock.lock();
        try {
            if (status == StatusLand.SEEDED && daysToPacking > 0) {
                daysToPacking--;
            }
        } finally {
            lock.unlock();
        }
    }

    // Getters and setters for fields
}

