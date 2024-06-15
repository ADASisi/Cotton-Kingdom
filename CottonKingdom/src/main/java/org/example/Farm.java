package org.example;

import java.util.*;
import java.util.concurrent.*;

public class Farm {
    private Queue<Land> lands;
    private Semaphore sem;
    private GlobalClock globalClock;
    private int money;
    private boolean needMoreLand;

    public Farm(int numberOfLands, int numberOfMonkeys, int initialMoney) {
        this.money = initialMoney;
        lands = new LinkedList<>();
        globalClock = GlobalClock.getInstance();
        Thread runnableTimer = new Thread(new RunnableTimer(10, globalClock, this::decrementDaysToPackingForAllLands));
        runnableTimer.start();
        int landPrice = 100;
        int monkeyPrice = 150;
        for (int i = 0; i < numberOfLands; i++) {
            Land land = new Land();
            land.plant();
            land.setId(i + 1);
            land.setDaysToPacking(new Random().nextInt(1, 5));
            String string = land.toString();
            System.out.println(string);
            deductMoney(landPrice);
            lands.add(land);
        }

        sem = new Semaphore(numberOfMonkeys);

        deductMoney(monkeyPrice * numberOfMonkeys);
        for (int i = 0; i < numberOfMonkeys; i++) {
            new Thread(new Monkey(globalClock, i, sem, this)).start();
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
        land.harvest();
    }

    private void decrementDaysToPackingForAllLands() {
        synchronized (this) {
            for (Land land : lands) {
                if (land.getStatus() == StatusLand.SEEDED) {
                    land.decrementDaysToPacking();
                }
            }
        }
    }

    public synchronized boolean checkLandIsFree() {
        int freeLands = 0;
        for (Land land : lands) {
            if (land.getStatus() == StatusLand.FREE) {
                freeLands++;
            }
        }
        if (freeLands == getNumberOfLands()) {
            System.out.println("All lands are free");
            needMoreLand = true;
            return true;
        } else {
            return false;
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
        int initialMoney = 1000;
        Farm farm = new Farm(lands, monkeys, initialMoney);
    }
}
