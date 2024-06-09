package org.example;

import java.util.concurrent.locks.ReentrantLock;

public class Land {
    private int id;
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
                daysToPacking = 2; // Example value
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
            if (status == StatusLand.REAP && daysToPacking <= 0) {
                status = StatusLand.FREE;
                enoughWater = false;
                careLevel = 0;
                System.out.printf("Land %d harvested and reset to free.%n", id);
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
//                System.out.printf("Days to packing decremented for land %d. Current value:%d %n",id, daysToPacking);
            }
            if(daysToPacking == 0)
            {
                status = StatusLand.REAP;
                System.out.printf("Land %d is ready to harvest%n",id);
            }
        } finally {
            lock.unlock();
        }
    }

    public StatusLand getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Land{" +
                "status=" + status +
                ", enoughWater=" + enoughWater +
                ", careLevel=" + careLevel +
                ", daysToPacking=" + daysToPacking +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setDaysToPacking(int daysToPacking) {
        this.daysToPacking = daysToPacking;
    }


}
