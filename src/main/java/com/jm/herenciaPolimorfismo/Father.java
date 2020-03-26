package com.jm.herenciaPolimorfismo;

public class Father implements Human, Abilities {
    int eatAtDay;
    int sleepAtDay;

    public Father() {
        eatAtDay = 3;
        sleepAtDay = 6;
    }

    @Override
    public void walk() {
        System.out.println("Camino muy poco");
    }

    @Override
    public void talk() {
        System.out.println("Hablo 4 idiomas");
    }

    @Override
    public void eat() {
        System.out.println("Como " + eatAtDay + " al dia");
    }

    @Override
    public void sleep() {
        System.out.println("Duermo " + sleepAtDay + " horas");
    }

    @Override
    public void gender() {
        System.out.println("Soy Hombre");
    }

    @Override
    public void skin() {
        System.out.println("Sy un poco moreno");
    }

    @Override
    public void eyes() {
        System.out.println("Mis ojos son color cafe claro");
    }

    public void description() {
        sing();
        walk();
        talk();
        eat();
        sleep();
        gender();
        skin();
        eyes();
        play();
        lie();
    }
}
