package com.ks.TestFilter;

import jboolexpr.BooleanExpression;
import jboolexpr.MalformedBooleanException;
import java.util.LinkedList;
import java.util.Queue;

public class TestFilter2
{
    private long inicio = System.currentTimeMillis();
    private Queue<String> QUEUE_OPERATORS = new LinkedList();
    private Queue<String> QUEUE_VALUES = new LinkedList();
    private Queue<Boolean> QUEUE_RESULTS = new LinkedList();
    private Object[] depuratedList;
    private String Script = "";

    private enum Operators
    {
        IGUAL("=", " == "), NO_IGUAL("<>", " != "), MENOR("<", " < "), MAYOR(">", " > "), MAYOR_IGUAL(">=", " >= "), MENOR_IGUAL("<=", " <= "), COMO("LIKE", ".equals"), Y("0", "&&"), O("1", "||");

        private String operador;
        private String operadorCodigo;

        Operators(String operador, String operadorCodigo)
        {
            this.operador = operador;
            this.operadorCodigo = operadorCodigo;
        }

        public String getOperadorCodigo()
        {
            return operadorCodigo;
        }

        public static Operators forValue(String value)
        {
            switch (value)
            {
                case "=":
                    return IGUAL;
                case "<>":
                    return NO_IGUAL;
                case "<":
                    return MENOR;
                case ">":
                    return MAYOR;
                case "<=":
                    return MENOR_IGUAL;
                case ">=":
                    return MAYOR_IGUAL;
                case "LIKE":
                    return COMO;
                case "0":
                    return Y;
                case "1":
                    return O;
                default:
                    return null;
            }
        }
    }

    public void datos()
    {
        //value se obtiene de los filtros y dato se obtiene del layout de la transaccion
        scriptBuilder("false", "<>", "", "", "03/06/2016", "03/05/2016", "30");
        scriptBuilder("false", "LIKE", "1", "(", "aei", "ai*", "1");
        scriptBuilder("false", ">", "0", "", "03/06/2018", "03/05/2012", "30");
        scriptBuilder("true", ">=", "1", "(", "25", "12", "10");
        scriptBuilder("false", "=", "0", "(", "16", "21", "11");
        scriptBuilder("true", "<=", "1", "", "79", "13226", "11");
        scriptBuilder("false", "<", "0", ")", "44032", "987", "10");
        scriptBuilder("true", "LIKE", "0", ")", "administrador", "admin*", "1");
        scriptBuilder("true", "<>", "1", ")", "27", "5", "11");
        scriptBuilder("false", "LIKE", "0", "", "EstadosUnidosMexicanos", "*Mexicanos", "11");
        depurationProcess();
        String strBoolExpr = Script;
        BooleanExpression boolExpr = null;
        boolean bool = false;
        try
        {
            boolExpr = BooleanExpression.readLeftToRight(strBoolExpr);
            bool = boolExpr.booleanValue();
        }
        catch (MalformedBooleanException e)
        {
            e.printStackTrace();
        }
        System.out.println("Result TestFilter 2 (" + bool + ") -> " + Script + " Evaluado en " + (System.currentTimeMillis() - inicio) + " milisegundos");
    }


    private void scriptBuilder(String negate, String operator, String connector, String parenthesis, String dato, String value, String type)
    {
        if (dato != null)
        {
            if (!connector.equals(""))
            {
                QUEUE_OPERATORS.add(Operators.forValue(connector).getOperadorCodigo());
            }
            if (negate.equals("true"))
            {
                QUEUE_OPERATORS.add("!");
            }
            if (parenthesis.equals("("))
            {
                QUEUE_OPERATORS.add(parenthesis);
            }
            QUEUE_OPERATORS.add(".");
            QUEUE_VALUES.add(type);
            QUEUE_VALUES.add(dato);
            QUEUE_VALUES.add(Operators.forValue(operator).getOperadorCodigo());
            QUEUE_VALUES.add(value);
            if (parenthesis.equals(")"))
            {
                QUEUE_OPERATORS.add(parenthesis);
            }
        }
    }

    private void depurationProcess()
    {

        while (!QUEUE_VALUES.isEmpty())
        {
            String type = QUEUE_VALUES.poll();
            String dato = QUEUE_VALUES.poll();
            String operador = QUEUE_VALUES.poll();
            String value = QUEUE_VALUES.poll();
            if (type.equals("10") || type.equals("11"))
            {
                if (operador.equals(" < ") && (Double.parseDouble(dato) < Double.parseDouble(value)))
                {
                    QUEUE_RESULTS.add(true);
                }
                else if (operador.equals(" > ") && (Double.parseDouble(dato) > Double.parseDouble(value)))
                {
                    QUEUE_RESULTS.add(true);
                }
                else if (operador.equals(" >= ") && (Double.parseDouble(dato) >= Double.parseDouble(value)))
                {
                    QUEUE_RESULTS.add(true);
                }
                else if (operador.equals(" <= ") && (Double.parseDouble(dato) <= Double.parseDouble(value)))
                {
                    QUEUE_RESULTS.add(true);
                }
                else if (operador.equals(" != ") && (dato != value))
                {
                    QUEUE_RESULTS.add(true);
                }
                else if (operador.equals(" == ") && (Double.parseDouble(dato) == Double.parseDouble(value)))
                {
                    QUEUE_RESULTS.add(true);
                }
                else
                {
                    QUEUE_RESULTS.add(false);
                }
            }
            else if (type.equals("1"))
            {
                if (value.contains("*"))
                {
                    value = value.replace("*", "");
                    if (value.endsWith("*"))
                    {
                        if (dato.startsWith(value))
                        {
                            QUEUE_RESULTS.add(true);
                        }
                        else
                        {
                            QUEUE_RESULTS.add(false);
                        }
                    }
                    else
                    {
                        if (dato.endsWith(value))
                        {
                            QUEUE_RESULTS.add(true);
                        }
                        else
                        {
                            QUEUE_RESULTS.add(false);
                        }
                    }
                }
                else
                {
                    if (dato.contains(value))
                    {
                        QUEUE_RESULTS.add(true);
                    }
                    else
                    {
                        QUEUE_RESULTS.add(false);
                    }
                }
            }
            else if (type.equals("30"))
            {
                int result = dato.compareTo(value);
                if (result > 0 && operador.equals(" > "))
                {
                    QUEUE_RESULTS.add(true);
                }
                else if (result == 0 && operador.equals(" = "))
                {
                    QUEUE_RESULTS.add(true);
                }
                else if (result < 0 && operador.equals(" < "))
                {
                    QUEUE_RESULTS.add(true);
                }
                else
                {
                    QUEUE_RESULTS.add(false);
                }
            }
        }

        depuratedList = QUEUE_OPERATORS.toArray();
        for (int i = 0; i < depuratedList.length; i++)
        {
            Script += (QUEUE_OPERATORS.poll().contains(".")) ? QUEUE_RESULTS.poll() : depuratedList[i];
        }
    }
}
