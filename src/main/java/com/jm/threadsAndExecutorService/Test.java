package com.jm.threadsAndExecutorService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Test {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {
        new Test().start();

        long timeoutOperation = System.currentTimeMillis() + 5000;
        while (timeoutOperation > System.currentTimeMillis()) {
        }

        System.exit(0);
    }

    private void start() {
        CallableThread callableThread = new CallableThread();
        FutureThread futureThread = new FutureThread();
        RunnableThread runnableThread = new RunnableThread();
        SimpleThread simpleThread = new SimpleThread();

        try {
            System.out.println(executor.submit(callableThread).get(10000, TimeUnit.MILLISECONDS));
        } catch (TimeoutException tow) {
            System.out.println("Error waiting a thread response");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            System.out.println(futureThread.get(10000, TimeUnit.MILLISECONDS));
        } catch (TimeoutException tow) {
            System.out.println("Error waiting a thread response");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            System.out.println(executor.submit(runnableThread).get(10000, TimeUnit.MILLISECONDS));
        } catch (TimeoutException tow) {
            System.out.println("Error waiting a thread response");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            System.out.println(executor.submit(simpleThread).get(10000, TimeUnit.MILLISECONDS));
        } catch (TimeoutException tow) {
            System.out.println("Error waiting a thread response");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
