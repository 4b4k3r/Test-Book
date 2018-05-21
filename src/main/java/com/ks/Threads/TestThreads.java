package com.ks.Threads;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Queue;

public class TestThreads {
    /*un hilo tiene cuatro estados:
        +recien creada -> se le asignan datos
        +corrible -> (Runnable) ocupa la cpu en cualquier momento
            -si en este paso se ocupa yield pasa a ultimo lugar pero aun en corriendo
        +bloqueada -> se invoca el metodo suspend() y por herencia se corre el metodo
                      sleep el hilo invoca join() al hilo corriendo y espera a que
                      termine el evento para inmediatamente ejecutarse al termino de este
         +muerta-> acaba la ejecucion del hilo u otro hilo ejecuta el metodo stop del otro hilo
                    es recomendable usar safeStop() para evitar inconcistencias

          Cuando un hilo llama a wait(), la llave que éste tiene es liberada, así otro proceso
          que esperaba por ingresar al monitor puede hacerlo. notify() sólo despierta o desbloquea
          un hilo, si lo hay esperando. notifyAll() despierta a todos los que estén esperando

    */
    private Thread hiloLlenar;
    private Thread HiloInsertarD;
    private Connection connection;
    private Queue cola;
    public int id;
    public dato Datos;
    public Statement statement;

    public TestThreads() {
        hiloLlenar = new Thread();
        HiloInsertarD = new Thread();
        cola = new LinkedList();
        id = 1;
        try {
            connection = conectarBD();
            statement=connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void AgregarCola() {
        if (!hiloLlenar.isAlive()) {
            hiloLlenar = new Thread(() -> {
                try {
                    long tolerancia = System.currentTimeMillis() + (10000);
                    do {
                        dato Dato = new dato(id, String.valueOf((int) (Math.random() * 100) + 1));
                        synchronized (cola) {
                            cola.add(Dato);
                        }
                        System.out.println("se ha agregado " + Dato.getDato() + " a la cola con id " + Dato.getId());
                        id++;
                        Thread.sleep(300);
                    } while (System.currentTimeMillis() < tolerancia);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            hiloLlenar.setDaemon(false);
            hiloLlenar.start();
        }
    }

    public void InsertarBd() {
        if (!HiloInsertarD.isAlive()) {
            HiloInsertarD = new Thread(() -> {
                do {
                    if (!cola.isEmpty()) {
                        synchronized (cola) {
                            try {
                                Datos= (dato) cola.poll();
                                int id=Datos.getId();
                                String valor=Datos.getDato();
                                statement.execute("INSERT INTO datos values ("+id+",'"+valor+"')");
                                System.out.println("se ha guardado id: "+id+" con valor: "+valor);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } while (hiloLlenar.isAlive());
            });
            HiloInsertarD.setDaemon(false);
            HiloInsertarD.start();
        }
    }

    public Connection conectarBD() {
        try {
            return connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\joela\\IdeaProjects\\javp\\MisPruebas\\sqlite\\base.db", "", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
