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
    private int natureBoost;

    public Monkey(GlobalClock clock, int id, Semaphore sem, Farm farm) {
        this.clock = clock;
        this.id = id;
        this.sem = sem;
        this.farm = farm;
        this.hasAccessory = new Random().nextBoolean();
        this.natureBoost = new Random().nextInt(1, 5);
        String string = toString();
        System.out.println(string);
    }

    public void addAccessory() {
        this.hasAccessory = true;
        this.natureBoost++;
        System.out.printf("Monkey %d has been given an accessory.%n", id);
    }

    public void removeAccessory() {
        if (hasAccessory) {
            this.hasAccessory = false;
            this.natureBoost--;
            System.out.printf("Monkey %d feels the animal in them and connects with nature. Big boost!%n", id);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (farm.checkLandIsFree()) {
                    System.out.printf("Monkey %d stops working as all lands are free.%n", id);
                    break;
                }

                sem.acquire();
                Land land;

                synchronized (clock) {
                    while (!GlobalClock.getInstance().isBusinessHours()) {
                        clock.wait();
                    }

                    while (true) {
                        land = farm.peekNextLand();
                        if (land == null) {
                            farm.rotateLands();
                            continue;
                        }

                        if (land.getStatus() == StatusLand.REAP) {
                            break;
                        } else {
                            farm.rotateLands();
                        }
                    }
                }

                Duration waitTime = Duration.ofSeconds(new Random().nextInt(2, 5));
                Thread.sleep(waitTime.toMillis());

                if (new Random().nextBoolean()) {
                    System.out.printf("Monkey %d starts working on Land %d%n", id, land.getId());
                    farm.assignLand(land);

                    int processTime = hasAccessory ? 3 : (natureBoost > 0 ? 1 : 5);
                    Thread.sleep(Duration.ofSeconds(processTime).toMillis());
                } else {
                    System.out.printf("Monkey %d declines Land %d%n", id, land.getId());
                    applyPenalty();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                sem.release();
            }
        }
    }

    @Override
    public String toString() {
        return "Monkey{" +
                "id=" + id +
                ", hasAccessory=" + hasAccessory +
                ", natureBoost=" + natureBoost +
                '}';
    }

    private void applyPenalty() throws InterruptedException {
        int penaltyTime = hasAccessory ? 10 : (natureBoost > 0 ? 5 : 15);
        System.out.printf("Monkey %d is penalized for %d seconds.%n", id, penaltyTime);
        Thread.sleep(Duration.ofSeconds(penaltyTime).toMillis());
    }
}
