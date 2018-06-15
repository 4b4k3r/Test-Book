package com.ks.FilterEvaluation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The <code>RequestManager</code> class
 * <p>
 * io.swagger.ks.messages
 * <p>
 * Created on 08/02/2018.
 *
 * @author Marco Calzada
 */

/**
 * Se encarga de separar las transacciones BFPROLAP de cada bloque
 */
public class RequestManager
{
    private static final Logger DEPURACION = LogManager.getLogger(RequestManager.class.getName());

    /**
     * Estructura de cola que guarda aquellos bloques pendientes por procesar
     */
    private static final Queue<String> MESSAGES_QUEUE = new LinkedList<>();
    private static final String KEYWORD_1 = "BFPROLAP";
    private static final String KEYWORD_2 = "SENDTRAN";
    /**
     * Hilo que procesa transacciones
     */
    private static Thread processRequestThread = new Thread(RequestManager::processRequest);

    /**
     * Método que inicializa el hilo que procesa cada transaccion
     *
     * @param message parametro que contiene en una cadena el bloque de transacciones BFPROLAP
     */
    public static void newTransaction(String message)
    {
        /**
         * Añadimos en la cola el bloque
         */
        MESSAGES_QUEUE.add(message);

        /**
         * Durante la duracion del programa, inicializamos el
         * hilo
         */
        processRequestThread = new Thread(RequestManager::processRequest);
        processRequestThread.setName("processRequestManager");
        processRequestThread.start();

    }

    /**
     * método del hilo que procesa transacciones BFPROLAP
     */
    private static void processRequest()
    {
        String message = "";
        String correctTransaction = "";

        int messageLength;
        int numberOfOcurrencesBFPROLAP;
        int numberOfOcurrencesSENDTRAN;

        boolean messageIncompleteFlag = false;

        while (!MESSAGES_QUEUE.isEmpty())
        {
            synchronized (MESSAGES_QUEUE)
            {
                message = MESSAGES_QUEUE.poll();
            }

            while (!message.isEmpty())
            {
                // Existen tres escenarios entre SENDTRAN y BFPPOLAP
                if (message.length() >= 20)
                {
                    if (message.substring((message.indexOf(KEYWORD_1) + 15), (message.indexOf(KEYWORD_1) + 20)).matches("\\d+"))
                    {
                        messageLength = Integer.parseInt(message.substring((message.indexOf(KEYWORD_1) + 15), (message.indexOf(KEYWORD_1) + 20)));

                        if ((messageLength - 16) <= message.length())
                        {
                            // No podemos asegurar que se tengan siempre los 16 caractéres anteriores
                            // al header
                            if (message.indexOf(KEYWORD_1) >= 16)
                            {
                                // Existe un header y comprobamos que no existe un mensaje SENDTRAN en él
                                if (!message.substring((message.indexOf(KEYWORD_1) - 16), (message.indexOf(KEYWORD_1))).matches("\\d+"))
                                {
                                    correctTransaction = message.substring(message.indexOf(KEYWORD_1), messageLength);
                                }
                                else
                                {
                                    correctTransaction = message.substring(message.indexOf(KEYWORD_1) - 16, messageLength);
                                }
                            }
                            else
                            {
                                // En este caso, no podemos asegurar que el tamaño total de la transacción BFPROLAP
                                // corresponda al tamaño dictado en su contenido
                                messageIncompleteFlag = true;

                                // Si no tiene header, es el tamaño de la transacción menos 16
                                correctTransaction = message.substring((message.indexOf(KEYWORD_1)), (messageLength - 16));
                            }

                            // contamos el número ocurrencias de BFPROLAP
                            numberOfOcurrencesBFPROLAP = correctTransaction.split(KEYWORD_1, -1).length - 1;

                            // Validamos que por transacción no haya más de un token BFPROLAP, de lo contrario, cortamos
                            // el mensaje y continuamos analizando la transacción desde el último BFPROLAP
                            if (numberOfOcurrencesBFPROLAP == 1)
                            {
                                // contamos el número de ocurrencias de SENDTRAN
                                numberOfOcurrencesSENDTRAN = correctTransaction.split(KEYWORD_2, -1).length - 1;

                                if (numberOfOcurrencesSENDTRAN == 1)
                                {
                                    message = message.substring(correctTransaction.length());
                                }
                                else
                                {
                                    if (messageIncompleteFlag != true)
                                    {
                                        message = message.substring(messageLength);
                                    }
                                    else
                                    {
                                        message = message.substring(messageLength - 16);
                                    }

                                    TransactionField.messageParse(correctTransaction);
                                    messageIncompleteFlag = false;
                                }
                            }
                            else
                            {
                                message = message.substring(message.indexOf(KEYWORD_1) + 8);

                                // Rescatamos la transacción a partir del último mensaje BFPROLAP
                                message = message.substring(message.indexOf(KEYWORD_1));
                            }
                        }
                        else
                        {
                            // Sabemos que el mensaje llegó incompleto
                            message = "";
                        }
                    }
                    // Existen dos escenarios; o se debe revisar la siguiente transacción
                    // BFPROLAP o, descartamos el mensaje SENDTRAN
                    else
                    {
                        if (message.contains(KEYWORD_1))
                        {
                            message = message.substring((message.indexOf(KEYWORD_1)) + 8);
                            message = message.substring(message.indexOf(KEYWORD_1));
                        }
                        else if (message.contains(KEYWORD_2))
                        {
                            // Revisamos si le sigue una transacción SENDTRAN
                            message = message.substring(message.indexOf(KEYWORD_2) + 8);

                            if (message.indexOf(KEYWORD_1) != -1)
                            {
                                message = message.substring(message.indexOf(KEYWORD_1) + 8);
                            }
                            else
                            {
                                message = "";
                            }
                        }
                        else if (!message.contains(KEYWORD_1) && !message.contains(KEYWORD_2))
                        {
                            message = "";
                        }
                    }
                }
                // El mensaje no tiene la longitud suficiente para ser procesado
                else
                {
                    DEPURACION.error("El mensaje no tiene el tamaño suficiente");
                    break;
                }
            }
        }
    }
}
