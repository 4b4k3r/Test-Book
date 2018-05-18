package com.ks.Electronicos;

public class Laptop extends Computadora implements Electronico {
    public String Pad;
    public String LectorDactilar;

    public Laptop( double precio, String chipset, String modelo, String pad, String lectorDactilar) {
        super(precio,chipset,modelo);
        this.Pad = Pad;
        this.LectorDactilar = lectorDactilar;
    }

    public String getPad() {
        return Pad;
    }

    public void setPad(String pad) {
        Pad = pad;
    }

    public String getLectorDactilar() {
        return LectorDactilar;
    }

    public void setLectorDactilar(String lectorDactilar) {
        LectorDactilar = lectorDactilar;
    }


    @Override
    public void voltaje() {
        System.out.println("El voltaje de una laptop es de 7v");
    }

    @Override
    public void peso() {
        System.out.println("El peso de una laptop es de al menos 1kg");
    }

    @Override
    public String toString() {
        this.voltaje();
        this.peso();
        return "Laptop: \n Precio: "+precio+"\n Chipset: "+chipset+"\n Modelo: "+Modelo+"\n Pad: "+Pad+"\n Lector Dactilar : "+LectorDactilar;
    }
}
