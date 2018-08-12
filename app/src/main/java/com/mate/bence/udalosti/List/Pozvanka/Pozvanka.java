package com.mate.bence.udalosti.List.Pozvanka;

import com.mate.bence.udalosti.List.Obsah;

public class Pozvanka extends Obsah {

    private int idPouzivatel;

    public Pozvanka(String obrazok, String meno, int idPouzivatel) {
        super(obrazok, meno);
        this.idPouzivatel = idPouzivatel;
    }

    public int getIdPouzivatel() {
        return idPouzivatel;
    }

    public void setIdPouzivatel(int idPouzivatel) {
        this.idPouzivatel = idPouzivatel;
    }

    @Override
    public String toString() {
        return "Pozvanka{" +
                "idPouzivatel=" + idPouzivatel +
                '}';
    }
}