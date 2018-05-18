package com.ks.Electronicos;

public class Electronicos {
    public void run(){
        Electronico computadora= new Computadora(15020.50,"AMD A9","MS64");
        System.out.println(computadora);
        Electronico laptop = new Laptop(12300,"core 15 8va","Lenovo thinkpad","multitouch","5ta gen");
        System.out.println(laptop);
        Electronico smartphone = new Smartphone("IOS","Unefon");
        System.out.println(smartphone);
    }
}
