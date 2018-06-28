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

    static
    {
        simpleDateFormat = new SimpleDateFormat("ddmmyyyy");
    }

    private enum Operators
    {
        IGUAL("=", " == "), NO_IGUAL("<>", " != "), MENOR("<", " < "), MAYOR(">", " > "), MAYOR_IGUAL(">=", " >= "), MENOR_IGUAL("<=", " <= "), COMO("LIKE", "==");

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
        scriptBuilder("false", "LIKE", "0", "", "EstadosUnidosMexicanos", "*Mexicanos", "1");
        System.out.println("Result TestFilter 1 (" + EvaluationProccess(Script) + ") -> " + Script + " Evaluado en " + (System.currentTimeMillis() - inicio) + " milisegundos");
    }

    private void scriptBuilder(String negate, String operator, String connector, String parenthesis, String dato, String value, String type)
    {
        if (dato != null)
        {
            String script = (connector.contains("1")) ? " || " : "";
            script += (connector.contains("0")) ? " && " : "";
            script += (negate.contains("true")) ? "!" : "";
            script += (parenthesis.contains("(")) ? "(" : "";
            script += (value.startsWith("*")) ? "(\"" + dato + "\").endsWith(\"" + value.replace("*", "") + "\")" : "";
            script += (value.endsWith("*")) ? "(\"" + dato + "\").startsWith(\"" + value.replace("*", "") + "\")" : "";
            script += (!value.contains("*")) ? "(\"" + dato + "\")" + Operators.forValue(operator).getOperadorCodigo() + "(\"" + value + "\")" : "";
            script += (parenthesis.contains(")")) ? ")" : "";
            script = (type.equals("10") || type.equals("11")) ? script.replace("\"", "") : script;
            Script += script;
        }
    }

    private Boolean EvaluationProccess(String script)
    {
        String Script = "" + "var echo; if (" + script + "){ echo=true; } else { echo=false; }";
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