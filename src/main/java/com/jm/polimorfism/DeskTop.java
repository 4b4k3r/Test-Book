package com.jm.polimorfism;

public class DeskTop implements Electronic {
    public double precio;
    public String chipset;
    public String Modelo;

    public DeskTop(double precio, String chipset, String Modelo) {
        this.precio = precio;
        this.chipset = chipset;
        this.Modelo= Modelo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getChipset() {
        return chipset;
    }

    public void setChipset(String chipset) {
        this.chipset = chipset;
    }

    public String getModelo() {
        return Modelo;
    }

    public void setModelo(String modelo) {
        Modelo = modelo;
    }

    @Override
    public void voltaje() {
        System.out.println("El voltaje de una Computadora es de mas de 200w");
    }

    @Override
    public void peso() {
        System.out.println("El peso de una computadora es mas de 2kg");
    }

    @Override
    public String toString() {
        this.voltaje();
        this.peso();
        return "Computadora: \n Precio: "+precio+" \n Chipset: "+chipset+" \n Modelo: "+Modelo;
    }
}
