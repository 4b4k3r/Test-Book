package com.ks;

import com.ks.Threads.TestThreads;

public class App {
    public static void main(String[] args) {
        TestThreads testThreads = new TestThreads();
        testThreads.AgregarCola();
        testThreads.InsertarBd();
    }
}
