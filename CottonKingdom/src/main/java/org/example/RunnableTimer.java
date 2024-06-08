package org.example;
import java.time.Duration;

public class RunnableTimer implements Runnable {

    private final GlobalClock clock;

    private final double speed;

    public RunnableTimer(double speed, GlobalClock clock) {
        this.speed = speed;
        this.clock = clock;
    }

    @Override
    public void run() {
        while(true) {
            System.out.println(clock);

            try {
                Thread.sleep((long) (Duration.ofSeconds(1).toMillis() / speed));
                synchronized (clock) {
                    clock.addMinute();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

