package com.ks.TestFilter;

import java.util.LinkedList;
import java.util.Queue;

public class TestFilter2
{
    private long inicio = System.currentTimeMillis();
    private Queue<String> QUEUE_OPERATORS = new LinkedList();
    private Queue<String> QUEUE_VALUES = new LinkedList();
    private Queue<Boolean> QUEUE_RESULTS = new LinkedList();
    private Queue<Boolean> QUEUE_RESULTS_FINAL = new LinkedList();

    private enum Operators
    {
        IGUAL("=", " == "), NO_IGUAL("<>", " != "), MENOR("<", " < "), MAYOR(">", " > "), MAYOR_IGUAL(">=", " >= "), MENOR_IGUAL("<=", " <= "), COMO("LIKE", ".equals"), Y("0", " && "), O("1", " || "), NEGATE("true", "!");

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
                case "true":
                    return NEGATE;
                default:
                    return null;
            }
        }
    }

    public void datos()
    {
        listData("false", ">", "", "(", "27/06/2017", "25/07/2016", "30");
        listData("false", "LIKE", "0", "", "Z264", "Z264*", "1");
        listData("false", "LIKE", "0", ")", "B003*", "B0030606", "1");
        listData("false", "<>", "1", "(", "35", "96", "10");
        listData("false", "<=", "0", ")", "5", "16", "10");
        listData("false", "<", "1", "(", "5", "16", "10");
        listData("false", ">", "0", ")", "5", "16", "10");
        depurationProccess();
        EvaluationProccess();
        System.out.println("Result TestFilter 1 (" + QUEUE_RESULTS_FINAL.peek() + ") ->  Evaluado en " + (System.currentTimeMillis() - inicio) + " milisegundos");
    }

    private void listData(String negate, String operator, String connector, String parenthesis, String value, String dato, String type)
    {
        if (dato != null)
        {
            if (negate.equals("true"))
            {
                QUEUE_OPERATORS.add(Operators.forValue(negate).getOperadorCodigo());
            }
            if (!connector.equals(""))
            {
                QUEUE_OPERATORS.add(Operators.forValue(connector).getOperadorCodigo());
            }
            if (parenthesis.equals("(") || parenthesis.equals(")"))
            {
                QUEUE_OPERATORS.add(parenthesis);
            }
            QUEUE_VALUES.add(type);
            QUEUE_VALUES.add(dato);
            QUEUE_VALUES.add(Operators.forValue(operator).getOperadorCodigo());
            QUEUE_VALUES.add(value);
        }
    }

    private void depurationProccess()
    {

        while (!QUEUE_VALUES.isEmpty())
        {
            String type = QUEUE_VALUES.poll();
            if (type.equals("10") || type.equals("11"))
            {
                double val1 = Double.parseDouble(QUEUE_VALUES.poll());
                String operador = QUEUE_VALUES.poll();
                double val12 = Double.parseDouble(QUEUE_VALUES.poll());
                if (operador.equals(" < "))
                {
                    if (val1 < val12)
                    {
                        QUEUE_RESULTS.add(true);
                    }
                    else
                    {
                        QUEUE_RESULTS.add(false);
                    }
                }
                else if (operador.equals(" > "))
                {
                    if (val1 > val12)
                    {
                        QUEUE_RESULTS.add(true);
                    }
                    else
                    {
                        QUEUE_RESULTS.add(false);
                    }
                }
                else if (operador.equals(" >= "))
                {
                    if (val1 >= val12)
                    {
                        QUEUE_RESULTS.add(true);
                    }
                    else
                    {
                        QUEUE_RESULTS.add(false);
                    }
                }
                else if (operador.equals(" <= "))
                {
                    if (val1 <= val12)
                    {
                        QUEUE_RESULTS.add(true);
                    }
                    else
                    {
                        QUEUE_RESULTS.add(false);
                    }
                }
                else if (operador.equals(" != "))
                {
                    if (val1 != val12)
                    {
                        QUEUE_RESULTS.add(true);
                    }
                    else
                    {
                        QUEUE_RESULTS.add(false);
                    }
                }
                else if (operador.equals(" == "))
                {
                    if (val1 > val12)
                    {
                        QUEUE_RESULTS.add(true);
                    }
                    else
                    {
                        QUEUE_RESULTS.add(false);
                    }
                }
            }
            else if (type.equals("1"))
            {
                String val12 = QUEUE_VALUES.poll();
                String operador = QUEUE_VALUES.poll();
                String val1 = QUEUE_VALUES.poll();
                if (operador.equals(".equals"))
                {
                    if (val12.contains(val1))
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
                String fecha1 = QUEUE_VALUES.poll();
                String operador = QUEUE_VALUES.poll();
                String fecha2 = QUEUE_VALUES.poll();
                int result = fecha1.compareTo(fecha2);
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
    }

    private Boolean EvaluationProccess()
    {
        while (!QUEUE_OPERATORS.isEmpty())
        {
            String elemento = QUEUE_OPERATORS.poll();
            if (QUEUE_OPERATORS.size()==0){
                break;
            }
            if (QUEUE_RESULTS_FINAL.isEmpty() || (QUEUE_RESULTS_FINAL.size() > 0 && elemento.equals(")")))
            {
                if (elemento.equals(")"))
                {
                    elemento = QUEUE_OPERATORS.poll();
                }
                if (elemento.equals("!"))
                {
                    elemento = QUEUE_OPERATORS.poll();
                    if (elemento.equals("("))
                    {
                        elemento = QUEUE_OPERATORS.poll();
                        Boolean d1 = QUEUE_RESULTS.poll();
                        Boolean d2 = QUEUE_RESULTS.poll();
                        if (elemento.equals(" && "))
                        {
                            if (d1 && d2)
                            {
                                QUEUE_RESULTS_FINAL.add(true);
                            }
                            else
                            {
                                QUEUE_RESULTS_FINAL.add(false);
                            }
                        }
                        else
                        {
                            if (d1 || d2)
                            {
                                QUEUE_RESULTS_FINAL.add(true);
                            }
                            else
                            {
                                QUEUE_RESULTS_FINAL.add(false);
                            }
                        }
                    }
                    else if (elemento.equals(" && "))

                    {
                        Boolean d1 = QUEUE_RESULTS.poll();
                        Boolean d2 = QUEUE_RESULTS.poll();
                        if (d1 && d2)
                        {
                            QUEUE_RESULTS_FINAL.add(true);
                        }
                        else
                        {
                            QUEUE_RESULTS_FINAL.add(false);
                        }
                    }
                    else
                    {
                        Boolean d1 = QUEUE_RESULTS.poll();
                        Boolean d2 = QUEUE_RESULTS.poll();
                        if (QUEUE_RESULTS.poll() || QUEUE_RESULTS.poll())
                        {
                            QUEUE_RESULTS_FINAL.add(true);
                        }
                        else
                        {
                            QUEUE_RESULTS_FINAL.add(false);
                        }
                    }
                }
                else if (elemento.equals("("))
                {
                    elemento = QUEUE_OPERATORS.poll();
                    Boolean d1 = QUEUE_RESULTS.poll();
                    Boolean d2 = QUEUE_RESULTS.poll();
                    if (elemento.equals(" && "))
                    {
                        if (d1 && d2)
                        {
                            QUEUE_RESULTS_FINAL.add(true);
                        }
                        else
                        {
                            QUEUE_RESULTS_FINAL.add(false);
                        }
                    }
                    else
                    {
                        if (d1 || d2)
                        {
                            QUEUE_RESULTS_FINAL.add(true);
                        }
                        else
                        {
                            QUEUE_RESULTS_FINAL.add(false);
                        }
                    }
                }
                else if (elemento.equals(" && "))
                {
                    Boolean d1 = QUEUE_RESULTS.poll();
                    Boolean d2 = QUEUE_RESULTS.poll();
                    if (d1 && d2)
                    {
                        QUEUE_RESULTS_FINAL.add(true);
                    }
                    else
                    {
                        QUEUE_RESULTS_FINAL.add(false);
                    }
                }
                else
                {
                    elemento = QUEUE_OPERATORS.poll();
                    if (elemento.equals("("))
                    {
                        Boolean d1 = QUEUE_RESULTS.poll();
                        Boolean d2 = QUEUE_RESULTS.poll();
                        if (d1 || d2)
                        {
                            QUEUE_RESULTS_FINAL.add(true);
                        }
                        else
                        {
                            QUEUE_RESULTS_FINAL.add(false);
                        }
                    }
                    else
                    {
                        Boolean d1 = QUEUE_RESULTS.poll();
                        Boolean d2 = QUEUE_RESULTS.poll();
                        if (d1 || d2)
                        {
                            QUEUE_RESULTS_FINAL.add(true);
                        }
                        else
                        {
                            QUEUE_RESULTS_FINAL.add(false);
                        }
                    }
                }
            }
            else
            {
                if (elemento.equals(")"))
                {
                    elemento = QUEUE_OPERATORS.poll();
                }
                else if (elemento.equals("!"))
                {
                    elemento = QUEUE_OPERATORS.poll();
                    if (elemento.equals("("))
                    {
                        elemento = QUEUE_OPERATORS.poll();
                        if (elemento.equals(" && "))
                        {
                            Boolean d1 = QUEUE_RESULTS.poll();
                            Boolean d2 = QUEUE_RESULTS.poll();
                            if (d1 && d2)
                            {
                                QUEUE_RESULTS_FINAL.add(true);
                            }
                        }
                        else
                        {
                            Boolean d1 = QUEUE_RESULTS.poll();
                            Boolean d2 = QUEUE_RESULTS.poll();
                            if (d1 || d2)
                            {
                                QUEUE_RESULTS_FINAL.add(true);
                            }
                        }
                    }
                    else if (elemento.equals(" && "))
                    {
                        Boolean d1 = QUEUE_RESULTS.poll();
                        Boolean d2 = QUEUE_RESULTS.poll();
                        if (d1 && d2)
                        {
                            QUEUE_RESULTS_FINAL.add(true);
                        }
                    }
                    else
                    {
                        Boolean d1 = QUEUE_RESULTS.poll();
                        Boolean d2 = QUEUE_RESULTS.poll();
                        if (d1 || d2)
                        {
                            QUEUE_RESULTS_FINAL.add(true);
                        }
                        else
                        {
                            QUEUE_RESULTS_FINAL.add(false);
                        }
                    }
                }
                else if (elemento.equals("("))
                {
                    elemento = QUEUE_OPERATORS.poll();
                    if (elemento.equals(" && "))
                    {
                        Boolean d1 = QUEUE_RESULTS_FINAL.poll();
                        Boolean d2 = QUEUE_RESULTS.poll();
                        if (d1 && d2)
                        {
                            QUEUE_RESULTS_FINAL.add(true);
                        }
                    }
                    else
                    {
                        Boolean d1 = QUEUE_RESULTS_FINAL.poll();
                        Boolean d2 = QUEUE_RESULTS.poll();
                        if (d1 || d2)
                        {
                            QUEUE_RESULTS_FINAL.add(true);
                        }
                        else
                        {
                            QUEUE_RESULTS_FINAL.add(false);
                        }
                    }
                }
                else if (elemento.equals(" && "))
                {
                    Boolean d1 = QUEUE_RESULTS_FINAL.poll();
                    Boolean d2 = QUEUE_RESULTS.poll();
                    if (d1 && d2)
                    {
                        QUEUE_RESULTS_FINAL.add(true);
                    }
                    else
                    {
                        QUEUE_RESULTS_FINAL.add(false);
                    }
                }
                else
                {
                    if (QUEUE_RESULTS.isEmpty())
                    {
                        Boolean d1 = QUEUE_RESULTS_FINAL.poll();
                        Boolean d2 = QUEUE_RESULTS_FINAL.poll();
                        if (d1 || d2)
                        {
                            QUEUE_RESULTS_FINAL.add(true);
                        }
                        else
                        {
                            QUEUE_RESULTS_FINAL.add(false);
                        }
                    }
                    else
                    {
                        Boolean d1 = QUEUE_RESULTS_FINAL.poll();
                        Boolean d2 = QUEUE_RESULTS.poll();
                        if (d1 || d2)
                        {
                            QUEUE_RESULTS_FINAL.add(true);
                        }
                        else
                        {
                            QUEUE_RESULTS_FINAL.add(false);
                        }
                    }
                }
            }
        } return false;
    }
}
