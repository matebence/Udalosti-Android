package com.mate.bence.udalosti.List.Ludia.Priatelia.Struktura;

import com.mate.bence.udalosti.List.Ludia.Ludia;

public class Pouzivatel extends Zoznam {

    private Ludia udaje;

    public Ludia getUdaje() {
        return udaje;
    }

    public void setUdaje(Ludia udaje) {
        this.udaje = udaje;
    }

    @Override
    public int cast() {
        return POUZIVATEL;
    }
}