package com.ks.FilterEvaluation;

/**
 * Created by Marco Calzada on 06/02/2018.
 */
public enum ConnectionType
{
    CLIENT, SERVER;

    public static ConnectionType forValue(String value) throws KSException
    {
        if (value.toUpperCase().equals("CLIENT"))
        {
            return CLIENT;
        }
        else if (value.toUpperCase().equals("SERVER"))
        {
            return SERVER;
        }
        else
        {
            throw new KSException("Tipo de conexion no valido: " + value);
        }
    }
}
