package com.jm.threadHashSincronized;

import java.util.HashMap;
import java.util.Random;

public class TestSynchronized {

    private static final HashMap<Integer, Integer> map = new HashMap<>();

    public static void main(String[] args) {
        TestSynchronized ts = new TestSynchronized();
        ts.start();
        ts.start();
        ts.start();
        ts.start();
        ts.start();
    }

    public void processing() {
        long extraTime = new Random().nextInt(10000) + 5000;
        long timeout = System.currentTimeMillis() + extraTime;
        System.out.println(Thread.currentThread().getName() + " will run for " + (extraTime / 1000) + "s");

        while (timeout > System.currentTimeMillis()) {
            try {
                synchronized (map) {
                    map.merge(new Random().nextInt(5) + 1, 1, Integer::sum);
                }

                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        synchronized (map) {
            System.out.println(Thread.currentThread().getName() + " finished with " + map.toString());
        }
    }

    public void start() {
        new Thread(this::processing).start();
    }
}
