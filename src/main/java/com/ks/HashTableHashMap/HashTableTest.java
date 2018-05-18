package com.ks.HashTableHashMap;

import java.util.Enumeration;
import java.util.Hashtable;

public class HashTableTest {
    public void start(){
        //definimos una variable de tipo contenedor con dos columnas, una para el indice y la siguiente para un dato
        //especifico referente a su indice
        Hashtable<String,String> container = new Hashtable<String, String>();
        //el metodo put de un contenedor Hashtable agrega datos al contenedor de tipo Hashtable
        container.put("3","Huevo");
        container.put("1","Arroz");
        container.put("5","Leche");
        container.put("2","Frijol");
        container.put("4","Café");
        container.put("6","Sopa");
        //el primer valor del contenedor se define por defecto como el indice, para obtener un valor
        //accederemos a el mediante el metodo get el cual recibe el tipo de dato que definimos desde el contenedor
        container.get("3");
        container.get("5");
        container.get("1");
        //Se define un array que recibe parametros de tipo string el cual
        //va a contener todos los valores pertenecientes a el indice mas no el indice
        Enumeration<String> enumeration = container.elements();
        System.out.println("Hash Table valores: ");
        //mientras haya mas elementos de la Hashtable en el array de enumeracion
        while(enumeration.hasMoreElements()){
            //mostramos los valores pertenecientes de cada indice
            System.out.println(enumeration.nextElement());
        }
        //definimos un array que solo contenga las claves del contenedor de tipo Hashtable
        Enumeration<String> enumeration1 = container.keys();
        //mientras haya mas indices de la Hashtable en el array de enumeracion1
        System.out.println("Los indices/llaves son: ");
        while (enumeration1.hasMoreElements()){
            //mostramos el elemento de la enumeracion1 que contiene el inice de la hashtable
            System.out.println(enumeration1.nextElement());
        }
    }
}
