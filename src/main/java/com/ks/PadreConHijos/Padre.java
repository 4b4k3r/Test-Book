package com.ks.PadreConHijos;

public class Padre implements Humano,Habilidades{
    int VecesComer;
    int HorasDormir;
    public Padre(){
        VecesComer=3;
        HorasDormir=6;
    }

    @Override
    public void caminar() {
        System.out.println("Camino muy poco");
    }

    @Override
    public void hablar() {
        System.out.println("Hablo 4 idiomas");
    }

    @Override
    public void comer() {
        System.out.println("Como "+VecesComer+" al dia");
    }

    @Override
    public void dormir() {
        System.out.println("Duermo "+HorasDormir+" horas");
    }

    @Override
    public void sexo() {
        System.out.println("Soy Hombre");
    }

    @Override
    public void piel() {
        System.out.println("Sy un poco moreno");
    }

    @Override
    public void ojos() {
        System.out.println("Mis ojos son color cafe claro");
    }

    public void decripcion() {
        cantar();
        caminar();
        hablar();
        comer();
        dormir();
        sexo();
        piel();
        ojos();
        jugar();
        mentir();
    }
}
