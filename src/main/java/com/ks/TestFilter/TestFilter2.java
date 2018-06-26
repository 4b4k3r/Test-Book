package com.ks.TestFilter;

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
        IGUAL("=", " == "), NO_IGUAL("<>", " != "), MENOR("<", " < "), MAYOR(">", " > "), MAYOR_IGUAL(">=", " >= "), MENOR_IGUAL("<=", " <= "), COMO("LIKE", ".equals"), Y("0", " && "), O("1", " || ");

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
        //dato se obtiene de los filtros y value se obtiene de los datos de la transaccion
        scriptBuilder("true", "<", "", "", "37", "60", "10");
        scriptBuilder("false", "LIKE", "1", "(", "aei", "ae*", "1");
        scriptBuilder("false", ">", "1", ")", "27", "5", "10");
        depurationProcess();
        evaluationProcess();
        System.out.println("Result TestFilter 1 (" + QUEUE_RESULTS.peek() + ") -> " + Script + " Evaluado en " + (System.currentTimeMillis() - inicio) + " milisegundos");
    }

    private void evaluationProcess()
    {

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
                QUEUE_OPERATORS.add(" ! ");
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
                value = (value.contains("*")) ? value.replace("*", "") : value;
                if (operador.equals(".equals"))
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
            depuratedList[i] = (QUEUE_OPERATORS.poll().contains(".")) ? QUEUE_RESULTS.poll() : depuratedList[i];
            Script += depuratedList[i];
        }
    }
}
