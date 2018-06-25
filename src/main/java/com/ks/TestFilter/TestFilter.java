package com.ks.TestFilter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.text.SimpleDateFormat;

public class TestFilter
{
    private final ScriptEngineManager m = new ScriptEngineManager();
    private final ScriptEngine e = m.getEngineByName("js");
    private String Script = "";
    private static SimpleDateFormat simpleDateFormat;
    private long inicio = System.currentTimeMillis();

    static {
        simpleDateFormat = new SimpleDateFormat("ddmmyyyy");
    }

    private enum Operators
    {
        IGUAL("=", " == "), NO_IGUAL("<>", " != "), MENOR("<", " < "), MAYOR(">", " > "), MAYOR_IGUAL(">=", " >= "), MENOR_IGUAL("<=", " <= "), COMO("LIKE", ".contains");

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
                default:
                    return null;
            }
        }
    }

    public void datos()
    {
        //String script = (EvalData("false", ">", "", "", "27/06/2017", "25/07/06","30"));
        String script = (EvalData("false", ">", "", "(", "27/06/2017", "25/07/2016", "30"));
        script += (EvalData("false", "LIKE", "0", "", "Z264", "Z264*", "1"));
        script += (EvalData("false", "LIKE", "0", ")", "B003*", "B0030606", "1"));
        script += (EvalData("false", "<>", "1", "(", "35", "96", "10"));
        script += (EvalData("false", "<=", "0", ")", "5", "16", "10"));
        script += (EvalData("false", "<", "1", "(", "5", "16", "10"));
        script += (EvalData("false", ">", "0", ")", "5", "16", "10"));
        //script += (EvalData("false", "<=", "1", ")", "35", "96","10"));
        System.out.println("Result TestFilter 1 (" + EvaluationProccess(script) + ") -> " + script + " Evaluado en " + (System.currentTimeMillis() - inicio) + " milisegundos");
    }

    private String EvalData(String negate, String operator, String connector, String parenthesis, String value, String dato,String type)
    {
        if (dato != null)
        {
            Script = (negate.equals("true")) ? " ! " : "";
            Script += (connector.equals("1")) ? " || " : "";
            Script += (connector.equals("0")) ? " && " : "";
            Script += (parenthesis.equals("(")) ? "(" : "";
            Script += (value.contains("*")) ? "(\"" + dato + "\")" + Operators.forValue(operator).getOperadorCodigo() + "(\"" + value.replace('*','%') : "(\"" + dato + "\")" + Operators.forValue(operator).getOperadorCodigo() + "(\"" + value + "\") ";
            Script += (parenthesis.equals(")")) ? ")" : "";
            Script = (type.equals("10")||type.equals("11")) ? Script.replace("\""," ") :  Script;
            return Script;
        }
        return null;
    }

    private Boolean EvaluationProccess(String script)
    {
        String Script = "" + "var echo; if (" + script + "){ echo=true; } else { echo=false; }";
        //System.out.println(Script);
        try
        {
            e.eval(Script);
            return Boolean.parseBoolean(String.valueOf(e.get("echo")));
        }
        catch (ScriptException e1)
        {
            System.out.println("Error en la evaluacion de la sentencia enviada por la razón: " + e1.getMessage());
        }
        return false;
    }

}