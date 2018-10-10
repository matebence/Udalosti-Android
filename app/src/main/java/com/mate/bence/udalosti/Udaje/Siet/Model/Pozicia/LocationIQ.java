package com.mate.bence.udalosti.Udaje.Siet.Model.Pozicia;

import com.google.gson.annotations.SerializedName;

public class LocationIQ {

    @SerializedName("address")
    private Pozicia pozicia;

    public LocationIQ(Pozicia pozicia) {
        this.pozicia = pozicia;
    }

    public Pozicia getPozicia() {
        return pozicia;
    }

    @Override
    public String toString() {
        return "LocationIQ{" +
                "pozicia=" + pozicia +
                '}';
    }
}