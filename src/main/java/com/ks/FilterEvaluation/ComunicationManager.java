package com.ks.FilterEvaluation;

import com.ks.lib.tcp.Cliente;
import com.ks.lib.tcp.Servidor;
import com.ks.lib.tcp.Tcp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Hashtable;

/**
 * The <code>ComunicationManager</code> class
 * <p>
 * io.swagger.ks.comunicaciones
 * <p>
 * Created on 01/02/2018.
 *
 * @author Marco Calzada
 */

/**
 * Clase que inicia las conexiones de Colector y Emisor ya sea como servidor o cliente
 */
public class ComunicationManager
{
    private static final Logger DEPURACION = LogManager.getLogger(ComunicationManager.class.getName());

    /**
     * Metodo que inicia la conexion ligada a un controlador y a la vez, guarda el colector en memoria
     * mediante un hashtable
     */
    public static void startCollectorsService()
    {
        final Hashtable<Integer, CollectorModel> collectorModelHashtable = CollectorModel.getCollectorModelHash();

        for (CollectorModel collectorModel : collectorModelHashtable.values())
        {
            try
            {
                final Tcp connection;
                final CollectorController collectorController;

                if (collectorModel.getConnectionType().equals((ConnectionType.SERVER)))
                {
                    connection = new Servidor();
                }
                else
                {
                    connection = new Cliente();
                    connection.setIP(collectorModel.getPrimaryIp());
                }

                collectorController = new CollectorController(connection);
                connection.setPuerto(collectorModel.getPrimaryPort());
                connection.setEventos(collectorController);

                collectorController.newConnection(collectorModel.getId(), connection);
                connection.conectar();
            }
            catch (Exception e)
            {
                DEPURACION.error("Problema al intentar iniciar conexion: " + e.getMessage());
            }
        }
    }

    /**
     * Metodo que inicia la conexion ligada a un distribuidor y a la vez, guarda el distribuidor en memoria
     * mediante un hashtable
     */
    public static void startDistributorService()
    {
        final Hashtable<Integer, DistributorModel> distributorModelHashtable = DistributorModel.getDistributorModelHash();

        for (DistributorModel distributorModel : distributorModelHashtable.values())
        {
            try
            {
                final Tcp connection;
                final DistributorController distributorController;

                if (distributorModel.getConnectionType().equals(ConnectionType.SERVER))
                {
                    connection = new Servidor();
                }
                else
                {
                    connection = new Cliente();
                    connection.setIP(distributorModel.getHost());
                }

                distributorController = new DistributorController(connection);
                connection.setPuerto(distributorModel.getPort());
                connection.setEventos(distributorController);

                distributorController.newConnection(distributorModel.getId(), connection);
                connection.conectar();
            }
            catch (Exception e)
            {
                DEPURACION.error("Problema al intentar iniciar conexion: " + e.getMessage());
            }
        }
    }
}
