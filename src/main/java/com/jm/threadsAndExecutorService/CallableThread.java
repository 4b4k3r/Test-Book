package com.jm.threadsAndExecutorService;

import java.util.Random;
import java.util.concurrent.Callable;

public class CallableThread implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        long extraTime = (new Random().nextInt(1000) + 9200);
        long timeout = System.currentTimeMillis() + extraTime;
        System.out.println("Timeout for " + Thread.currentThread().getName() + " " + extraTime + " ms");

        int total = 0;

        while (timeout > System.currentTimeMillis()) {
            try {
                total += new Random().nextInt(500);
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return total;
    }
}
