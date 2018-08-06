package com.mate.bence.udalosti.Udaje.Siet.Model.Obsah;

import com.google.gson.annotations.SerializedName;
import com.mate.bence.udalosti.Zoznam.Udalost;

import java.util.ArrayList;

public class Obsah {

    @SerializedName("udalosti")
    private ArrayList<Udalost> udalosti;

    public Obsah(ArrayList<Udalost> udalosti) {
        this.udalosti = udalosti;
    }

    public ArrayList<Udalost> getUdalosti() {
        return udalosti;
    }

    public void setUdalosti(ArrayList<Udalost> udalosti) {
        this.udalosti = udalosti;
    }

    @Override
    public String toString() {
        return "Obsah{" +
                "udalosti=" + udalosti +
                '}';
    }
}
