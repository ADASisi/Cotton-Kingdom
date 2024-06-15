package org.example;
import java.time.Duration;

public class RunnableTimer implements Runnable {

    private final GlobalClock clock;
    private final Runnable decrementDaysToHearts;

    private final double speed;

    public RunnableTimer(double speed, GlobalClock clock, Runnable decrementDaysToHearts) {
        this.speed = speed;
        this.clock = clock;
        this.decrementDaysToHearts = decrementDaysToHearts;
    }

    @Override
    public void run() {
        while(true) {
//            System.out.println(clock);

            try {
                Thread.sleep((long) (Duration.ofSeconds(1).toMillis() / speed));
                synchronized (clock) {
                    clock.addMinute();
                    decrementDaysToHearts.run();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

