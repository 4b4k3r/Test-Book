package com.ks;

import com.ks.Threads.TestInVariable;
import com.ks.Threads.TestThreads;

public class App {
    public static void main(String[] args) {
        //new Thread(new TestThreads()).run();
        TestThreads testThreads = new TestThreads();
        testThreads.AgregarCola();
        testThreads.InsertarBd();
    }
}
