package com.ks.FilterEvaluation;

import java.util.Hashtable;

public class CollectorModel
{
    private static final Hashtable<Integer, CollectorModel> COLLECTOR_MODEL_HASHTABLE = new Hashtable<>();

    private Hashtable<String, KeyField> keyFields;
    private ConnectionType connectionType;
    private String primaryIp;
    private String secondaryIp;
    private String user;
    private String responseId;
    private String lastSentTrxDate;

    private Boolean confirmation;
    private Boolean sentLogon;

    private static int sequential;
    private int id;

    private int primaryPort;
    private int secondaryPort;
    private int responseLength;
    private int reconnectionTime;
    private int reconnectionValue;
    private int totalSentTrx;
    private int hoursFromLogon;

    private byte messageType;

    private byte messageLength;

    public CollectorModel()
    {
        sequential++;
        primaryIp = "";
        secondaryIp = "";
        user = "";
        responseId = "";
        primaryPort = 0;
        secondaryPort = 0;
        confirmation = false;
        responseLength = 0;
        reconnectionTime = 0;
        messageLength = 0;
        keyFields = new Hashtable<>();
    }

    public static Hashtable<Integer, CollectorModel> getCollectorModelHash()
    {
        return COLLECTOR_MODEL_HASHTABLE;
    }

    public void addKeyField(KeyField keyField)
    {
        keyFields.put(keyField.getVlStrId(), keyField);
    }

    public Hashtable<String, KeyField> getKeyFields()
    {
        return keyFields;
    }

    public void setKeyFields(Hashtable<String, KeyField> keyFields)
    {
        this.keyFields = keyFields;
    }

    public void save()
    {
        id = sequential;
        COLLECTOR_MODEL_HASHTABLE.put(id, this);
    }

    public ConnectionType getConnectionType()
    {
        return connectionType;
    }

    public void setConnectionType(ConnectionType connectionType)
    {
        this.connectionType = connectionType;
    }

    public Boolean getConfirmation()
    {
        return confirmation;
    }

    public void setConfirmation(Boolean confirmation)
    {
        this.confirmation = confirmation;
    }

    public Boolean getSentLogon()
    {
        return sentLogon;
    }

    public void setSentLogon(Boolean sentLogon)
    {
        this.sentLogon = sentLogon;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getPrimaryPort()
    {
        return primaryPort;
    }

    public void setPrimaryPort(int primaryPort)
    {
        this.primaryPort = primaryPort;
    }

    public int getResponseLength()
    {
        return responseLength;
    }

    public void setResponseLength(int responseLength)
    {
        this.responseLength = responseLength;
    }

    public int getReconnectionTime()
    {
        return reconnectionTime;
    }

    public void setReconnectionTime(int reconnectionTime)
    {
        this.reconnectionTime = reconnectionTime;
    }

    public String getPrimaryIp()
    {
        return primaryIp;
    }

    public void setPrimaryIp(String primaryIp)
    {
        this.primaryIp = primaryIp;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String getResponseId()
    {
        return responseId;
    }

    public void setResponseId(String responseId)
    {
        this.responseId = responseId;
    }

    public byte getMessageType()
    {
        return messageType;
    }

    public void setMessageType(byte type)
    {
        this.messageType = type;
    }

    public byte getMessageLength()
    {
        return messageLength;
    }

    public void setMessageLength(byte messageLength)
    {
        this.messageLength = messageLength;
    }

    public int getReconnectionValue()
    {
        return reconnectionValue;
    }

    public void setReconnectionValue(int reconnectionValue)
    {
        this.reconnectionValue = reconnectionValue;
    }

    public int getSecondaryPort()
    {
        return secondaryPort;
    }

    public void setSecondaryPort(int secondaryPort)
    {
        this.secondaryPort = secondaryPort;
    }

    public String getSecondaryIp()
    {
        return secondaryIp;
    }

    public void setSecondaryIp(String secondaryIp)
    {
        this.secondaryIp = secondaryIp;
    }

    public String getLastSentTrxDate()
    {
        return lastSentTrxDate;
    }

    public void setLastSentTrxDate(String lastSentTrxDate)
    {
        this.lastSentTrxDate = lastSentTrxDate;
    }

    public int getTotalSentTrx()
    {
        return totalSentTrx;
    }

    public void setTotalSentTrx(int totalSentTrx)
    {
        this.totalSentTrx = totalSentTrx;
    }

    public int getHoursFromLogon()
    {
        return hoursFromLogon;
    }

    public void setHoursFromLogon(int hoursFromLogon)
    {
        this.hoursFromLogon = hoursFromLogon;
    }
}
