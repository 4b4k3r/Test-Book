package com.ks.TestFilter;

import com.ks.FilterEvaluation.FilterModel;
import com.ks.FilterEvaluation.TransactionField;
import jboolexpr.BooleanExpression;
import jboolexpr.MalformedBooleanException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class TestFilter3
{
    private Queue<String> QUEUE_OPERATORS = new LinkedList();
    private Queue<String> QUEUE_VALUES = new LinkedList();
    private Queue<Boolean> QUEUE_RESULTS = new LinkedList();
    private Object[] depuratedList;
    private String Script = "";
    private long inicio = System.currentTimeMillis();

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

    public Boolean evaluationProcess(String script)
    {
        boolean bool = false;
        try
        {
            BooleanExpression boolExpr = BooleanExpression.readLeftToRight(script);
            bool = boolExpr.booleanValue();
        }
        catch (MalformedBooleanException e)
        {
            e.printStackTrace();
        }
        System.out.println("Result TestFilter 3 (" + bool + ") -> " + Script + " Evaluado en " + (System.currentTimeMillis() - inicio) + " milisegundos");
        return bool;
    }

    public Boolean evaluationProcess(Set<FilterModel> filters,  TransactionField transactionField )
    {
        if (filters==null||transactionField==null)
        {
            return false;
        }
        for (FilterModel filterModel : filters)
        {
            int idField = Integer.parseInt(filterModel.getIdField());
            String negated = String.valueOf(filterModel.getIsNegated());
            String operator = filterModel.getOperator();
            String connector = filterModel.getConnector();
            String parenthesis = filterModel.getParenthesis();
            String dato = transactionField.getField(idField);
            String value = filterModel.getValue();
            String type = filterModel.getType();
            scriptBuilder(negated,operator,connector,parenthesis,dato,value,type);
        }
        boolean bool = false;
        try
        {
            BooleanExpression boolExpr = BooleanExpression.readLeftToRight(Script);
            bool = boolExpr.booleanValue();
        }
        catch (MalformedBooleanException e)
        {
            e.printStackTrace();
        }
        System.out.println("Result TestFilter 3 (" + bool + ") -> " + Script + " Evaluado en " + (System.currentTimeMillis() - inicio) + " milisegundos");
        return bool;
    }

    private void scriptBuilder(String negate, String operator, String connector, String parenthesis, String dato, String value, String type)
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
                    if (value.endsWith("*"))
                    {
                        value = value.replace("*", "");
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
                        value = value.replace("*", "");
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
                if (result > 0 && (operador.equals(" > ") || operador.equals(" >= ") || operador.equals(" != ")))
                {
                    QUEUE_RESULTS.add(true);
                }
                else if (result == 0 && operador.equals(" = "))
                {
                    QUEUE_RESULTS.add(true);
                }
                else if (result < 0 && (operador.equals(" < ") || operador.equals(" <= ") || operador.equals(" != ")))
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
