package com.jm.threadsAndExecutorService;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureThread implements Future<Integer> {

    private long extraTime = (new Random().nextInt(1000) + 9200);
    private long timeout2 = System.currentTimeMillis() + extraTime;
    private int total = 0;

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return mayInterruptIfRunning && timeout2 == 0;
    }

    @Override
    public boolean isCancelled() {
        return timeout2 == 0;
    }

    @Override
    public boolean isDone() {
        return !(timeout2 > System.currentTimeMillis());
    }

    @Override
    public Integer get() throws InterruptedException, ExecutionException {
        fill();
        while (!isDone()) {
        }
        return total;
    }

    @Override
    public Integer get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        fill();
        long timeoutFinal = unit.toMillis(timeout) + System.currentTimeMillis();
        while (timeoutFinal > System.currentTimeMillis() || !isDone()) {
            if (timeoutFinal < System.currentTimeMillis()) {
                throw new TimeoutException();
            }
        }

        return total;
    }

    private void fill() throws InterruptedException {
        System.out.println("Timeout for " + Thread.currentThread().getName() + " " + extraTime + " ms");
        while (timeout2 > System.currentTimeMillis()) {
            total += new Random().nextInt(500);
            Thread.sleep(500);
        }
    }
}
