package com.ks.PadreConHijos;

public interface Habilidades {
    default void cantar(){
        System.out.println("No todos tienen la habilidad de cantar");
    }
    default void jugar(){
        System.out.println("Los niños juegan mas");
    }
    default void mentir(){
        System.out.println("No hay que mentir");
    }
}
