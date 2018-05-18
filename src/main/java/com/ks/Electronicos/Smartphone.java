package com.ks.Electronicos;

public class Smartphone implements Electronico {
    public String SistemaOperativo;
    public String SIM;

    public Smartphone(String sistemaOperativo, String SIM) {
        SistemaOperativo = sistemaOperativo;
        this.SIM = SIM;
    }

    public String getSistemaOperativo() {
        return SistemaOperativo;
    }

    public void setSistemaOperativo(String sistemaOperativo) {
        SistemaOperativo = sistemaOperativo;
    }

    public String getSIM() {
        return SIM;
    }

    public void setSIM(String SIM) {
        this.SIM = SIM;
    }

    @Override
    public void voltaje() {
        System.out.println("El voltaje de un smartphone es de 5v");
    }

    @Override
    public void peso() {
        System.out.println("Un Smartphone pesa al menos 100g ");
    }

    @Override
    public String toString() {
        this.voltaje();
        this.peso();
        return "Smartphone: \n SO: "+SistemaOperativo+" \n SIM: "+SIM;
    }
}
