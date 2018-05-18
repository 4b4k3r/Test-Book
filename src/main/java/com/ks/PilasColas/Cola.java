package com.ks.PilasColas;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Cola {
    private Queue<Nodo> cola;
    private ArrayList<Nodo> array;
    private java.awt.List ListaAwt;
    private java.util.List ListaUtil;

    public Cola() {

        cola = new LinkedList();
        array = new ArrayList<>();
        ListaAwt = new java.awt.List();
        ListaUtil = new ArrayList();

    }


    public void llenadoCola() {

        for (int i = 0; i < 10; i++) {

            Nodo nodo = new Nodo(i + 1, "hola" + (i + 1));
            cola.add(nodo);
            array.add(nodo);
            //ListaAwt.add(,nodo.getDato());

        }
    }

    public void muestraDatos() {

        int conutador = 0;
        while (cola.size() > 0) {
            Nodo nodo = cola.poll();
            System.out.println(nodo.getId() + " " + nodo.getDato());
            Nodo nodo1 = array.get(conutador);
            System.out.println(nodo1.getId() + " de array " + nodo1.getDato());
            String nodo2 = ListaAwt.getItem(conutador);
            //System.out.println(nodo2.getId()+" de array "+ nodo2.getDato());
            conutador++;
        }

    }

}
