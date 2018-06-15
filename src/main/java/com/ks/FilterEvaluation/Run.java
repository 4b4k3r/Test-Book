package com.ks.FilterEvaluation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Run
{
    private static final Logger DEPURACION = LogManager.getLogger();

    public static void correr()
    {
        try
        {
            ReadConfiguration readConfiguration = ReadConfiguration.getInstance();
            Base base = Base.getInstance();
            Counter counter = Counter.getInstance();

            DEPURACION.info("Se inicia lectura de configuracion");

            try
            {
                readConfiguration.readInitialConfiguration();

                counter.validateTable();
                counter.readCounter();
                base.readUsers();

                base.register();
                base.readCollectorConfiguration();
                base.readDistributorConfiguration();

                base.readLayoutConfiguration();
                base.readFilterConfiguration();

                ComunicationManager.startCollectorsService();
                ComunicationManager.startDistributorService();

                DEPURACION.info("Termino lectura de configuracion");
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
