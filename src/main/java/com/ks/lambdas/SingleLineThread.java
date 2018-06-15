package com.ks.lambdas;

import javax.swing.*;
import java.util.HashSet;

public class SingleLineThread {

    Timer timer = new Timer(3000, e -> comportamientoHilo());
    private Thread thread = new Thread(this::comportamientoHilo);

    private HashSet<String> hashSet = new HashSet<>();


    public void startThread() {

        Thread thread = new Thread(() -> {
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
        });

        thread.start();
        this.thread.start();
        timer.start();

        hashSet.stream()

                .map(String::toUpperCase)

                .filter(varString -> varString.startsWith("HOLA"))

                .forEach(varString -> System.out.println(varString));
    }

    private void comportamientoHilo() {
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
    }
}
