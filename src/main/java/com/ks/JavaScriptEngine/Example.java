package com.ks.JavaScriptEngine;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Example
{
    public static void main(String[] args)
    {
        final ScriptEngineManager m = new ScriptEngineManager();
        final ScriptEngine e = m.getEngineByName("js");
        String script = "" + "var echo; " + "if ('1'>'2'){ " + "echo=true; " + "}" + "else" + "{" + "echo=false" + "}";
        try
        {
            e.eval(script);
            System.out.println(String.valueOf(e.get("echo")));
        }
        catch (Exception exp)
        {
            exp.printStackTrace();
        }
    }
}

