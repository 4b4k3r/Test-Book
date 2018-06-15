package com.ks.FilterEvaluation;

public class ConfigurationModel
{
    private static final ConfigurationModel _INSTANCE = new ConfigurationModel();
    private int guideType;
    private DataBase vlObjDataBase;

    public static ConfigurationModel getInstance()
    {
        return _INSTANCE;
    }

    public int getGuideType()
    {
        return guideType;
    }

    public void setGuideType(int guideType)
    {
        this.guideType = guideType;
    }

    public DataBase getVlObjDataBase()
    {
        return vlObjDataBase;
    }

    public void setVlObjDataBase(DataBase vlObjDataBase)
    {
        this.vlObjDataBase = vlObjDataBase;
    }

    public static boolean isTypeOne()
    {
        return ConfigurationModel.getInstance().getGuideType() == 1;
    }
}
