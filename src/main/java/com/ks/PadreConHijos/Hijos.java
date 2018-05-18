package com.ks.PadreConHijos;

public class Hijos extends Padre {
    String cantar;
    String caminar;
    String Hablar;
    int VecesComer;
    int HorasDormir;
    String sexo;
    String piel;
    String ojos;
    String mentir;
    String jugar;

    public Hijos(String cantar, String caminar, String hablar, int vecesComer, int horasDormir, String sexo, String piel, String ojos, String mentir, String jugar) {
        this.cantar = cantar;
        this.caminar = caminar;
        Hablar = hablar;
        VecesComer = vecesComer;
        HorasDormir = horasDormir;
        this.sexo = sexo;
        this.piel = piel;
        this.ojos = ojos;
        this.mentir = mentir;
        this.jugar = jugar;
    }

    public String getCantar() {
        return cantar;
    }

    public void setCantar(String cantar) {
        this.cantar = cantar;
    }

    public String getCaminar() {
        return caminar;
    }

    public void setCaminar(String caminar) {
        this.caminar = caminar;
    }

    public String getHablar() {
        return Hablar;
    }

    public void setHablar(String hablar) {
        Hablar = hablar;
    }

    public int getVecesComer() {
        return VecesComer;
    }

    public void setVecesComer(int vecesComer) {
        VecesComer = vecesComer;
    }

    public int getHorasDormir() {
        return HorasDormir;
    }

    public void setHorasDormir(int horasDormir) {
        HorasDormir = horasDormir;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getPiel() {
        return piel;
    }

    public void setPiel(String piel) {
        this.piel = piel;
    }

    public String getOjos() {
        return ojos;
    }

    public void setOjos(String ojos) {
        this.ojos = ojos;
    }

    public String getMentir() {
        return mentir;
    }

    public void setMentir(String mentir) {
        this.mentir = mentir;
    }

    public String getJugar() {
        return jugar;
    }

    public void setJugar(String jugar) {
        this.jugar = jugar;
    }

    @Override
    public void decripcion() {
        super.decripcion();
    }

    @Override
    public void cantar() {
        System.out.println(getCantar());
    }

    @Override
    public void caminar() {
        System.out.println(getCaminar());
    }

    @Override
    public void hablar() {
        System.out.println(getHablar());
    }

    @Override
    public void comer() {
        System.out.println("Como " + getVecesComer() + " veces al dia");
    }

    @Override
    public void dormir() {
        System.out.println("Duermo " + getHorasDormir()+" horas");
    }

    @Override
    public void sexo() {
        System.out.println("Soy " + getSexo());
    }

    @Override
    public void piel() {
        System.out.println("Soy de piel " + getPiel());
    }

    @Override
    public void ojos() {
        System.out.println("Mis ojos son color " + getOjos());
    }

    @Override
    public void jugar() {
        System.out.println(getJugar());
    }

    @Override
    public void mentir() {
        System.out.println(getMentir());
    }

}
