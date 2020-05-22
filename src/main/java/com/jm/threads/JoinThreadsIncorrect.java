package com.jm.threads;

public class JoinThreadsIncorrect
{
    public static void test() throws Exception
    {
        long start = System.currentTimeMillis();
        ThreadExample joinThreadExample = new ThreadExample("ExampleThread");
        joinThreadExample.start();

        while (!joinThreadExample.isFinished() && (System.currentTimeMillis() - start) < 1000)
        {
            Thread.sleep(5);
        }

        System.out.println("Bad time was " + (System.currentTimeMillis() - start) + "ms at " + joinThreadExample.getC());
    }

    public static class ThreadExample extends Thread
    {
        private boolean finished = false;
        private int c = 0;

        public ThreadExample(String name)
        {
            super(name);
        }

        @Override
        public void run()
        {
            try
            {
                for (int i = 0; i < 99999; i++)
                {
                    Integer a = (i * i) - i + 1;

                    for (int j = 0; j < 99999; j++)
                    {
                        Integer b = (j * j) - j + 1;
                    }
                    c++;
                }

                finished = true;
            }
            catch (Exception e)
            {
                throw new RuntimeException("Error inesperado al pausar hilo");
            }
        }

        public boolean isFinished()
        {
            return finished;
        }

        public int getC()
        {
            return c;
        }
    }
}


