package com.ks.Callable;

import java.util.concurrent.Callable;

public class CallableHilo implements Callable<Integer>
{

    @Override
    public Integer call() throws Exception
    {
        int total = 0;
        for (int i = 0; i < 10; i++)
        {
            System.out.println("Ejecutando Hilo " + Thread.currentThread().getName());
            total += i;
            try
            {
                Thread.sleep(250);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        return total;
    }
}
