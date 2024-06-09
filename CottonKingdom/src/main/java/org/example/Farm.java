package org.example;

import java.util.*;
import java.util.concurrent.*;

public class Farm {
    private Queue<Land> lands;
    private Semaphore sem;
    private GlobalClock globalClock;
    private int money;

    public Farm(int numberOfLands, int numberOfMonkeys, int initialMoney) {
        this.money = initialMoney;
        lands = new LinkedList<>();
        int landPrice = 100;
        int monkeyPrice = 150;
        for (int i = 0; i < numberOfLands; i++) {
            Land land = new Land();
            land.plant();
            land.setId(i + 1);
            land.setDaysToPacking(new Random().nextInt(1, 3));
            String string = land.toString();
            System.out.println(string);
            deductMoney(landPrice);
            lands.add(land);
        }

        sem = new Semaphore(numberOfMonkeys);
        globalClock = GlobalClock.getInstance();

        new Thread(this::runGlobalClock).start();
        deductMoney(monkeyPrice*numberOfMonkeys);
        for (int i = 0; i < numberOfMonkeys; i++) {
            new Thread(new Monkey(globalClock, i , sem, this)).start();
        }
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

    public synchronized int getMoney() {
        return money;
    }

    public synchronized void addMoney(int amount) {
        this.money += amount;
    }

    public synchronized void deductMoney(int amount) {
        this.money -= amount;
    }

    public synchronized int getNumberOfLands() {
        return lands.size();
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Let's start: enter monkeys and lands you want");
        int monkeys = scanner.nextInt();
        int lands = scanner.nextInt();
        new Farm(lands, monkeys, 1000);
    }
}
