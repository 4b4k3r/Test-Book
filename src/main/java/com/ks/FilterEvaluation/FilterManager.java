package com.ks.FilterEvaluation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * The <code>FilterManager</code> class
 * <p>
 * io.swagger.ks
 * <p>
 * Created on 08/02/2018.
 *
 * @author Marco Calzada
 */

public class FilterManager implements Callable<Boolean>
{
    private static final Logger DEPURACION = LogManager.getLogger(CollectorController.class.getName());
    private static final Hashtable<String, Boolean> RESULTS_HASH = new Hashtable<>();
    private static Queue<String> MESSAGE_QUEUE = new LinkedList<>();
    private static Queue<String> PORTS_QUEUE = new LinkedList<>();
    private int idTransaction;

    public enum Operators
    {
        IGUAL("="), NO_IGUAL("<>"), MENOR("<"), MAYOR(">"), MAYOR_IGUAL(">="), MENOR_IGUAL("<="), COMO("LIKE");

        Operators(String operador)
        {
            this.operador = operador;
        }

        public String getOperador()
        {
            return operador;
        }

        private String operador;
    }


    public static void sendToDistributor()
    {
        synchronized (MESSAGE_QUEUE)
        {
            String mensaje = MESSAGE_QUEUE.poll();
            synchronized (PORTS_QUEUE)
            {
                while (!PORTS_QUEUE.isEmpty())
                {
                    int port = Integer.parseInt(PORTS_QUEUE.poll());
                    new Thread(() -> DistributorController.sendToDistributors(mensaje, port), "collectorSender").start();
                }
            }
        }
    }

    @Override
    public Boolean call()
    {
        String Result = "";
        Boolean Action = false;
        Hashtable<Integer, DistributorModel> distributorModel = DistributorModel.getDistributorModelHash();
        int i = 1;
        while (distributorModel.elements().hasMoreElements())
        {
            int puerto = distributorModel.get(i).getPort();
            Set<FilterModel> filterModels = FilterModel.getPortFilters(String.valueOf(puerto));
            String dato = " ", value, connector, operator, parenthesis;
            Boolean negate;
            for (FilterModel filterModel : filterModels)
            {
                int idField = Integer.parseInt(filterModel.getIdField());
                connector = filterModel.getConnector();
                negate = filterModel.getIsNegated();
                operator = filterModel.getOperator();
                dato = TransactionField.getField(idField);
                parenthesis = filterModel.getParenthesis();

                if (dato == null)
                {
                    DEPURACION.error("Error en la evaluación del dato porque es nullo ");
                    break;
                }
                value = filterModel.getValue();

                if (connector.equals("Y"))
                {
                    Result += "&&";
                }

                if (negate.equals("true"))
                {
                    Result += "!";
                }

                if (parenthesis.equals("(") || parenthesis.equals(")"))
                {
                    Result += parenthesis;
                }
                else
                {
                    if (operator != null)
                    {
                        if (Operators.IGUAL.getOperador().equals(operator))
                        {
                            Result = dato + " == " + value;
                            break;
                        }
                        if (Operators.NO_IGUAL.getOperador().equals(operator))
                        {
                            Result = dato + " != " + value;
                            break;
                        }
                        if (Operators.MENOR.getOperador().equals(operator))
                        {
                            Result = dato + " < " + value;
                            break;
                        }
                        if (Operators.MENOR_IGUAL.getOperador().equals(operator))
                        {
                            Result = dato + " <= " + value;
                            break;
                        }
                        if (Operators.MAYOR.getOperador().equals(operator))
                        {
                            Result = dato + " > " + value;
                            break;
                        }
                        if (Operators.MAYOR_IGUAL.getOperador().equals(operator))
                        {
                            Result = dato + " >= " + value;
                            break;
                        }
                        if (Operators.COMO.getOperador().equals(operator))
                        {
                            if (value.contains("*"))
                            {
                                if (value.startsWith("*"))
                                {
                                    Result = dato + " like *" + value;
                                }
                                else
                                {
                                    Result = dato + " like " + value + "*";
                                }
                            }
                            else
                            {
                                Result = dato + " like " + value;
                            }
                        }
                    }
                }

            }
            String model = "function(){ if(";
            model += Result;
            model += "){return true;}else {return false;}}";
            System.out.println(model);
            Action = Evaluate(model);
            if (Action)
            {
                if (MESSAGE_QUEUE.isEmpty())
                {
                    MESSAGE_QUEUE = (Queue<String>) TransactionField.getMessage();
                }
                synchronized (PORTS_QUEUE)
                {
                    PORTS_QUEUE.add(String.valueOf(puerto));
                }
            }

            i++;
        }
        return Action;
    }

    private boolean Evaluate(String sentencia)
    {
        boolean resultado = false;

        try
        {
            resultado = Boolean.parseBoolean(String.valueOf(new ScriptEngineManager().getEngineByName("javascript").eval(sentencia))) ;
            return resultado;
        }
        catch (ScriptException ex)
        {
            DEPURACION.error("Error en la evaluación de la sentencia: " + ex.getMessage());
        }
        catch (Exception ex)
        {
            DEPURACION.error("Error en la evaluación de la sentencia: " + ex.getMessage());
        }
        return resultado;
    }

    private Hashtable<String, Boolean> getResults()
    {
        return RESULTS_HASH;
    }
}