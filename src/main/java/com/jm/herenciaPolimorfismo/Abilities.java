package com.jm.herenciaPolimorfismo;

public interface Abilities {
    default void sing(){
        System.out.println("No todos tienen la habilidad de cantar");
    }
    default void play(){
        System.out.println("Los niños juegan mas");
    }
    default void lie(){
        System.out.println("No hay que mentir");
    }
}
