package org.example;

import java.util.*;
import java.util.concurrent.*;

public class Farm {
    private List<Land> lands;
    private List<Monkey> monkeys;
    private ExecutorService executor;
    private GlobalClock globalClock;

    public Farm(int numberOfLands, int numberOfMonkeys) {
        lands = new ArrayList<>();
        for (int i = 0; i < numberOfLands; i++) {
            lands.add(new Land());
        }

        monkeys = new ArrayList<>();
        for (int i = 0; i < numberOfMonkeys; i++) {
            monkeys.add(new Monkey("Monkey" + (i + 1), "Tool" + (i + 1), 5, 3));
        }

        executor = Executors.newCachedThreadPool();
        globalClock = new GlobalClock();
    }

    public void startFarmOperations() {
        for (Land land : lands) {
            land.plant();
        }

        for (int i = 0; i < monkeys.size() && i < lands.size(); i++) {
            monkeys.get(i).assignLand(lands.get(i));
            executor.submit(monkeys.get(i));
        }

        monkeys.get(0).addAccessory();
        monkeys.get(1).addAccessory();
        monkeys.get(0).removeAccessory();

        executor.submit(this::waterLands);

        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }

    private void waterLands() {
        for (Land land : lands) {
            land.water();
        }
    }

    public static void main(String[] args) {
        Farm farm = new Farm(5, 3);
        farm.startFarmOperations();
    }
}
