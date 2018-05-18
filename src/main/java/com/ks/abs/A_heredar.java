package com.ks.abs;

public class A_heredar {

    protected int contador;

    public A_heredar() {
        contador = 0;
    }


    protected void incrementaContador() {
        for (int i = 0; i < 20; i++) {
            contador++;
        }
        System.out.println("Contador: " + contador);

    }


}
