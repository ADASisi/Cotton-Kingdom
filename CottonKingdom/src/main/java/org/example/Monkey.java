package org.example;
import java.util.concurrent.Semaphore;

public class Monkey implements Runnable {
    private final String name;
    private final String instrument;
    private final int timeToPickCotton;
    private Land assignedLand;
    private boolean hasAccessory;
    private boolean natureBoost;
    private static Semaphore sem;

    public Monkey(String name, String instrument, int timeToPickCotton, int age) {
        this.name = name;
        this.instrument = instrument;
        this.timeToPickCotton = timeToPickCotton;
        this.hasAccessory = false;
        this.natureBoost = false;
    }

    public void assignLand(Land land) {
        this.assignedLand = land;
    }

    public void addAccessory() {
        this.hasAccessory = true;
        this.natureBoost = false;
        System.out.println(name + " has been given an accessory.");
    }

    public void removeAccessory() {
        if (hasAccessory) {
            this.hasAccessory = false;
            this.natureBoost = true;
            System.out.println(name + " feels the animal in them and connects with nature. Big boost!");
        }
    }

    @Override
    public void run() {
        if (assignedLand != null) {
            try {
                sem.acquire();
                if (natureBoost) {
                    System.out.println(name + " is picking cotton with a big nature boost!");
                    Thread.sleep((timeToPickCotton / 2) * 1000);
                } else if (hasAccessory) {
                    System.out.println(name + " is picking cotton with an accessory.");
                    Thread.sleep(timeToPickCotton * 1000);
                } else {
                    System.out.println(name + " is picking cotton.");
                    Thread.sleep(timeToPickCotton * 1000);
                }
                assignedLand.harvest();
                System.out.println(name + " finished picking cotton.");
            } catch (InterruptedException e) {
                System.out.println(name + " was interrupted.");
            } finally {
                sem.release();
            }
        } else {
            System.out.println(name + " has no land assigned.");
        }
    }

    // Getters and setters for fields
}
