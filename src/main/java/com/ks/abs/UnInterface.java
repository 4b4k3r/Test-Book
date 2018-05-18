package com.ks.abs;

public interface UnInterface {

    void ejemplo1();

    default void ejemplo2() {
        System.out.println("Soy un abstracto con cuerpo.");
    }


}
