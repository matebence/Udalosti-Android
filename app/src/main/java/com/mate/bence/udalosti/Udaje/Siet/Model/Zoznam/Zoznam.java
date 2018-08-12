package com.mate.bence.udalosti.Udaje.Siet.Model.Zoznam;

import com.google.gson.annotations.SerializedName;
import com.mate.bence.udalosti.List.Kalendar.Struktura.Informacie;
import com.mate.bence.udalosti.List.Ludia.Ludia;
import com.mate.bence.udalosti.List.Oznamenie.Oznamenie;
import com.mate.bence.udalosti.List.Udalosti.Udalost;
import com.mate.bence.udalosti.List.Ziadost.Ziadost;

import java.util.ArrayList;

public class Zoznam {

    @SerializedName("udalosti")
    private ArrayList<Udalost> udalosti;

    @SerializedName("pouzivatelia")
    private ArrayList<Ludia> ludia;

    @SerializedName("priatelia")
    private ArrayList<Ludia> priatelia;

    @SerializedName("buduce_udalosti")
    private ArrayList<Informacie> naplanovaneUdalosti;

    @SerializedName("oznamenia")
    private ArrayList<Oznamenie> oznamenia;

    @SerializedName("ziadosti")
    private ArrayList<Ziadost> ziadosti;

    public Zoznam(ArrayList<Udalost> udalosti, ArrayList<Ludia> ludia, ArrayList<Ludia> priatelia, ArrayList<Informacie> naplanovaneUdalosti, ArrayList<Oznamenie> oznamenia, ArrayList<Ziadost> ziadosti) {
        this.udalosti = udalosti;
        this.ludia = ludia;
        this.priatelia = priatelia;
        this.naplanovaneUdalosti = naplanovaneUdalosti;
        this.oznamenia = oznamenia;
        this.ziadosti = ziadosti;
    }

    public ArrayList<Udalost> getUdalosti() {
        return udalosti;
    }

    public void setUdalosti(ArrayList<Udalost> udalosti) {
        this.udalosti = udalosti;
    }

    public ArrayList<Ludia> getLudia() {
        return ludia;
    }

    public void setLudia(ArrayList<Ludia> ludia) {
        this.ludia = ludia;
    }

    public ArrayList<Ludia> getPriatelia() {
        return priatelia;
    }

    public void setPriatelia(ArrayList<Ludia> priatelia) {
        this.priatelia = priatelia;
    }

    public ArrayList<Informacie> getNaplanovaneUdalosti() {
        return naplanovaneUdalosti;
    }

    public void setNaplanovaneUdalosti(ArrayList<Informacie> naplanovaneUdalosti) {
        this.naplanovaneUdalosti = naplanovaneUdalosti;
    }

    public ArrayList<Oznamenie> getOznamenia() {
        return oznamenia;
    }

    public void setOznamenia(ArrayList<Oznamenie> oznamenia) {
        this.oznamenia = oznamenia;
    }

    public ArrayList<Ziadost> getZiadosti() {
        return ziadosti;
    }

    public void setZiadosti(ArrayList<Ziadost> ziadosti) {
        this.ziadosti = ziadosti;
    }
}