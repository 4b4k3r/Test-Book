package com.jm.threads;

public class JoinThreadsCorrect
{
    public static void test() throws Exception
    {
        long start = System.currentTimeMillis();
        JoinThreadsIncorrect.ThreadExample joinThreadExample = new JoinThreadsIncorrect.ThreadExample("SleepingThread");
        joinThreadExample.start();
        joinThreadExample.join(1000);
        System.out.println("Correct time was " + (System.currentTimeMillis() - start) + "ms at " + joinThreadExample.getC());
    }
}


