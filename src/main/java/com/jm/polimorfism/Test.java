package com.jm.polimorfism;

public class Test {
    public static void main(String[] args) {
        Electronic computadora = new DeskTop(15020.50, "AMD A9", "MS64");
        System.out.println(computadora);
        Electronic laptop = new Laptop(12300, "core 15 8va", "Lenovo thinkpad", "multitouch", "5ta gen");
        System.out.println(laptop);
        Electronic smartphone = new Smartphone("IOS", "Unefon");
        System.out.println(smartphone);
    }
}
