package com.ks.FilterEvaluation;

import com.ks.lib.Configuracion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Counter
{
    private static final Logger DEPURACION = LogManager.getLogger(Counter.class.getName());

    private static final Counter _INSTANCE = new Counter();

    private static SimpleDateFormat vmDteDate;
    private static SimpleDateFormat vmDteDateHour;

    private static String vlStrQuery1;
    private static String vmStrBase;
    private static String vmStrDate;

    private static boolean vmBoolConnection;

    private static double vmDblReceived;
    private static double vmDblProcessed;
    private static double vmDblIgnored;
    private static double vmDblMemory;

    private static Connection vmCtnConnection;

    static
    {
        vmDteDate = new SimpleDateFormat("ddMMyyyy");
        vmDteDateHour = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        vmStrDate = "";
        vmDblReceived = 0;
        vmDblProcessed = 0;
        vmDblIgnored = 0;
        vmDblMemory = 0;
        vmCtnConnection = null;

        if (Configuracion.getRuta().contains("\\"))
        {
            vmStrBase = Configuracion.getRuta() + "base\\contadores.db";
        }
        else
        {
            vmStrBase = Configuracion.getRuta() + "base/contadores.db";
        }

    }

    private Counter()
    {
        vlStrQuery1 = "";
    }

    public static Counter getInstance()
    {
        return _INSTANCE;
    }

    public void register() throws ClassNotFoundException
    {
        Class.forName("org.sqlite.JDBC");
    }

    private void connect() throws SQLException
    {
        vmCtnConnection = DriverManager.getConnection("jdbc:sqlite:" + vmStrBase);
    }

    private void disconnect() throws SQLException
    {
        if (vmCtnConnection != null)
        {
            vmCtnConnection.close();
            vmCtnConnection = null;
        }
    }

    public void validateTable() throws SQLException
    {
        final String vlStrQuery1 = "SELECT * FROM ContadoresBloques WHERE Fecha = ? ";

        if (vmCtnConnection == null)
        {
            connect();
        }

        try (PreparedStatement preparedStatement = vmCtnConnection.prepareStatement(vlStrQuery1))
        {
            vmBoolConnection = true;
            preparedStatement.setString(1, vmDteDate.format(new Date()));

            try (ResultSet vlRlsData = preparedStatement.executeQuery())
            {
                while (vlRlsData.next())
                {
                    final String vlStrQuery2 = "INSERT INTO ContadoresBloques VALUES(?. ?, ?, ?. ?, ?)";

                    try (PreparedStatement preparedStatement2 = vmCtnConnection.prepareStatement(vlStrQuery2))
                    {
                        preparedStatement2.setString(1, vmDteDate.format(new Date()));
                        preparedStatement2.setInt(2, 0);
                        preparedStatement2.setInt(3, 0);
                        preparedStatement2.setInt(4, 0);
                        preparedStatement2.setInt(5, 0);
                        preparedStatement2.setString(6, vmDteDateHour.format(new Date()));

                        preparedStatement2.executeUpdate();
                    }
                    catch (SQLException ex)
                    {
                        DEPURACION.error("Error al actualizar contadores: " + ex.getMessage());
                    }
                }
            }
            catch (SQLException ex)
            {
                DEPURACION.error("Error interno de SQL (validar tablas): " + ex.getMessage());
            }
        }

        catch (SQLException ex)
        {
            DEPURACION.error("Error al validar contadores: " + ex.getMessage());
            DEPURACION.error("Sentencia error: " + Counter.vlStrQuery1);
        }
        finally
        {
            disconnect();
        }

    }

    public void readCounter() throws SQLException
    {
        final String vlStrQuery1 = "SELECT * FROM ContadoresBloques WHERE Fecha = ? ";

        if (vmCtnConnection == null)
        {
            connect();
        }

        try (PreparedStatement preparedStatement = vmCtnConnection.prepareStatement(vlStrQuery1))
        {
            preparedStatement.setString(1, vmDteDate.format(new Date()));

            try (ResultSet vlRlsData = preparedStatement.executeQuery())
            {
                while (vlRlsData.next())
                {
                    vmStrDate = vlRlsData.getString("Fecha");
                    vmDblReceived = vlRlsData.getDouble("Recibidos");
                    vmDblProcessed = vlRlsData.getDouble("Procesados");
                    vmDblIgnored = vlRlsData.getDouble("Ignorados");
                    vmDblMemory = vlRlsData.getDouble("Memoria");
                }
            }
            catch (SQLException ex)
            {
                DEPURACION.error("Error interno de SQL (leer tablas)");
            }

        }
        catch (SQLException ex)
        {
            DEPURACION.error("Error al leer contadores " + ex.getMessage());
        }
        finally
        {
            disconnect();
        }
    }

    public synchronized void updateReceivedCounter() throws SQLException
    {
        final String vlStrQuery1 = "UPDATE ContadoresBloques set Recibidos = Recibidos + 1, Ultimo = ? WHERE Fecha = ?";

        if (vmCtnConnection == null)
        {
            connect();
        }

        try (Connection vmCtnConnection = DriverManager.getConnection("jdbc:sqlite:" + vmStrBase); PreparedStatement preparedStatement = vmCtnConnection.prepareStatement(vlStrQuery1))
        {
            preparedStatement.setString(1, vmDteDateHour.format(new Date()));
            preparedStatement.setString(2, vmDteDateHour.format(new Date()));

            preparedStatement.executeUpdate();

        }
        catch (SQLException ex)
        {
            DEPURACION.error("Error al actualizar Recibidos: " + ex.getMessage());
            DEPURACION.error("Sentencia error: " + vlStrQuery1);
        }
        finally
        {
            disconnect();
        }

    }

    public synchronized void updateProccesedCounter(int counter) throws SQLException
    {
        final String vlStrQuery1 = "UPDATE ContadoresBloques SET Ignorados = Ignorados + 1, Ultimo = ? WHERE Fecha = ?";

        if (vmCtnConnection == null)
        {
            connect();
        }

        try (Connection vmCtnConnection = DriverManager.getConnection("jdbc:sqlite:" + vmStrBase); PreparedStatement preparedStatement = vmCtnConnection.prepareStatement(vlStrQuery1))
        {
            preparedStatement.setInt(1, counter);
            preparedStatement.setString(2, vmDteDateHour.format(new Date()));
            preparedStatement.setString(3, vmDteDateHour.format(new Date()));

            preparedStatement.executeUpdate();
        }
        catch (SQLException ex)
        {
            DEPURACION.error("Error al actualizar contadores: " + ex.getMessage());
            DEPURACION.error("Sentencia error: " + vlStrQuery1);
        }
        finally
        {
            disconnect();
        }
    }

    public synchronized void updateIgnoredCounter() throws SQLException
    {
        final String vlStrQuery1 = "UPDATE ContadoresBloques SET Ignorados = Ignorados + 1, Ultimo = ? where FECHA = ?";

        if (vmCtnConnection == null)
        {
            connect();
        }

        try (Connection vmCtnConnection = DriverManager.getConnection("jdbc:sqlite:" + vmStrBase); PreparedStatement preparedStatement = vmCtnConnection.prepareStatement(vlStrQuery1))
        {
            preparedStatement.setString(1, vmDteDateHour.format(new Date()));
            preparedStatement.setString(2, vmDteDate.format(new Date()));

            preparedStatement.executeUpdate();
        }
        catch (SQLException ex)
        {
            DEPURACION.error("Error al actualizar ignorados: " + ex.getMessage());
            DEPURACION.error("Sentencia error: " + vlStrQuery1);
        }
        finally
        {
            disconnect();
        }
    }

    public synchronized void updateMemoryCounter() throws SQLException
    {
        final String vlStrQuery1 = "UPDATE ContadoresBloques SET Memoria = Memoria + 1, Ultimo = ? where FECHA = ?";

        if (vmCtnConnection == null)
        {
            connect();
        }

        try (Connection vmCtnConnection = DriverManager.getConnection("jdbc:sqlite:" + vmStrBase); PreparedStatement preparedStatement = vmCtnConnection.prepareStatement(vlStrQuery1))
        {
            preparedStatement.setString(1, vmDteDateHour.format(new Date()));
            preparedStatement.setString(2, vmDteDate.format(new Date()));

            preparedStatement.executeUpdate();
        }
        catch (SQLException ex)
        {
            DEPURACION.error("Error al actualizar memoria: " + ex.getMessage() + "Sentencia error: " + vlStrQuery1);
        }
        finally
        {
            disconnect();
        }
    }
}
