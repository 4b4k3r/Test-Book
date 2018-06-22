package com.ks.TestFilter;

public class TestFilter2
{    private String Script = "";
    private long inicio = System.currentTimeMillis();


    private enum Operators
    {
        IGUAL("=", " == "), NO_IGUAL("<>", " != "), MENOR("<", " < "), MAYOR(">", " > "), MAYOR_IGUAL(">=", " >= "), MENOR_IGUAL("<=", " <= "), COMO("LIKE", ".equals");

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
        String script = (EvalData("true", "<", "", "(", "27/06/2017", "25/07/06"));
        script += (EvalData("false", "LIKE", "0", ")", "Z264*", "Z264"));
        script += (EvalData("false", "<=", "1", "(", "35", "96"));
        script += (EvalData("false", "<=", "1", ")", "35", "96"));
        System.out.println("Result TestFilter 2 (" + EvaluationProccess(script) + ") -> " + script + " Evaluado en " + (System.currentTimeMillis() - inicio) + " milisegundos");
        EvaluationProccess(script);
    }

    private String EvalData(String negate, String operator, String connector, String parenthesis, String value, String dato)
    {
        if (dato != null)
        {
            Script = (negate.equals("true")) ? " ! " : "";
            Script += (connector.equals("0")) ? " || " : "";
            Script += (connector.equals("1")) ? " && " : "";
            Script += (parenthesis.equals("(")) ? "(" : "";
            Script += (value.contains("*")) ? "(\"" + dato + "\")" + Operators.forValue(operator).getOperadorCodigo() + "(\"" + value.replace('*', '%') + "\") " : "(\"" + dato + "\")" + Operators.forValue(operator).getOperadorCodigo() + "(\"" + value + "\") ";
            Script += (parenthesis.equals(")")) ? ")" : "";
            return Script;
        }
        return null;
    }

    private Boolean EvaluationProccess(String script)
    {
        return false;
    }

}
