package com.ks.FilterEvaluation;

import com.ks.lib.Configuracion;
import com.ks.lib.seguridad.Aes_128;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Se encarga de la lectura de configuración desde XML. Dado que la
 * configuración es universal para cada colector o distribuidor, se
 * hace de esta clase un singleton
 */
public class ReadConfiguration
{
    private static final Logger DEPURACION = LogManager.getLogger(ReadConfiguration.class.getName());
    private static final ReadConfiguration _INSTANCE = new ReadConfiguration();

    private SAXBuilder vmObjBuilder;
    private File vmObjMyArchive;
    private File vmObjMyComunications;
    private File vmObjMyArchiveFilter;
    private File vmObjMyArchiveLayoutField;

    private final LayoutModel vmLayoutModel = LayoutModel.getInstance();

    /**
     * Constructor que inicializa los componentes JDOM
     * así como las rutas de cada uno de los archivos
     * xml para su posterior lectura
     */
    private ReadConfiguration()
    {
        vmObjBuilder = new SAXBuilder();
        vmObjMyArchive = new File(Configuracion.getRutaConfiguracion() + "config.xml");
        vmObjMyComunications = new File(Configuracion.getRutaConfiguracion() + "comunicacion.xml");
        vmObjMyArchiveFilter = new File(Configuracion.getRutaConfiguracion() + "filters.xml");
        vmObjMyArchiveLayoutField = new File(Configuracion.getRutaConfiguracion() + "layout.xml");
    }

    /**
     * Método que permite implementar esta clase bajo el patrón singleton
     *
     * @return una única instancia de la clase ReadConfiguration
     *
     */
    public static ReadConfiguration getInstance()
    {
        return _INSTANCE;
    }

    /**
     *
     *  Método que lee información desde el archivo config.xml
     *
     * @throws JDOMException cuando se construye un documento de un archivo "build(vmObjMyArchive)"
     * @throws IOException   cuando se lee un documento de un archivo "build(vmObjMyArchive)"
     * @throws KSException   cuando ocurre un RunTimeException
     */
    public void readInitialConfiguration() throws JDOMException, IOException, KSException
    {

        final Element vlElmDataBase;
        final Element vlElmAppData;

        Document vlObjDocument = vmObjBuilder.build(vmObjMyArchive);

        try
        {
            final ConfigurationModel vlObjConfiguracion = ConfigurationModel.getInstance();

            final DataBase vlObjdataBase = new DataBase();
            final Element vlElmRootNode = vlObjDocument.getRootElement();

            vlElmDataBase = vlElmRootNode.getChild("dataBase");
            vlElmAppData = vlElmRootNode.getChild("applicationData");

            /**
             * Mediante un patrón enum, elegimos el tipo de base de datos:
             * _SQLSERVER(1)
             * _MYSQL(2)
             * _ORACLE(3)
             * _XML(4)
             */
            DataBase.setDataBaseType(DataBase.MotorBd.fromValue(Integer.parseInt(vlElmDataBase.getChildText("dataBaseType"))));

            DataBase.setServerHost(vlElmDataBase.getChildText("server"));
            DataBase.setBaseName(vlElmDataBase.getChildText("base"));
            DataBase.setUser(vlElmDataBase.getChildText("user"));
            DataBase.setPassword(vlElmDataBase.getChildText("password"));
            DataBase.setInstance(vlElmDataBase.getChildText("instance"));

            vlObjConfiguracion.setGuideType(Integer.parseInt(vlElmAppData.getChildText("guideType")));
            vlObjConfiguracion.setVlObjDataBase(vlObjdataBase);
        }
        catch (RuntimeException ex)
        {
            throw new KSException("Ocurrió una excepción al intentar leer la configuración inicial " + ex.getMessage());
        }
    }

    /**
     *
     * Método que lee información desde el archivo comunicacion.xml y de ella, los valores pertenecientes
     * a los tags de distribuidores. Necesarios para inicializar pojo's de distribuidor en memoria
     *
     * @throws JDOMException    cuando se construye un documento de un archivo "build(vmObjMyComunications)"
     * @throws IOException      cuando se lee un documento de un archivo "build(vmObjMyComunications)"
     * @throws KSException      cuando ocurre un RunTimeException
     */
    public void readDistributorConfiguration() throws JDOMException, IOException, KSException
    {
        final Document vlObjDocument = vmObjBuilder.build(vmObjMyComunications);

        try
        {
            DistributorModel vlObjDistributorModel;

            final Element vlElmRootNode = vlObjDocument.getRootElement();
            final Element vlElmDistributorNode = vlElmRootNode.getChild("distribuidores");
            final Element vlElmGeneralConfigNode = vlElmDistributorNode.getChild("generalConfiguration");


            DistributorModel.setLayoutId(Integer.parseInt(vlElmGeneralConfigNode.getAttributeValue("layoutId")));
            DistributorModel.setFormatId(Integer.parseInt(vlElmGeneralConfigNode.getAttributeValue("formatID")));

            if (ConfigurationModel.isTypeOne() && Boolean.valueOf(vlElmGeneralConfigNode.getAttributeValue("sequentialPorts")))
            {
                final int vlIntListenPort = Integer.parseInt(vlElmDistributorNode.getChildText("listenPort"));


                final String vlStrNumberOfPortsDecrypted = Aes_128.desencriptar(vlElmDistributorNode.getChildText("numberOfPorts"));
                int vlIntNumberOfPorts = Integer.parseInt(vlStrNumberOfPortsDecrypted);

                final String ReplicadorDecrypted = Aes_128.desencriptar(vlElmDistributorNode.getChildText("replicador"));


                if (Boolean.valueOf(ReplicadorDecrypted))
                {
                    vlIntNumberOfPorts++;
                }

                for (int currentPort = vlIntListenPort; currentPort <= (vlIntListenPort + vlIntNumberOfPorts); currentPort++)
                {
                    vlObjDistributorModel = new DistributorModel();

                    vlObjDistributorModel.setPort(currentPort);
                    vlObjDistributorModel.setHost("localhost");
                    vlObjDistributorModel.setConnectionType(ConnectionType.SERVER);
                    vlObjDistributorModel.setContainTokens(Boolean.valueOf(vlElmDistributorNode.getAttributeValue("token")));

                    vlObjDistributorModel.save();
                }
            }
            else
            {
                final List<Element> vlLstDistributor = vlElmDistributorNode.getChildren("distribuidor");

                for (Element distributorNode : vlLstDistributor)
                {
                    vlObjDistributorModel = new DistributorModel();

                    vlObjDistributorModel.setHost(distributorNode.getChildText("ip"));
                    vlObjDistributorModel.setPort(Integer.parseInt(distributorNode.getChildText("port")));
                    vlObjDistributorModel.setConnectionType(ConnectionType.forValue(distributorNode.getChildText("type")));
                    vlObjDistributorModel.setContainTokens(Boolean.valueOf(distributorNode.getChildText("token")));

                    vlObjDistributorModel.save();
                }
            }
        }
        catch (RuntimeException ex)
        {
            throw new KSException("Ocurrió una excepción al intentar leer la configuración de distribuidores " + ex.getMessage());
        }

    }

    /**
     *
     * Método que lee información desde el archivo comunicacion.xml y de ella, los valores pertenecientes
     * a los tags de colectores. Necesarios para inicializar pojo's de colector en memoria
     *
     * @throws JDOMException    cuando se construye un documento de un archivo "build(vmObjMyComunications)"
     * @throws IOException      cuando se lee un documento de un archivo "build(vmObjMyComunications)"
     * @throws KSException      cuando ocurre un RunTimeException
     */
    public void readCollectorConfiguration() throws JDOMException, IOException, KSException
    {
        List<Element> vlLstCollector;

        final Document vlObjDocument = vmObjBuilder.build(vmObjMyComunications);
        try
        {
            final Element vlElmRootNode = vlObjDocument.getRootElement();
            final Element vlElmCollector = vlElmRootNode.getChild("colectores");


            vlLstCollector = vlElmCollector.getChildren("colector");

            for (Element collectorValueTag : vlLstCollector)
            {
                CollectorModel vlObjCollectorModel = new CollectorModel();

                final Element vlEleReconnectionNode = collectorValueTag.getChild("reconection");
                List<Element> vlLstKeyField = collectorValueTag.getChild("saveKey").getChildren("field");

                vlObjCollectorModel.setPrimaryIp(collectorValueTag.getChildText("primaryIp"));
                vlObjCollectorModel.setPrimaryPort(Integer.parseInt(collectorValueTag.getChildText("primaryPort")));

                vlObjCollectorModel.setSecondaryIp(collectorValueTag.getChildText("secondaryIp"));
                vlObjCollectorModel.setSecondaryPort(Integer.parseInt(collectorValueTag.getChildText("secondaryPort")));

                vlObjCollectorModel.setConnectionType(ConnectionType.forValue(collectorValueTag.getChildText("type")));
                vlObjCollectorModel.setMessageType(Byte.parseByte(collectorValueTag.getChildText("messageType")));
                vlObjCollectorModel.setMessageLength(Byte.parseByte(collectorValueTag.getChildText("messageLength")));
                vlObjCollectorModel.setUser(Aes_128.desencriptar(collectorValueTag.getChildText("user")));
                vlObjCollectorModel.setResponseLength(Integer.parseInt(collectorValueTag.getChildText("responseLength")));
                vlObjCollectorModel.setResponseId(collectorValueTag.getChildText("responseId"));
                vlObjCollectorModel.setConfirmation(Boolean.valueOf(collectorValueTag.getChildText("confirmation")));

                vlObjCollectorModel.setReconnectionTime(Integer.parseInt(vlEleReconnectionNode.getAttributeValue("time")));
                vlObjCollectorModel.setReconnectionValue(Integer.parseInt(vlEleReconnectionNode.getAttributeValue("value")));

                vlLstKeyField.forEach(keyFieldTag -> {
                    KeyField keyField = new KeyField();

                    keyField.setVlStrId(keyFieldTag.getAttributeValue("id"));
                    keyField.setVlStrIdField(keyFieldTag.getAttributeValue("idField"));

                    vlObjCollectorModel.addKeyField(keyField);
                });

                vlObjCollectorModel.save();
            }
        }
        catch (RuntimeException ex)
        {
            throw new KSException("Ocurrió una excepción al intentar leer la configuración de collectores " + ex.getMessage());
        }
    }

    /**
     *
     * Método que lee información desde el archivo filters.xml  Necesarios para inicializar
     * pojo's de filtros en memoria
     *
     * @throws JDOMException    cuando se construye un documento de un archivo "build(vmObjMyComunications)"
     * @throws IOException      cuando se lee un documento de un archivo "build(vmObjMyComunications)"
     * @throws KSException      cuando ocurre un RunTimeException
     */
    public void ReadFilterConfiguration() throws JDOMException, IOException, KSException
    {
        final Document vlObjDocument = vmObjBuilder.build(vmObjMyArchiveFilter);
        List<Element> vlLstFilters;
        List<Element> vlLstFilter;
        List<Element> vlLstConditions;

        Element vlElmFilter;
        Element vlElmDataFilter;
        Element vlElmCondition;

        try
        {
            final Element vlElmRootNode = vlObjDocument.getRootElement();
            vlLstFilters = vlElmRootNode.getChildren("filters");

            /**
             * Obtiene datos de todos los filtros
             */
            for (Element filterValueTag : vlLstFilters)
            {
                FilterModel vlObjFilter = new FilterModel();
                vlElmFilter = filterValueTag;

                /**
                 * Obtiene los datos por filtro y puerto
                 */
                vlLstFilter = vlElmFilter.getChildren();

                for (Element filterValueTag2 : vlLstFilter)
                {
                    vlElmDataFilter = filterValueTag2;

                    /**
                     * Guardamos en la hash de filtros el número de puerto como llave
                     * y creamos el área de datos para las condiciones.
                     */
                    vlObjFilter.setPort(vlElmDataFilter.getAttributeValue("port"));

                    /**
                     * En caso de existir datos
                     */
                    if (vlElmFilter != null)
                    {
                        vlLstConditions = vlElmDataFilter.getChildren("condition");

                        for (Element filterValueTag3 : vlLstConditions)
                        {
                            /* El campo id se utiliza como campo de condición */
                            vlElmCondition = filterValueTag3;

                            /* El campo id se utiliza como campo de condición */
                            vlObjFilter.setId(vlElmCondition.getAttributeValue("id"));
                            vlObjFilter.setIdField(vlElmCondition.getAttributeValue("idField"));
                            vlObjFilter.setConnector(vlElmCondition.getAttributeValue("connector"));
                            vlObjFilter.setIsNegated(vlElmCondition.getAttribute("negate").getBooleanValue());
                            vlObjFilter.setOperator(vlElmCondition.getAttributeValue("operator"));
                            vlObjFilter.setParenthesis(vlElmCondition.getAttributeValue("parenthesis"));
                            vlObjFilter.setValue(vlElmCondition.getAttributeValue("value"));

                            vlObjFilter.save();
                        }
                    }
                }
            }
        }
        catch (RuntimeException ex)
        {
            throw new KSException("Ocurrió una excepción al intentar la lectura de filtros " + ex.getMessage());
        }
    }

    /**
     *
     * Método que lee información desde el archivo layout.xml  Necesarios para inicializar
     * pojo's de layout en memoria
     *
     * @throws JDOMException    cuando se construye un documento de un archivo "build(vmObjMyComunications)"
     * @throws IOException      cuando se lee un documento de un archivo "build(vmObjMyComunications)"
     * @throws KSException      cuando ocurre un RunTimeException
     */
    public void ReadLayoutConfiguration() throws JDOMException, IOException, KSException
    {
        final Document vlObjDocument = vmObjBuilder.build(vmObjMyArchiveLayoutField);
        List<Element> vlLstLayout;


        Element vlElmLayout;

        try
        {
            final Element vlElmRootNode = vlObjDocument.getRootElement();
            vlLstLayout = vlElmRootNode.getChild("fields").getChildren();

            for (Element LayoutTag : vlLstLayout)
            {
                final Field vlObjField = new Field();

                vlElmLayout = LayoutTag;

                if (vlElmLayout != null)
                {
                    vlObjField.setSequential(Integer.parseInt(vlElmLayout.getAttributeValue("sec")));
                    vlObjField.setIdField(Integer.parseInt(vlElmLayout.getAttributeValue("idField")));
                    vlObjField.setType(vlElmLayout.getAttributeValue("type"));
                    vlObjField.setName(vlElmLayout.getAttributeValue("name"));
                    vlObjField.setLength(Integer.parseInt(vlElmLayout.getAttributeValue("length")));
                    vmLayoutModel.save(vlObjField);
                }
            }
        }
        catch (RuntimeException ex)
        {
            throw new KSException("Ocurrió una excepción al intentar la lectura de layouts " + ex.getMessage());
        }
    }
}