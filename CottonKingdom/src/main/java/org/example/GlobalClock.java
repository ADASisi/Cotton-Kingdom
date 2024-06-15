package org.example;

public class GlobalClock {
    private static GlobalClock instance;

    private static final int BUSINESS_HOUR_START = 8;
    private static final int BUSINESS_HOUR_END = 18;

    private int day = 1;
    private int hour = 7;
    private int minute = 0;

    private GlobalClock() {}

    public static GlobalClock getInstance() {
        if (instance == null) {
            instance = new GlobalClock();
        }

        return instance;
    }

    public void addMinute() {
        if (this.minute == 59) {
            this.minute = 0;
            addHour();
        } else {
            this.minute++;
        }
        this.notifyAll();
    }

    private void addHour() {
        if (this.hour == 23) {
            this.hour = 0;
            addDay();
        } else {
            this.hour++;
            if (this.hour == BUSINESS_HOUR_START) {
                this.notifyAll();
            }
        }
        System.out.println(this);
    }

    public boolean isBusinessHours() {
        return this.hour >= BUSINESS_HOUR_START && this.hour < BUSINESS_HOUR_END;
    }

    public int getMinute() {
        return minute;
    }

    private void addDay() {
        this.day++;

    }

    @Override
    public String toString() {
        return "Day %d, %02d:%02d".formatted(day, hour, minute);
    }
}
