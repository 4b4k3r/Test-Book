package com.ks.OverloadOverride.Main;

public class operaciones {

    public static void main(String[] args) {
        metodos m = new metodos();
        int Entero1 = 7;
        int Entero2 = 3;
        Double double1 = 3.5;
        Double double2 = 2.2;
        System.out.println(" Sobrecarga de metodos:"+
        "\n Multiplicacion de Doubles = " + m.multiplicar(double1, double2)+
        "\n Multiplicacion de Double e int = " + m.multiplicar(Entero1, double2)+
        "\n Multiplicacion de ints = " + m.multiplicar(Entero2, Entero1));
        System.out.println("\n Sobreescritura de metodos:");
        System.out.print(" Multiplicacion de dos doubles y un entero = "+multiplicar(double1,double2,Entero1));
    }



    public static Double multiplicar(double double1, double double2,int entero1) {
        double1 = 1.5;
        double2=1.9;
        entero1 =6;
        Double res = 1.5*1.9*6;
        return res;
    }
}
