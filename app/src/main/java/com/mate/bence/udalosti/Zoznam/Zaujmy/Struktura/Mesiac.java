package com.mate.bence.udalosti.Zoznam.Zaujmy.Struktura;

public class Mesiac extends Zoznam {

    private String mesiac;

    public String getMesiac() {
        return mesiac;
    }

    public void setMesiac(String mesiac) {
        this.mesiac = mesiac;
    }

    @Override
    public int cast() {
        return MESIAC;
    }
}