package com.ks.FilterEvaluation;

import com.ks.lib.seguridad.Aes_128;

public class DataBase
{
    private static MotorBd dataBaseType;
    private static String serverHost;
    private static String baseName;
    private static String instance;
    private static String user;
    private static String password;

    static
    {
        serverHost = "";
        baseName = "";
        instance = "";
        user = "";
        password = "";
    }

    public DataBase()
    {

    }

    /**
     * Patron enum para determinar el motor BD a
     * utilizar
     * _SQLSERVER (1)
     * _MYSQL(2)
     * _ORACLE(3)
     * _XML(4)
     */
    public enum MotorBd
    {
        _SQLSERVER(1), _MYSQL(2), _ORACLE(3), _XML(4);

        private int vmIntMotoBdM;

        MotorBd(int vmIntMotoBdM)
        {
            this.vmIntMotoBdM = vmIntMotoBdM;
        }

        public static MotorBd fromValue(int vmIntMotoBdM)
        {
            final MotorBd vmEnumMotorBd;

            switch (vmIntMotoBdM)
            {
                case 1:
                    vmEnumMotorBd = _SQLSERVER;
                    break;
                case 2:
                    vmEnumMotorBd = _MYSQL;
                    break;
                case 3:
                    vmEnumMotorBd = _ORACLE;
                    break;
                case 4:
                    vmEnumMotorBd = _XML;
                    break;
                default:
                    vmEnumMotorBd = null;
                    break;
            }
            return vmEnumMotorBd;
        }
    }

    public static MotorBd getDataBaseType()
    {
        return dataBaseType;
    }

    public static void setDataBaseType(MotorBd dataBaseType)
    {
        DataBase.dataBaseType = dataBaseType;
    }

    public static String getServerHost()
    {
        return serverHost;
    }

    public static void setServerHost(String serverHost)
    {
        DataBase.serverHost = serverHost;
    }

    public static String getBaseName()
    {
        return baseName;
    }

    public static void setBaseName(String baseName)
    {
        DataBase.baseName = baseName;
    }

    public static String getInstance()
    {
        return instance;
    }

    public static void setInstance(String instance)
    {
        DataBase.instance = instance;
    }

    public static String getUser()
    {
        return user;
    }

    public static void setUser(String user)
    {
        DataBase.user = user;
    }

    public static String getPassword()
    {
        return password;
    }

    public static void setPassword(String password)
    {
        DataBase.password = Aes_128.desencriptar(password);
    }

    public static String getUrl()
    {
        String url = null;

        switch (DataBase.getDataBaseType())
        {
            case _SQLSERVER:
                url = "jdbc:sqlserver://" + DataBase.getServerHost() + ":1433";
                break;
            case _MYSQL:
                url = "jdbc:mysql://" + DataBase.getServerHost() + ":3306/" + DataBase.getBaseName() + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
                break;
            case _ORACLE:
                url = "jdbc:oracle:thin:@" + DataBase.getServerHost() + ":1521:" + DataBase.getBaseName();
                break;
            case _XML:
                /** Todo: Aun si se lee desde xml, se realiza la lectura de usuarios
                 *  en base de datos, debemos elegir sobre cual base de datos leer usuarios  **/
                url = "jdbc:mysql://" + DataBase.getServerHost() + ":3306/" + DataBase.getBaseName() + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
                break;
        }

        return url;
    }
}