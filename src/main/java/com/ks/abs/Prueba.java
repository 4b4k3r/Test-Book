package com.ks.abs;

public class Prueba extends A_heredar implements UnInterface {


    Prueba() {
        super();
    }

    @Override
    public void incrementaContador() {

        int iterador = 0;
        while (true) {

            contador++;
            iterador++;


            if (iterador >= 20) {
                break;
            }
        }
        System.out.println("Contador eredado: " + contador);
    }


    @Override
    public void ejemplo1() {

        //incrementaContador();

    }


}
