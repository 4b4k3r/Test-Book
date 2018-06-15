package com.ks.FilterEvaluation;

import com.ks.lib.tcp.Cliente;
import com.ks.lib.tcp.EventosTCP;
import com.ks.lib.tcp.Tcp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Set;

/**
 * The <code>CollectorController</code> class
 * <p>
 * io.swagger.ks.comunicaciones
 * <p>
 * Created on 01/02/2018.
 *
 * @author Marco Calzada
 */

/**
 * Se utiliza el patron de inyeccion de paquetes para
 * permitir al controlador del colector acceder a los metodos de escucha
 * en la interfaz EventosTCP, definidos en las librerias
 * de com.ks.kslib v 1.6.4.8
 */
public class CollectorController implements EventosTCP
{

    private static final Logger DEPURACION = LogManager.getLogger(CollectorController.class.getName());
    private static final Marker PARENT = MarkerManager.getMarker("XML");
    private static final Marker _XML = MarkerManager.getMarker("XMLS").addParents(PARENT);

    /**
     * Cada conexion que recibe (abstraida como un objeto de tipo Tcp) se guarda en una
     * Hashtable para su acceso de manera sincronizada
     */
    private static final Hashtable<Integer, Tcp> CONNECTIONS_HASH = new Hashtable<>();

    /**
     * Token que distingue a cada transacción BFPROLAP del respectivo bloque
     */
    public static final String KEYWORD_1 = "BFPROLAP";

    /**
     * Token que distingue a cada transacción SENDTRAN del respectivo bloque
     */
    public static final String KEYWORD_2 = "SENDTRAN";

    /**
     * Campo Pathway del mensaje SENDTRAN
     */
    public static final String PATH = "$ENVIAR         ";

    /**
     * Campo que distingue al servicio solicitado por la guia
     */
    public static final String SERVICE = "SENDTRAN       ";

    /**
     * Atributo que guarda los bloques BFPROLAP que se reciben
     */
    private String message = "";

    /**
     * Atributo que guarda la ip del LocalHost en donde
     * se ejecuta Guia
     */
    private static String vmStrIpPc;

    /**
     * Atributo que guarda el nombre del LocalHost en donde
     * se ejecuta Guia
     */
    private static String vmStrPcName;

    /**
     * Atributo que determina si se envio un mensaje SENDTRAN
     */
    private static boolean vmBoolSentLogon = true;

    /**
     * Atributo que guarda la conexion actual como un objeto Tcp
     */
    private Tcp vmObjConnection;

    /**
     * Atributo que guarda la clase InetAddress la cual permite obtener
     * a partir de un host, la direccion IṔ
     */
    private static InetAddress vmObjLocalHost;

    /**
     * Variables estaticas que inicializaremos por defecto
     */
    static
    {
        try
        {
            vmObjLocalHost = InetAddress.getLocalHost();
            vmStrIpPc = vmObjLocalHost.getHostAddress();
            vmStrPcName = vmObjLocalHost.getHostAddress();

            if (vmStrPcName.length() > 14)
            {
                vmStrPcName = vmStrPcName.substring(0, 14);
            }
        }
        catch (UnknownHostException ex)
        {
            DEPURACION.error("Error al obtener IP y Nombre del equipo " + ex.getMessage());
        }
    }

    /**
     * Constructor que asigna la conexion actual al atributo vmObjConnection
     *
     * @param tcp interfaz que determina el comportamiento de cada conexion
     */
    public CollectorController(Tcp tcp)
    {
        this.vmObjConnection = tcp;
    }

    /**
     * Constructor que inicializa el atributo que guardara al bloque
     */
    public CollectorController()
    {
        message = "";
    }

    /**
     * Uno de los metodos a sobreescribir de la interfaz
     * EventosTCP, nos indica cuando una conexion se ha
     * establecido.
     *
     * @param cliente pertenece a un objeto Cliente quien
     *                a su vez, implementa a la interfaz Tcp.
     */
    @Override
    public void conexionEstablecida(Cliente cliente)
    {
        logon();

    }

    @Override
    public void errorConexion(String s)
    {

    }

    /**
     * Metodo que recibe datos; los manipula en el momento que estos llegan.
     * En el caso del colector, verifican el bloque BFPROLAP al descartar cualquier
     * transaccion incompleta al inicio y guardando en memoria, la ultima transaccion
     * del bloque en caso de estar incompleta para concatenarla con transacciones del
     * siguiente bloque
     *
     * @param s     contiene el mensaje enviado por conexion
     * @param bytes obtiene en bytes, el tamaño del mensaje enviado por conexion
     * @param tcp   interfaz que determina el comportamiento de cada conexion
     */
    @Override
    public void datosRecibidos(String s, byte[] bytes, Tcp tcp)
    {
        String correctTransaction = "";
        String transactionLength = "";
        String lastMessage = "";

        int messageLength;

        if (ConfigurationModel.isTypeOne())
        {
            new Thread(() -> DistributorController.sendToDistributors(s), "collectorSender").start();
        }
        else
        {
            message += s;

            // En este punto de ejecución discriminamos los mensajes SENDTRAN y BFPROLAP

            if (message.contains(KEYWORD_1) && (message.substring(message.indexOf(KEYWORD_1))).length() >= 20)
            {
                // Se comprueba la integridad de la primer transacción, en caso de estar incompleta, se
                // descarta y elimina de memoria
                if (message.contains(KEYWORD_1) && (message.substring(message.indexOf(KEYWORD_1))).length() >= 20)
                {
                    if (message.substring(0, message.indexOf(KEYWORD_1)).length() >= 16)
                    {
                        message = message.substring(message.indexOf(KEYWORD_1) - 16);
                    }

                    // Se comprueba la integridad de la última transacción, en caso de estar incompleta, se
                    // guarda en memoria y se concatena con la información proveniente del siguiente bloque
                    if (message.substring(((message.lastIndexOf(KEYWORD_1)) + 15), ((message.lastIndexOf(KEYWORD_1)) + 20)).matches("\\d+"))
                    {

                        transactionLength = message.substring((message.lastIndexOf(KEYWORD_1) + 15), message.lastIndexOf(KEYWORD_1) + 20);

                        messageLength = Integer.parseInt(transactionLength);
                        lastMessage = message;

                        // Si la última transacción del bloque tiene un tamaño menor o igual a 16
                        // tomando en cuenta una posible carencia del header previo al token BFPROLAP
                        if ((messageLength - 16) <= (lastMessage.substring(lastMessage.indexOf(KEYWORD_1))).length())
                        {
                            correctTransaction = lastMessage;
                            message = "";
                        }
                        else
                        {
                            correctTransaction = message.substring(0, message.lastIndexOf(KEYWORD_1));
                            message = message.substring(message.lastIndexOf(KEYWORD_1));
                        }
                    }
                    else
                    {
                        correctTransaction = message.substring(0, message.lastIndexOf(KEYWORD_1));
                        message = message.substring(message.lastIndexOf(KEYWORD_1));
                    }
                }

                RequestManager.newTransaction(correctTransaction);
            }

            else if (message.contains(KEYWORD_2) && (message.substring(message.indexOf(KEYWORD_2)) + 8).length() > 0)
            {
                correctTransaction = message;
                message = "";
                getCollectorModel().setSentLogon(Boolean.TRUE);

                RequestManager.newTransaction(correctTransaction);
            }
        }
    }

    @Override
    public void cerrarConexion(Cliente cliente)
    {

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

    /**
     * Metodo que regresa un objeto pojo del colector si su respectiva conexion existe
     * en memoria
     */
    public CollectorModel getCollectorModel()
    {
        Set<Integer> idConnections = CONNECTIONS_HASH.keySet();

        for (Integer idConnection : idConnections)
        {
            if (CONNECTIONS_HASH.get(idConnection).equals(vmObjConnection))
            {
                return CollectorModel.getCollectorModelHash().get(idConnection);
            }
        }

        return null;
    }

    /**
     * Metodo que crea y envia el mensaje SENDTRAN
     *
     * @throws UnknownHostException cuando se no se puede asociar una IP al nombre del host
     */

    public void logon()
    {

        /**
         * El id de la conexion actual debe corresponder con el id
         * del pojo del colector quien contiene la configuracion del
         * mensaje
         */
        CollectorModel vlObjCollectorModel = getCollectorModel();

        byte vlByteMessageType = vlObjCollectorModel.getMessageType();
        byte vlByteMessageLength = vlObjCollectorModel.getMessageLength();

        /**
         * Para este punto, el atributo user ya fue desencriptado.
         */
        String vlStrUser = vlObjCollectorModel.getUser();

        /**
         * Inicializamos la variable que contendra el SENDTRAN
         * armado por guia
         */
        String vlStrLogon = "";

        /**
         * Armamos el mensaje dependiendo del la configuracion leida en
         * Colector
         */
        if (vlByteMessageType == 1)
        {
            /**
             * Hace referencia a un mensaje estandar normal
             */
            if (vlByteMessageLength == 1)
            {
                vlStrLogon = PATH + SERVICE + "65   " + String.format("%-15s", vmStrIpPc) + String.format("%-15s", vmStrPcName) + String.format("%189s", "") + String.format("%-6s", vlStrUser);
            }
            /**
             * Hace referencia a un mensaje estandar corto
             */
            else if (vlByteMessageLength == 2)
            {
                vlStrLogon = PATH + SERVICE + "65   " + vmStrIpPc + vmStrPcName + String.format("%-6s", vlStrUser) + "10";
            }
            else
            {
                vmBoolSentLogon = false;
                DEPURACION.error("No se asigno un valor permitido para la longitud del mensaje");
            }
        }
        /**
         * Mensaje Stratus
         */
        else if (vlByteMessageType == 2)
        {
            vlStrLogon = PATH + SERVICE + "00078" + vmStrIpPc + vmStrPcName + String.format("%-6s", vlStrUser) + " 1000000";
        }
        else
        {
            vmBoolSentLogon = false;
            DEPURACION.error("No se asigno un valor permitido para la longitud del mensaje, no se enviara el Logon");
        }

        if (vmBoolSentLogon == true)
        {
            DEPURACION.info(_XML, "Mensaje Logon: <Envia>" + vlStrLogon + "</Envia>");
        }

        vmObjConnection.enviar(vlStrLogon);
    }
}
