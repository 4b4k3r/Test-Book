package com.ks.Threads;

import com.ks.lib.Configuracion;
import org.apache.logging.log4j.*;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;


public class TestThreads {
    private Thread hiloLlenar;
    private Thread HiloInsertarD;
    private Connection connection;
    private Queue<dato> cola;
    public int id;
    public dato Datos;
    public Statement statement;
    public Logger Depuracion = LogManager.getLogger(TestThreads.class.getName());

    public void start() {
        Depuracion.info("Instanciando los hilos");
        hiloLlenar = new Thread();
        HiloInsertarD = new Thread();
        cola = new LinkedList();
        id = 1;
        try {
            connection = conectarBD();
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Depuracion.info("Iniciando los hilos");
        AgregarCola();
        InsertarBd();
        Depuracion.error("Error de prueba");
    }


    public void AgregarCola() {
        if (!hiloLlenar.isAlive()) {
            hiloLlenar = new Thread(() -> {
                try {
                    long tolerancia = System.currentTimeMillis() + (8000);
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
                                Datos = cola.poll();
                                int id = Datos.getId();
                                String valor = Datos.getDato();
                                statement.execute("INSERT INTO datos values (" + id + ",'" + valor + "')");
                                System.out.println("se ha guardado id: " + id + " con valor: " + valor);
                            } catch (Exception e) {
                                Depuracion.error("Error al insertar datos: " + e.getMessage());
                            }
                        }
                    } else {
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
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

            Properties properties = new Properties();
            String pathconfiguracion = Configuracion.getRutaConfiguracion() + System.getProperty("propertiesName") + ".properties";
            Depuracion.info("Se esta conectando a la base de datos");
            File fileproperties = new File(pathconfiguracion);
            InputStream propiedades = new FileInputStream(fileproperties);
            properties.load(propiedades);
            String ruta = properties.getProperty("path");
            return connection = DriverManager.getConnection("jdbc:sqlite:" + ruta, "", "");
        } catch (SQLException e) {
            Depuracion.error("Error en la base de datos: " + e.getMessage());
        } catch (Exception e) {
            Depuracion.error("Error en la conexion a base de datos: " + e.getMessage());
        }
        return null;
    }

}
