package com.ks.PilasColas;

public class Nodo {
    private int id;
    private String dato;

    public Nodo(int id, String dato) {
        this.id = id;
        this.dato = dato;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

}
