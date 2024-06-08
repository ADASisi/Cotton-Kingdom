package org.example;

import java.util.*;
import java.util.concurrent.*;

public class Farm {
    private Queue<Land> lands;
    private Semaphore sem;
    private GlobalClock globalClock;

    public Farm(int numberOfLands, int numberOfMonkeys) {
        lands = new LinkedList<>();
        for (int i = 0; i < numberOfLands; i++) {
            Land land = new Land();
            land.plant(); // Planting lands initially
            lands.add(land);
        }

        sem = new Semaphore(numberOfMonkeys);
        globalClock = GlobalClock.getInstance();



        new Thread(this::runGlobalClock).start();
        for (int i = 0; i < numberOfMonkeys; i++) {
            new Thread(new Monkey(globalClock, i + 1, sem, this)).start();
        }
//        new Thread(this::runGlobalClock).start();
    }

    public synchronized Land peekNextLand() {
        return lands.peek();
    }

    public synchronized void assignLand(Land land) {
        lands.poll();
        land.harvest();
    }

    private void runGlobalClock() {
        while (true) {
            //Thread.sleep(1000); // Simulate one minute passing
            globalClock.addMinute();
            decrementDaysToPackingForAllLands();
        }
    }

    private void decrementDaysToPackingForAllLands() {
        synchronized (this) {
            for (Land land : lands) {
                land.decrementDaysToPacking();
            }
        }
    }

    public static void main(String[] args) {
        new Farm(5, 3); // 5 lands and 3 monkeys
    }
}
