package com.jm.herenciaPolimorfismo;

public class Application {
    public static void main(String[] args) {
        Father father = new Father();
        System.out.println("Padre: ");
        father.description();
        System.out.println("\nEstos son mis hijos e hijas: ");
        System.out.println("\nHijo 1");
        Father hijo1 = new Son("Me gusta cantar", "No me gusta caminar", "Solo se hablar español", 4, 9, "niña", "blanca como mi mamá", "verdes como mi papa", "No deberia mentir dice papá", "No juego mucho");
        hijo1.description();
        System.out.println("\nHijo 2");
        Father hijo2 = new Son("Odio cantar", "Me encanta caminar", "hablo español y frances", 5, 7, "niño", "blanca como mi mamá", "verdes como mi papa", "No deberia mentir dice papá", "Me encanta el foot");
        hijo2.description();
        System.out.println("\nHijo 3");
        Father hijo3 = new Son("Me gusta cantar en fiestas familiares", "No me gusta caminar me fatigo", "Solo se hablar español y un poco ingles", 3, 8, "niño", "morena como mi padre", "cafe como mi mama", "solo miento cuando es necesario", "Me gusta el americano");
        hijo3.description();
    }
}
