package com.jm.threads;

public class CatchingErrors implements Runnable
{
    public static void test() throws InterruptedException
    {
        Thread thread = new Thread(new CatchingErrors(), "CatchingErrorsThread");
        thread.setUncaughtExceptionHandler((tread, throwable) -> System.out.println("Some fatal error was happen at " + thread.getName() + " for: " + throwable.getMessage()));
        thread.start();
        Thread.sleep(3000);
    }

    @Override
    public void run()
    {
        throw new RuntimeException("Sorry about that, is just for a test ...");
    }
}
