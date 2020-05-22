package com.jm.threads;

import java.math.BigInteger;

public class ComplexCalculation
{
    public BigInteger calculateResult(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2)
    {
        try
        {
            PowerCalculatingThread power1Thread = new PowerCalculatingThread(base1, power1);
            power1Thread.join();
            power1Thread.start();

            PowerCalculatingThread power2Thread = new PowerCalculatingThread(base2, power2);
            power2Thread.join();
            power2Thread.start();

            return power1Thread.getResult().add(power2Thread.getResult());
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private static class PowerCalculatingThread extends Thread
    {
        private BigInteger result = BigInteger.ONE;
        private BigInteger base;
        private BigInteger power;

        public PowerCalculatingThread(BigInteger base, BigInteger power)
        {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run()
        {
            result = base.pow(power.intValue());
            System.out.println(result.intValue());
        }

        public BigInteger getResult()
        {
            return result;
        }
    }
}
