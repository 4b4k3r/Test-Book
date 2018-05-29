package com.ks;

import com.ks.Threads.TestThreads;
public class App {

    public static void main(String[] args) {

        TestThreads testThreads = new TestThreads();
        testThreads.start();

    }
    public static String regresar(String s) {
        String cadena = "";
        int count = 0;
        while (s.length() > count) {
            cadena += s.toUpperCase().charAt(count);
            for (int i = 0; i < count; i++) {
                cadena += s.toLowerCase().charAt(count);
            }
            if (count != (s.length() - 1)) {
                cadena += "-";
            }
            count++;
        }
        return cadena;
    }
}
