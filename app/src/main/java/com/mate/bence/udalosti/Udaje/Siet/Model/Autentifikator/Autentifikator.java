package com.mate.bence.udalosti.Udaje.Siet.Model.Autentifikator;

import com.google.gson.annotations.SerializedName;

public class Autentifikator {
    @SerializedName("chyba")
    private Boolean chyba;

    @SerializedName("pouzivatel")
    private Pouzivatel pouzivatel;

    @SerializedName("validacia")
    private Validacia validacia;

    public Autentifikator(Boolean chyba, Pouzivatel pouzivatel, Validacia validacia) {
        this.chyba = chyba;
        this.pouzivatel = pouzivatel;
        this.validacia = validacia;
    }

    public Boolean getChyba() {
        return chyba;
    }

    public Pouzivatel getPouzivatel() {
        return pouzivatel;
    }

    public Validacia getValidacia() {
        return validacia;
    }

    @Override
    public String toString() {
        return "Autentifikator{" +
                "chyba=" + chyba +
                ", pouzivatel=" + pouzivatel +
                ", validacia=" + validacia +
                '}';
    }
}