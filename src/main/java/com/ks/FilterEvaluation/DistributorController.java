package com.ks.FilterEvaluation;

import com.ks.lib.tcp.Cliente;
import com.ks.lib.tcp.EventosTCP;
import com.ks.lib.tcp.Tcp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * The <code>DistributorController</code> class
 * <p>
 * io.swagger.ks.comunicaciones
 * <p>
 * Created on 01/02/2018.
 *
 * @author Marco Calzada
 */


/**
 * Se utiliza el patron de inyeccion de paquetes para
 * permitir al controlado del distribuidor acceder a los metodos de escucha
 * en la interfaz EventosTCP, definidos en las librerias
 * de com.ks.kslib v 1.6.4.8
 */
public class DistributorController implements EventosTCP
{
    private static final Logger DEPURACION = LogManager.getLogger((DistributorController.class.getName()));
    /**
     * Cada conexion que recibe (abstraida com un objeto TCP) se guarda en una Hashtable para su acceso
     * de manera sincronizada
     */
    private static final Hashtable<Integer, Tcp> CONNECTIONS_HASH = new Hashtable<>();

    /**
     * Timer que una vez transcurridos 30 segundos, ejecuta el metodo validateConnection
     */
    private final Timer TIMER_CIERRE_CONEXION = new Timer(30000, event -> validateConnections());

    /**
     * Atributo singleton que permite hacer busquedas hacia base de datos
     */
    final Base vlObjBase = Base.getInstance();

    /**
     * Atributo que obtiene al cliente actual si es que este se
     * conecta y autentifica con su respectivo LOGON
     */
    private Cliente vmObjCliente;

    /**
     * Atributo que guarda la conexion actual como un objeto Tcp
     */
    private Tcp vmObjConnection;

    /**
     * Atributo que indica si el cliente ha hecho logon
     */
    private static boolean vmBoolClientHasLogon = false;

    /**
     * Atributo que indica si un cliente se ha conectao
     */
    private static boolean vmBoolClientIsConnected = false;

    /**
     * Constructor que asigna la conexion actual al atributo vmObjConnection
     *
     * @param tcp interfaz que determina el comportamiento de cada conexion
     */
    public DistributorController(Tcp tcp)
    {
        this.vmObjConnection = tcp;
    }

    /**
     * Uno de los metodos a sobreescribir de la interfaz
     * EventosTCP, nos indica cuando una conexion se ha
     * establecido. Si un cliente ya se encuentra conectado
     * rechaza cierra o si este durante un lapso de 30 segundos,
     * no ha enviado una autenticacion LOGON
     *
     * @param cliente pertenece a un objeto Cliente quien
     *                a su vez, implementa a la interfaz Tcp
     */
    @Override
    public void conexionEstablecida(Cliente cliente)
    {
        if (!vmBoolClientIsConnected)
        {
            vmBoolClientIsConnected = true;
            TIMER_CIERRE_CONEXION.start();
            this.vmObjCliente = cliente;
        }
        else
        {
            cliente.cerrar();
        }
    }

    @Override
    public void errorConexion(String s)
    {

    }

    /**
     * Metodo que recibe datos; los manipula en el momento que estos llegan.
     * En el caso del distribuidor, verifica el mensaje SENDTRAN y de el, obtiene
     * el ID de usuario, el usuario, su Ip y su App
     *
     * @param s     contiene el mensaje enviado por conexion
     * @param bytes obtiene en bytes, el tamaño del mensaje enviado por conexion
     * @param tcp   interfaz que determina el comportamiento de cada conexion
     */
    @Override
    public void datosRecibidos(String s, byte[] bytes, Tcp tcp)
    {
        vmBoolClientIsConnected = false;

        int vlIntPuerto;

        String vlStrmessage = null;
        final String vlStrIdUser;
        final String vlStrName;
        final String vlStrIp;
        String vlStrApp;

        DistributorModel vlObjDistributorModel = getDistributorModel();

        /**
         * El escenario planteado por esta validacion no deberia ocurrir pues
         * el distribuidor ya existe en memoria.
         */
        if (vlObjDistributorModel != null)
        {
            if (s.contains("SENDTRAN"))
            {
                if (s.length() >= 57)
                {

                    vlStrmessage = s.substring(s.indexOf("SENDTRAN"));
                    vlStrIdUser = vlStrmessage.substring(36, 49).trim();

                    /**
                     *
                     * En este punto, es necesario determinar si el usuario ya existe en memoria
                     */
                    Set<String> idUsers = User.getUserHashtable().keySet();

                    for (String idUser : idUsers)
                    {
                        if (User.getUserHashtable().get(idUser).getUserName().equals(idUser))
                        {
                            vmBoolClientHasLogon = true;
                            break;
                        }
                    }


                    /**
                     * Si el usuario no se encontro en memoria, entonces, se busca en base de
                     * datos
                     */
                    if (vmBoolClientHasLogon == false)
                    {
                        idUsers = User.getUserHashtable().keySet();
                        try
                        {
                            vlObjBase.readUser(vlStrIdUser);

                            /**
                             * Una vez consultada la base de datos, comprobamos la existencia del
                             * usuario otra vez
                             */
                            for (String idUser : idUsers)
                            {
                                if (User.getUserHashtable().get(idUser).getIdUsername().equals(idUser))
                                {
                                    vmBoolClientHasLogon = true;
                                    break;
                                }
                            }
                        }
                        catch (SQLException ex)
                        {
                            DEPURACION.error("Error interno de SQL (leer usuario) " + ex.getMessage());
                        }
                    }

                    /**
                     * Para este punto ya sabemos si el usuario se encontraba en
                     * memoria o en base de datos
                     */
                    if (vmBoolClientHasLogon == true)
                    {

                        TIMER_CIERRE_CONEXION.stop();

                        /**
                         * Una vez autenticado el logon del cliente
                         * y verificado la existencia del usuario,
                         * procedemos a obtener informacion del mensaje
                         * SENDTRAN
                         */
                        vlStrIp = vlStrmessage.substring(20, 35).trim();
                        vlStrApp = vlStrmessage.substring(55).trim();
                        vlStrName = vlStrmessage.substring(239, 244);

                        if (vlStrApp.length() > 2)
                        {
                            vlStrApp = vlStrApp.substring(0, 2);
                        }

                        vlObjDistributorModel.setUser(vlStrIdUser);
                        vlObjDistributorModel.setIp(vlStrIp);
                        vlObjDistributorModel.setApp(vlStrApp);
                        vlObjDistributorModel.setName(vlStrName);
                    }
                    else
                    {
                        DEPURACION.error("El usuario " + vlStrIdUser + " no se encontro en BD");
                    }

                }
                else
                {
                    DEPURACION.error("Se descarte el mensaje por no cumplir con la longitud minima");
                }
            }
            else
            {
                DEPURACION.error("Se descarta el mensaje por no tener la validacion SENDTRAN");
            }
        }
        else
        {
            DEPURACION.error("No existe el cliente en sistema");
        }
    }

    /**
     * Metodo que cierra la conexion y asigna a los atributos
     * de conexion y de autenticacion el valor falso
     *
     * @param cliente pertenece a un objeto Cliente quien
     *                a su vez, implementa a la interfaz Tcp.
     */
    @Override
    public void cerrarConexion(Cliente cliente)
    {
        vmBoolClientHasLogon = false;
        vmBoolClientIsConnected = false;
    }

    /**
     * Metodo que guarda en una Hashtable una conexion asociada con un id unico
     *
     * @param id         contiene el identificador de cada conexion
     * @param connection interfaz que determina el comportamiento de cada conexion
     */
    public void newConnection(int id, Tcp connection)
    {
        CONNECTIONS_HASH.put(id, connection);
    }

    public static void sendToDistributors(String message)
    {
        sendToDistributors(message, CONNECTIONS_HASH.values());
    }

    public static void sendToDistributors(String message, int port)
    {
        if (port > 0)
        {
            sendToSpecificDistributor(port, message, CONNECTIONS_HASH.values());
        }
    }

    public static void sendToDistributors(String message, Collection<Tcp> connections)
    {
        connections.forEach(connection -> connection.enviar(message));

    }

    public static void sendToSpecificDistributor(int port, String message, Collection<Tcp> connections)
    {
        connections.forEach(connection -> {
            if (connections.contains(String.valueOf(port)))
            {
                connection.enviar(message);
            }
        });
    }

    /**
     * Metodo que regresa un objeto pojo del distribuidor si su respectiva conexion existe
     * en memoria.
     *
     * @return pojo de DistributorModel
     */
    public DistributorModel getDistributorModel()
    {
        Set<Integer> idConnections = CONNECTIONS_HASH.keySet();

        for (Integer idConnection : idConnections)
        {
            if (CONNECTIONS_HASH.get(idConnection).equals(vmObjConnection))
            {
                /** Ya sabemos que el modelo reside en memoria y lo asociamos **/
                return DistributorModel.getDistributorModelHash().get(idConnection);
            }
        }

        /** Para este punto, si no existe, regresamos un valor nulo **/
        return null;
    }

    /**
     * Validamos si un cliente no se ha validado con un LOGON en 30 segundos,
     * si este fuese el caso, cerramos la conexion con dicho cliente
     */
    public void validateConnections()
    {
        if (!vmBoolClientHasLogon)
        {
            try
            {
                this.vmObjCliente.cerrar();
            }
            catch (Exception ex)
            {
                DEPURACION.error("Hubo un error al cerrar  conexion con el cliente: " + ex.getMessage());
            }
        }
    }

    public static void evaluateFilter()
    {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Boolean> response = executorService.submit(new FilterManager());
        try
        {
            if (response.get())
            {
                DEPURACION.info("La transaccion cumple con los filtros y ha sido enviada al distribuidor asignado");
                FilterManager.sendToDistributor();
            }
            else
            {
                DEPURACION.info("La transaccion no cumple con los filtros");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
