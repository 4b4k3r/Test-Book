package com.ks.PadreConHijos;

public interface Humano {

    default void caminar(){
        System.out.println("Puedo andar en dos piernas");
    }
    default void hablar(){
        System.out.println("Puedo comunicarme verbalmente con alguien");
    }
    void comer();
    void dormir();
    void sexo();
    void piel();
    void ojos();
}
