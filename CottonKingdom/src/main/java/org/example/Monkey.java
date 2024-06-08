package org.example;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Monkey implements Runnable {

    private final GlobalClock clock;

    private final int id;
    private final Semaphore sem;
    private final Farm farm;
    private boolean hasAccessory;
    private boolean natureBoost;

    public Monkey(GlobalClock clock, int id, Semaphore sem, Farm farm) {
        this.clock = clock;
        this.id = id;
        this.sem = sem;
        this.farm = farm;
        this.hasAccessory = false;
        this.natureBoost = false;
    }

    public void addAccessory() {
        this.hasAccessory = true;
        this.natureBoost = false;
        System.out.printf("Monkey %d has been given an accessory.%n", id);
    }

    public void removeAccessory() {
        if (hasAccessory) {
            this.hasAccessory = false;
            this.natureBoost = true;
            System.out.printf("Monkey %d feels the animal in them and connects with nature. Big boost!%n", id);
        }
    }

    @Override
    public void run() {


        while (true) {
            try {
//                System.out.print("molqqqq raboti%n");
                sem.acquire();

                synchronized (farm) {
                    while (!GlobalClock.getInstance().isBusinessHours()) {
                        farm.wait();
                    }

                    Land land = farm.peekNextLand();

                    Duration waitTime = Duration.ofSeconds(new Random().nextInt(2, 5));
                    System.out.printf("Monkey %d looks at Land for %d seconds%n", id, waitTime.toSeconds());
                    Thread.sleep(waitTime.toMillis());

                    if (new Random().nextBoolean()) {
                        System.out.printf("Monkey %d starts working on Land%n", id);
                        farm.assignLand(land);
                    } else {
                        System.out.printf("Monkey %d declines Land%n", id);
                        sem.release();
                        continue;
                    }
                }

                int processTime = hasAccessory ? 3 : (natureBoost ? 1 : 5); // Example process times based on boosts
                Thread.sleep(Duration.ofSeconds(processTime).toMillis());
                System.out.printf("Land done by Monkey %d%n", id);

                sem.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
