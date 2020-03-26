package com.jm.javaScriptEngine;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Example {
    private static final ScriptEngineManager SCRIPT_ENGINE_MANAGER = new ScriptEngineManager();
    private static final ScriptEngine SCRIPT_ENGINE = SCRIPT_ENGINE_MANAGER.getEngineByName("Nashorn");

    public static void main(String[] args) {
        System.out.println("testing| '1'>'2' [=>] " + testScript("'1'>'2'"));
        System.out.println("testing| 25 * 2 >= 50 [=>] " + testScript("25 * 2 >= 50"));
        System.out.println("testing| '1' == 1 [=>] " + testScript("'1' == 1"));
        System.out.println("testing| '1' === '1' [=>] " + testScript("'1' === '1'"));
        System.out.println("testing| Math.pow(2,8) [=>] " + testScript("Math.pow(2,8)"));
    }

    private static String testScript(String script) {
        try {
            return String.valueOf(SCRIPT_ENGINE.eval(script));
        } catch (Exception exp) {
            exp.printStackTrace();
            return "error " + exp.getMessage();
        }
    }
}

