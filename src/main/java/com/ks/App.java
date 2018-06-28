package com.ks;

import com.ks.FilterEvaluation.FilterModel;
import com.ks.FilterEvaluation.TransactionField;
import com.ks.TestFilter.TestFilter;
import com.ks.TestFilter.TestFilter2;
import com.ks.TestFilter.TestFilter3;
import java.util.Set;
public class App
{
    public static void main(String[] args)
    {
        Set<FilterModel> filtros = null;
        TransactionField transactionField = null;
        TestFilter testFilter = new TestFilter();
        testFilter.datos();
        TestFilter2 testFilter2 = new TestFilter2();
        testFilter2.datos();
        TestFilter3 testFilter3 = new TestFilter3();
        System.out.println(testFilter3.evaluationProcess("true||(false&&true||!(true&&(false||!true&&false)&&!true)||!true)&&true"));
        System.out.println(testFilter3.evaluationProcess(filtros,transactionField));
    }
}
