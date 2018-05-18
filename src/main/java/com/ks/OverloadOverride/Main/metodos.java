package com.ks.OverloadOverride.Main;

public class metodos {
    public double multiplicar(int numero1, double numero2){
        double res=numero1*numero2;
        return res;
    }
    public Double multiplicar(double numero1, double numero2){
        double res=numero1*numero2;
        return res;
    }
    public int multiplicar(int numero1, int numero2){
        int res=numero1*numero2;
        return res;
    }
}
