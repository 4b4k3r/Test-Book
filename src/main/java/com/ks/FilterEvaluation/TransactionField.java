package com.ks.FilterEvaluation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Hashtable;


/**
 * Se encarga de procesar la transaccion BFPROLAP y asignar dependiendo de la longitud
 * definida en el layout, cortar el mensaje y asignar el fragmento adecuado a los campos
 * tambien especificados en layout
 */
public class TransactionField
{
    private static Logger DEPURACION = LogManager.getLogger(TransactionField.class.getName());

    /**
     * Hashtable que posee los campos leidos desde layout
     */
    private static final Hashtable<Integer, String> LAYOUT_MODEL_FIELDS_HASH = new Hashtable<>();

    /**
     * Metodo que procesa la transaccion BFPROLAP y asigna los fragmentos correpondientes a los campos
     * definidos en layout y que a su vez, lo guarda en memoria
     *
     * @param message transaccion a analizar
     */
    public static void messageParse(String message)
    {
        int vlIntCounter = 0;
        String vlStrTemp = "";

        for (int i = 1; i <= LayoutModel.getVmLargestLayoutKey(); i++)
        {
            try
            {
                vlStrTemp = message.substring(vlIntCounter, vlIntCounter + LayoutModel.getFieldHashtable().get(i).getLength());
            }
            catch (IndexOutOfBoundsException ex)
            {
                DEPURACION.error("El mensaje no tiene la longitud especificada: " + ex.getMessage());
            }


            synchronized (LAYOUT_MODEL_FIELDS_HASH)
            {
                LAYOUT_MODEL_FIELDS_HASH.put(LayoutModel.getFieldHashtable().get(i).getIdField(), vlStrTemp);
            }

            vlIntCounter += LayoutModel.getFieldHashtable().get(i).getLength();
        }
        DistributorController.evaluateFilter();
    }

    /**
     * Metodo que regresa el valor de un campo layout en particular. este ya se encuentra
     * en memoria
     *
     * @param id clave que identifica a un campo unico de los definidos en layout
     * @return regresamos el string asociado a un campo
     */
    public static String getField(int id)
    {
        return LAYOUT_MODEL_FIELDS_HASH.get(id);
    }

    public static Hashtable<Integer, String> getMessage(){
        return LAYOUT_MODEL_FIELDS_HASH;
    }
}

