package org.example;

public class GlobalClock {
    private int currentDay;

    public GlobalClock() {
        this.currentDay = 0;
    }

    public void incrementDay() {
        currentDay++;
        System.out.println("Day " + currentDay + " begins.");
    }

    public int getCurrentDay() {
        return currentDay;
    }
}

