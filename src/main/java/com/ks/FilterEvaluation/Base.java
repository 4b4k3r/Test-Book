package com.ks.FilterEvaluation;
/**
 * Created by Marco Calzada on 31/01/2018.
 * <p>
 * The <code>DataBase</code> class
 * <p>
 * io.swagger.ks.dataBase
 * <p>
 * Created on 31/01/2018.
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/**
 * Se encarga de la lectura de configuracion desde Base de Datos "SQL-SERVER, MY-SQL, "ORACLE", "XML" Dado que la
 * configuracion es universal para cada colector o distribuidor, se
 * hace de esta clase un singleton
 */
public class Base
{
    private static final Logger DEPURACION = LogManager.getLogger(Base.class.getName());
    private static final Base _INSTANCE = new Base();

    private static Connection vmCtnConnection;

    private final LayoutModel vmLayoutModel = LayoutModel.getInstance();


    static
    {
        vmCtnConnection = null;
    }

    private Object Fields;

    private Base()
    {
    }

    public static Base getInstance()
    {
        return _INSTANCE;
    }

    /**
     * Metodo que carga drivers en memoria y regresa la referencia como una
     * instancia de la clase
     *
     * @throws ClassNotFoundException   cuando no se encontro la clase indicada
     * @throws IllegalAccessException   cuando no es accesible el constructor por defecto de la clase
     * @throws InstantiationException   cuando la clase es abstracta o una interface y no tiene un constructor por defecto
     */
    public void register() throws ClassNotFoundException, IllegalAccessException, InstantiationException
    {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Class.forName("com.mysql.cj.jdbc.Driver");
        Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
    }

    /**
     *
     * Metodo que invoca a DriverManager y que por consiguiente, lleva el seguimiento de los
     *  controladores disponibles y maneja las solicitudes de conexion entre controladores
     *  adecuados y bases de datos o servidores de bases de datos.
     *
     * @throws SQLException cuando se produce un error al acceder a un origen de datos
     */
    private void connect() throws SQLException
    {
        vmCtnConnection = DriverManager.getConnection(DataBase.getUrl(), DataBase.getUser(), DataBase.getPassword());
    }

    /**
     *
     * Metodo que desconecta cualquier conexion existente a una base de datos
     *
     * @throws SQLException cuando se produce un error al acceder a un origen de datos
     */
    private void disconnect() throws SQLException
    {
        if (vmCtnConnection != null)
        {
            vmCtnConnection.close();
            vmCtnConnection = null;
        }
    }

    /**
     *
     * Metodo sobrecargado que se encarga de invocar a su funcion homologa con un parametro
     * null (Para buscar de manera general a todos los usuarios de una base de datos
     *
     * @throws SQLException cuando se produce un error al acceder a un origen de datos
     */
    public void readUsers() throws SQLException
    {
        this.readUser(null);
    }

    /**
     * Metodo que busca usuarios en la base de datos
     *
     * @param user
     * @throws SQLException cuando se produce un error al acceder a un origen de datos
     */
    public void readUser(String user) throws SQLException
    {
        final String vlStrQuery;

        if (user == null)
        {
            vlStrQuery = "SELECT * FROM users ";
        }
        else
        {
            vlStrQuery = "SELECT *  from users where user_name = ?";
        }

        if (vmCtnConnection == null)
        {
            connect();
        }

        try (PreparedStatement preparedStatement = vmCtnConnection.prepareStatement(vlStrQuery))
        {
            /**
             * Evitamos el error, values not bound to statement
             */

            try (ResultSet vlRlsUsers = preparedStatement.executeQuery())
            {
                while (vlRlsUsers.next())
                {
                    final User vlObjUser = new User();

                    vlObjUser.setIdUsername(vlRlsUsers.getString("user_name").trim().substring(0, 6));
                    vlObjUser.setUserName(vlRlsUsers.getString("user_name"));
                    vlObjUser.setName(vlRlsUsers.getString("name"));
                    vlObjUser.setEmail(vlRlsUsers.getString("email"));
                    vlObjUser.setUpdatedAt(vlRlsUsers.getString("updated_at"));

                    vlObjUser.save();

                }
            }
            catch (SQLException ex)
            {
                DEPURACION.error("Error interno de SQL (leer usuarios) " + ex.getMessage());
            }
        }
        catch (SQLException ex)
        {
            DEPURACION.error("Error al actualizar memoria: " + ex.getMessage());
            DEPURACION.error("Sentencia error: " + vlStrQuery);
        }
        finally
        {
            disconnect();
        }
    }

    /**
     *
     * Metodo que lee informacion necesaria desde base de datos para inicializar
     * pojo's de distribuidor en memoria
     *
     * @throws SQLException cuando se produce un error al acceder a un origen de datos
     */
    public void readDistributorConfiguration() throws SQLException
    {
        final String vlStrQuery = "SELECT * FROM filter_ports";

        if (vmCtnConnection == null)
        {
            connect();
        }

        try (Statement vlStmFilterPorts = vmCtnConnection.createStatement(); ResultSet vlRlsFilterPorts = vlStmFilterPorts.executeQuery(vlStrQuery))
        {
            final DistributorModel vlObjDistributorModel = new DistributorModel();

            while (vlRlsFilterPorts.next())
            {
                vlObjDistributorModel.setIp(vlRlsFilterPorts.getString("ip"));
                vlObjDistributorModel.setPort(vlRlsFilterPorts.getInt("port"));
                vlObjDistributorModel.setConnectionType(ConnectionType.valueOf(vlRlsFilterPorts.getString("connectionType")));
                vlObjDistributorModel.setContainTokens(vlRlsFilterPorts.getBoolean("token"));

                vlObjDistributorModel.save();
            }
        }
        catch (SQLException ex)
        {
            DEPURACION.error("Sentencia error: " + vlStrQuery + ex.getMessage());
        }
        finally
        {
            disconnect();
        }
    }

    /**
     *
     * Metodo que lee informacion necesaria desde base de datos para inicializar
     * pojo's de colector en memoria
     *
     * @throws SQLException cuando se produce un error al acceder a un origen de datos
     */
    public void readCollectorConfiguration() throws SQLException
    {
        CollectorModel vlObjCollectorModel;

        final String vlStrCollectorsQuery = "SELECT * FROM collector";

        if (vmCtnConnection == null)
        {
            connect();
        }

        try (Statement vlStmCollectors = vmCtnConnection.createStatement())
        {
            try (ResultSet vlRlsCollectors = vlStmCollectors.executeQuery(vlStrCollectorsQuery))
            {
                while (vlRlsCollectors.next())
                {
                    vlObjCollectorModel = new CollectorModel();

                    final int id = vlRlsCollectors.getInt("collectorId");

                    vlObjCollectorModel.setPrimaryIp(vlRlsCollectors.getString("primaryIp"));
                    vlObjCollectorModel.setPrimaryPort(vlRlsCollectors.getInt("primaryPort"));
                    vlObjCollectorModel.setSecondaryIp(vlRlsCollectors.getString("secondaryIp"));
                    vlObjCollectorModel.setConnectionType(ConnectionType.valueOf(vlRlsCollectors.getString("connectionType")));
                    vlObjCollectorModel.setMessageType(vlRlsCollectors.getByte("messageType"));
                    vlObjCollectorModel.setMessageLength(vlRlsCollectors.getByte("messageLength"));
                    vlObjCollectorModel.setUser(vlRlsCollectors.getString("userName"));
                    vlObjCollectorModel.setResponseLength(vlRlsCollectors.getInt("responseLength"));
                    vlObjCollectorModel.setResponseId(vlRlsCollectors.getString("responseId"));
                    vlObjCollectorModel.setConfirmation(vlRlsCollectors.getBoolean("confirmation"));
                    vlObjCollectorModel.setReconnectionTime(vlRlsCollectors.getInt("reconnectionTime"));
                    vlObjCollectorModel.setReconnectionValue(vlRlsCollectors.getInt("reconnectionValue"));

                    final String vlStrKeyQuery = "SELECT * FROM save_keys WHERE collectorId  = ?";

                    try (PreparedStatement vlStmSaveKey = vmCtnConnection.prepareStatement(vlStrKeyQuery))
                    {
                        vlStmSaveKey.setInt(1, id);

                        try (ResultSet vlRlsSaveKey = vlStmSaveKey.executeQuery())
                        {
                            while (vlRlsSaveKey.next())
                            {
                                KeyField vlObjKeyField = new KeyField();

                                vlObjKeyField.setVlStrId(String.valueOf((vlRlsSaveKey.getInt("saveKey_id"))));
                                vlObjKeyField.setVlStrIdField(String.valueOf((vlRlsSaveKey.getInt("fieldLay_id"))));

                                vlObjCollectorModel.addKeyField(vlObjKeyField);
                            }
                            vlObjCollectorModel.save();
                        }
                        catch (SQLException ex)
                        {
                            DEPURACION.error("Sentencia error: " + vlStrKeyQuery + ex.getMessage());
                        }
                    }
                    catch (SQLException ex)
                    {
                        DEPURACION.error("Sentencia error: " + vlStrKeyQuery + ex.getMessage());
                    }
                }
            }
            catch (SQLException ex)
            {
                DEPURACION.error("Error interno de SQL (leer configuracion del colector) " + ex.getMessage());
            }
        }
        catch (SQLException ex)
        {
            DEPURACION.error("Sentencia error: " + vlStrCollectorsQuery + ex.getMessage());
        }
        finally
        {
            disconnect();
        }
    }


    /**
     *
     * Metodo que lee informacion necesaria desde base de datos para inicializar
     * pojo's de filtros en memoria
     *
     * @throws SQLException cuando se produce un error al acceder a un origen de datos
     * @throws KSException  cuando ocurre un RunTimeException
     */
    public void readFilterConfiguration() throws SQLException, KSException
    {
        final String vlStrQuery = "SELECT a.*, b.* FROM filter_ports a, filter_port_details b " + "WHERE a.id = b.id";

        if (vmCtnConnection == null)
        {
            connect();
        }

        try (Statement vlStmFilter = vmCtnConnection.createStatement(); ResultSet vlRlsDataFilter = vlStmFilter.executeQuery(vlStrQuery))
        {
            while (vlRlsDataFilter.next())
            {
                FilterModel vlObjFilter = new FilterModel();

                vlObjFilter.setId(String.valueOf(vlRlsDataFilter.getInt("id")));
                vlObjFilter.setIdField(String.valueOf(vlRlsDataFilter.getInt("fieldLay_id")));
                vlObjFilter.setConnector(vlRlsDataFilter.getString("connector"));
                vlObjFilter.setIsNegated(vlRlsDataFilter.getBoolean("denied"));
                vlObjFilter.setOperator(vlRlsDataFilter.getString("operator"));
                vlObjFilter.setParenthesis(vlRlsDataFilter.getString("parenthesis"));
                vlObjFilter.setValue(vlRlsDataFilter.getString("value"));
                vlObjFilter.setPort(vlRlsDataFilter.getString("port"));

                vlObjFilter.save();
            }
        }
        catch (SQLException ex)
        {
            DEPURACION.error("Sentencia error: " + vlStrQuery + ex.getMessage());
        }
        catch (RuntimeException ex)
        {
            throw new KSException("Ocurrio una excepcion al intentar la lectura de filtros " + ex.getMessage());
        }
        finally
        {
            disconnect();
        }
    }

    /**
     *
     * Metodo que lee informacion necesaria desde base de datos para inicializar
     * pojo's de layout en memoria
     *
     * @throws SQLException cuando se produce un error al acceder a un origen de datos
     */
    public void readLayoutConfiguration() throws SQLException
    {
        final String vlStrQuery = "SELECT b.*, a.name FROM field_lays a, field_formats b " + "WHERE b.lay_id = ? AND b.format_id = ? " + "AND a.lay_id = b.lay_id " + "AND a.id = b.fieldLay_id GROUP BY b.fieldLay_id, a.name";

        if (vmCtnConnection == null)
        {
            connect();
        }

        try (PreparedStatement vlStmLayout = vmCtnConnection.prepareStatement(vlStrQuery))
        {
            vlStmLayout.setInt(1, 1);
            vlStmLayout.setInt(2, 1);

            try (ResultSet vlRlsLayoutData = vlStmLayout.executeQuery())
            {
                while (vlRlsLayoutData.next())
                {

                    final Field vlObjLayout = new Field();

                    vlObjLayout.setSequential(vlRlsLayoutData.getInt("consecutive"));
                    vlObjLayout.setIdField(vlRlsLayoutData.getInt("fieldLay_id"));
                    vlObjLayout.setType(vlRlsLayoutData.getString("field_type"));
                    vlObjLayout.setName(vlRlsLayoutData.getString("name"));
                    vlObjLayout.setLength(vlRlsLayoutData.getInt("length"));

                    vmLayoutModel.save(vlObjLayout);
                }
            }
            catch (SQLException ex)
            {
                DEPURACION.error("Error interno de SQL (leer configuracion de Campos) " + ex.getMessage());
            }
        }
        catch (SQLException ex)
        {
            DEPURACION.error("Sentencia error: " + vlStrQuery + ex.getMessage());
        }
        finally
        {
            disconnect();
        }
    }
}
