package com.ks.Threads;

public class TestInVariable {

    private Thread hilo;
    private int contador;


    public TestInVariable() {

        hilo = new Thread();
        contador = 0;
    }


    public void invocaHilo() {

        System.out.println("Inicia Hilo");
        if (!hilo.isAlive()) {
            hilo = new Thread(() -> {

                for (int i = 0; i < 10; i++) {
                    contador++;
                }
                System.out.println(contador);

            });
            hilo.setDaemon(false);
            hilo.start();


        }
    }


}
