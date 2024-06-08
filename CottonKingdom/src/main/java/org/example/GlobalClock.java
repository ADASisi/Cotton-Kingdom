package org.example;

public class GlobalClock {
    private static GlobalClock instance;

    private static final int BUSINESS_HOUR_START = 8;
    private static final int BUSINESS_HOUR_END = 18; // exclusive

    private int day = 1;
    private int hour = 7; // Let's start at 7 so we don't have to wait too much
    private int minute = 0;

    private GlobalClock() {}

    public static GlobalClock getInstance() {
        if (instance == null) {
            instance = new GlobalClock();
        }

        return instance;
    }

    public synchronized void addMinute() {
        if (this.minute == 1) {
            this.minute = 0;
            addHour();
        } else {
            this.minute++;
        }
        this.notifyAll(); // Notify all waiting threads after each minute increment
    }

    private synchronized void addHour() {
        if (this.hour == 23) {
            this.hour = 0;
            addDay();
        } else {
            this.hour++;
            if (this.hour == BUSINESS_HOUR_START) {
                this.notifyAll(); // Notify all waiting threads at the start of business hours
            }
        }
    }

    public synchronized boolean isBusinessHours() {
        return this.hour >= BUSINESS_HOUR_START && this.hour < BUSINESS_HOUR_END;
    }

    public int getMinute() {
        return minute;
    }

    private synchronized void addDay() {
        this.day++;
    }

    @Override
    public synchronized String toString() {
        return "Day %d, %02d:%02d".formatted(day, hour, minute);
    }
}
