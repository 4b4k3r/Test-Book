package com.jm.herenciaPolimorfismo;

public interface Human {

    default void walk(){
        System.out.println("Puedo andar en dos piernas");
    }
    default void talk(){
        System.out.println("Puedo comunicarme verbalmente con alguien");
    }
    void eat();
    void sleep();
    void gender();
    void skin();
    void eyes();
}
