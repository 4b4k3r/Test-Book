package com.ks;

import com.ks.TestFilter.TestFilter;
import com.ks.TestFilter.TestFilter2;

public class App
{
    public static void main(String[] args)
    {
        if (("25/07/2016".compareTo("27/06/2017")>0  && ("Z264*").contains("Z264")  && ("B0030606").contains("B003") ) || ((96) != (35)  && (16) <= (5) ) || ((16) < (5)  && (16) > (5) )){
        System.out.println(true);
        }else{
            System.out.println(false);
        }
        TestFilter testFilter = new TestFilter();
        testFilter.datos();
        TestFilter2 testFilter2 = new TestFilter2();
        testFilter2.datos();
    }
}
