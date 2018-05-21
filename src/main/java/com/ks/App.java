package com.ks;

import com.ks.Threads.TestThreads;

public class App {
    public static void main(String[] args) {
<<<<<<< HEAD
=======
        //new Thread(new TestThreads()).run();
>>>>>>> b2e15004b5a25e44c59b73f2462150c337780e92
        TestThreads testThreads = new TestThreads();
        testThreads.AgregarCola();
        testThreads.InsertarBd();
    }
}
