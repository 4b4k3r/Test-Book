package com.ks.FilterEvaluation;

import java.util.Hashtable;

/**
 * Librerías para guardar fechas
 **/

public class DistributorModel
{
    private static final Hashtable<Integer, DistributorModel> DISTRIBUTOR_MODEL_HASH = new Hashtable<>();

    private Boolean containReplicador;
    private Boolean containTokens;

    private static int sequential;

    private String host;
    private String ip;
    private String name;
    private String app;
    private String user;
    private String lastSentTrxDate;
    private String lastProcessedTrxDate;

    private int id;
    private int port;
    private int totalProcessedTrx;
    private int totalReceivedTrx;
    private int hoursFromLogon;

    private ConnectionType connectionType;

    private static int layoutId;
    private static int formatId;

    static
    {
        layoutId = 0;
        formatId = 0;
    }

    public DistributorModel()
    {
        sequential++;
        containTokens = false;
    }

    public void save()
    {
        id = sequential;
        DISTRIBUTOR_MODEL_HASH.put(sequential, this);
    }

    public Boolean getContainReplicador()
    {
        return containReplicador;
    }

    public void setContainReplicador(Boolean containReplicador)
    {
        this.containReplicador = containReplicador;
    }

    public Boolean getContainTokens()
    {
        return containTokens;
    }

    public void setContainTokens(Boolean containTokens)
    {
        this.containTokens = containTokens;
    }

    public int getId()
    {
        return id;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public ConnectionType getConnectionType()
    {
        return connectionType;
    }

    public void setConnectionType(ConnectionType connectionType)
    {
        this.connectionType = connectionType;
    }

    public static int getLayoutId()
    {
        return layoutId;
    }

    public static void setLayoutId(int layoutId)
    {
        DistributorModel.layoutId = layoutId;
    }

    public static int getFormatId()
    {
        return formatId;
    }

    public static void setFormatId(int formatId)
    {
        DistributorModel.formatId = formatId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getApp()
    {
        return app;
    }

    public void setApp(String app)
    {
        this.app = app;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String getLastSentTrxDate()
    {
        return lastSentTrxDate;
    }

    public void setLastSentTrxDate(String lastSentTrxDate)
    {
        this.lastSentTrxDate = lastSentTrxDate;
    }

    public String getLastProcessedTrxDate()
    {
        return lastProcessedTrxDate;
    }

    public void setLastProcessedTrxDate(String lastProcessedTrxDate)
    {
        this.lastProcessedTrxDate = lastProcessedTrxDate;
    }

    public int getHoursFromLogon()
    {
        return hoursFromLogon;
    }

    public void setHoursFromLogon(int hoursFromLogon)
    {
        this.hoursFromLogon = hoursFromLogon;
    }

    public int getTotalReceivedTrx()
    {
        return totalReceivedTrx;
    }

    public void setTotalReceivedTrx(int totalReceivedTrx)
    {
        this.totalReceivedTrx = totalReceivedTrx;
    }

    public int getTotalProcessedTrx()
    {
        return totalProcessedTrx;
    }

    public void setTotalProcessedTrx(int totalProcessedTrx)
    {
        this.totalProcessedTrx = totalProcessedTrx;
    }

    public static Hashtable<Integer, DistributorModel> getDistributorModelHash()
    {
        return DISTRIBUTOR_MODEL_HASH;
    }
}
