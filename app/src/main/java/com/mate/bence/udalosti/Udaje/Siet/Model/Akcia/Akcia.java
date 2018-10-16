package com.mate.bence.udalosti.Udaje.Siet.Model.Akcia;

import com.google.gson.annotations.SerializedName;

public class Akcia {

    @SerializedName("uspech")
    private String uspech;

    @SerializedName("chyba")
    private String chyba;

    public Akcia(String uspech, String chyba) {
        this.uspech = uspech;
        this.chyba = chyba;
    }

    public String getUspech() {
        return uspech;
    }

    public String getChyba() {
        return chyba;
    }

    @Override
    public String toString() {
        return "Akcia{" +
                "uspech='" + uspech + '\'' +
                ", chyba='" + chyba + '\'' +
                '}';
    }
}