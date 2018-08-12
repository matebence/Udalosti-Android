package com.mate.bence.udalosti.List.Ziadost;

import com.mate.bence.udalosti.List.Obsah;

public class Ziadost extends Obsah {

    private int idVztah;

    public Ziadost(String obrazok, String meno, int idVztah) {
        super(obrazok, meno);
        this.idVztah = idVztah;
    }

    public int getIdVztah() {
        return idVztah;
    }

    public void setIdVztah(int idVztah) {
        this.idVztah = idVztah;
    }

    @Override
    public String toString() {
        return "Ziadost{" +
                "idVztah=" + idVztah +
                '}';
    }
}