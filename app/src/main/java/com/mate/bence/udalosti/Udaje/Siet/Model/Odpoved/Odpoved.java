package com.mate.bence.udalosti.Udaje.Siet.Model.Odpoved;

import com.google.gson.annotations.SerializedName;

public class Odpoved {

    @SerializedName("chyba")
    private String chyba;

    @SerializedName("uspech")
    private String uspech;

    public Odpoved(String chyba, String uspech) {
        this.chyba = chyba;
        this.uspech = uspech;
    }

    public String getChyba() {
        return chyba;
    }

    public void setChyba(String chyba) {
        this.chyba = chyba;
    }

    public String getUspech() {
        return uspech;
    }

    public void setUspech(String uspech) {
        this.uspech = uspech;
    }

    @Override
    public String toString() {
        return "Odpoved{" +
                "chyba='" + chyba + '\'' +
                ", uspech='" + uspech + '\'' +
                '}';
    }
}