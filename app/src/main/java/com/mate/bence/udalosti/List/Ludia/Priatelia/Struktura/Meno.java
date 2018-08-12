package com.mate.bence.udalosti.List.Ludia.Priatelia.Struktura;

public class Meno extends Zoznam {

    private String meno;

    public String getMeno() {
        return meno;
    }

    public void setMeno(String meno) {
        this.meno = meno;
    }

    @Override
    public int cast() {
        return MENO;
    }
}