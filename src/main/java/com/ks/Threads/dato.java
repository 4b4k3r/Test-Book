package com.ks.Threads;

public class dato {
    public int id;
    public String Dato;

    public dato(int id, String dato) {
        this.id = id;
        Dato = dato;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDato() {
        return Dato;
    }

    public void setDato(String dato) {
        Dato = dato;
    }
}
