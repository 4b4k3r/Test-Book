package com.jm.threads;

import java.util.Date;

public class SingleLineThread {

    public static void main(String[] args) {
        new SingleLineThread().startThread();
    }

    public void startThread() {
        new Thread(this::run).start();
        new Thread(this::run).start();
        new Thread(this::run).start();
        new Thread(this::run).start();
        new Thread(this::run).start();

        Thread thread = new Thread(this::run);
        thread.setPriority(Thread.NORM_PRIORITY);
        thread.setDaemon(true);
        thread.setName("Custom thread");
        thread.start();
    }

    private void behavior() {
        System.out.println(Thread.currentThread().getName() + " running at " + new Date());
    }

    private void run() {
        long timeout = System.currentTimeMillis() + 10000;

        while (System.currentTimeMillis() < timeout) {
            behavior();

            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
