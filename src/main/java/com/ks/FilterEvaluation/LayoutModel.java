package com.ks.FilterEvaluation;

import java.util.Hashtable;
import java.util.Map;

public class LayoutModel
{
    private static final LayoutModel _INSTANCE = new LayoutModel();
    private static final Hashtable<Integer, Field> FIELD_HASHTABLE = new Hashtable<>();

    /**
     * Todo: guardamos dinámicamente la mayor secuencia
     **/
    private static int vmCurrentLayoutKey;
    private static int vmLargestLayoutKey;

    /**
     * Todo: guardamos dinámicamente el tamaño de los campos a leer
     **/
    private static int vmTotalLayoutField;

    public static LayoutModel getInstance()
    {
        return _INSTANCE;
    }

    public static Map<Integer, Field> getFieldHashtable()
    {
        return FIELD_HASHTABLE;
    }

    public void save(Field field)
    {
        FIELD_HASHTABLE.put(field.getSequential(), field);

        /** Validación del tamaño final del campo **/
        vmTotalLayoutField += field.getLength();

        /** Validación de la secuencia **/
        vmCurrentLayoutKey = field.getSequential();

        if (vmLargestLayoutKey == 0)
        {
            vmLargestLayoutKey = vmCurrentLayoutKey;
        }
        else
        {
            if (vmCurrentLayoutKey > vmLargestLayoutKey)
            {
                vmLargestLayoutKey = vmCurrentLayoutKey;
            }
        }
    }

    public static int getVmLargestLayoutKey()
    {
        return vmLargestLayoutKey;
    }

    public static int getVmTotalLayoutField()
    {
        return vmTotalLayoutField;
    }
}
