package com.jm;

import com.jm.threads.CatchingErrors;
import com.jm.threads.ComplexCalculation;
import com.jm.threads.JoinThreadsCorrect;
import com.jm.threads.JoinThreadsIncorrect;

import java.math.BigInteger;

public class App
{
    public static void main(String[] args) throws Exception
    {
        /*String a = "2";
        System.out.println(new ComplexCalculation().calculateResult(new BigInteger(a), new BigInteger(a), new BigInteger(a), new BigInteger(a)).toString());*/

        JoinThreadsIncorrect.test();
        JoinThreadsCorrect.test();
        //CatchingErrors.test();
    }
}
