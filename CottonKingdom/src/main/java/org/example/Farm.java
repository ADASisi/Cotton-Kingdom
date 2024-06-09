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
            land.plant();
            land.setId(i + 1);
            land.setDaysToPacking(new Random().nextInt(1, 3));
            String string = land.toString();
            System.out.println(string);
            lands.add(land);

        }

        sem = new Semaphore(numberOfMonkeys);
        globalClock = GlobalClock.getInstance();

        new Thread(this::runGlobalClock).start();
        for (int i = 0; i < numberOfMonkeys; i++) {
            new Thread(new Monkey(globalClock, i , sem, this)).start();
        }

//        new Thread(this::runGlobalClock).start();
    }

    public synchronized Land peekNextLand() {
        if (lands.isEmpty()) {
            return null;
        }
        return this.lands.peek();
    }

    public synchronized void rotateLands() {
        if (!lands.isEmpty()) {
            Land land = lands.poll();
            lands.offer(land);
        }
    }

    public synchronized void assignLand(Land land) {
        //lands.poll();
        land.harvest();
    }

    private void runGlobalClock() {
        while (true) {
            globalClock.addMinute();
            decrementDaysToPackingForAllLands();
        }
    }

    private void decrementDaysToPackingForAllLands() {
        synchronized (this) {
            for (Land land : lands) {
                if(land.getStatus() == StatusLand.SEEDED)
                {
                    land.decrementDaysToPacking();
                }
            }
        }
    }

    public static void main(String[] args) {
        new Farm(5, 3);
    }
}
