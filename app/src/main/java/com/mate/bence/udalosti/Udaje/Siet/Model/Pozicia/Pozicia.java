package com.mate.bence.udalosti.Udaje.Siet.Model.Pozicia;

import com.google.gson.annotations.SerializedName;

public class Pozicia {

    @SerializedName("city_district")
    private String pozicia;

    @SerializedName("city")
    private String okres;

    @SerializedName("state")
    private String kraj;

    @SerializedName("postcode")
    private String psc;

    @SerializedName("country")
    private String stat;

    @SerializedName("country_code")
    private String znakStatu;

    public Pozicia(String pozicia, String okres, String kraj, String psc, String stat, String znakStatu) {
        this.pozicia = pozicia;
        this.okres = okres;
        this.kraj = kraj;
        this.psc = psc;
        this.stat = stat;
        this.znakStatu = znakStatu;
    }

    public String getPozicia() {
        return pozicia;
    }

    public void setPozicia(String pozicia) {
        this.pozicia = pozicia;
    }

    public String getOkres() {
        return okres;
    }

    public void setOkres(String okres) {
        this.okres = okres;
    }

    public String getKraj() {
        return kraj;
    }

    public void setKraj(String kraj) {
        this.kraj = kraj;
    }

    public String getPsc() {
        return psc;
    }

    public void setPsc(String psc) {
        this.psc = psc;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getZnakStatu() {
        return znakStatu;
    }

    public void setZnakStatu(String znakStatu) {
        this.znakStatu = znakStatu;
    }

    @Override
    public String toString() {
        return "Pozicia{" +
                "pozicia='" + pozicia + '\'' +
                ", okres='" + okres + '\'' +
                ", kraj='" + kraj + '\'' +
                ", psc='" + psc + '\'' +
                ", stat='" + stat + '\'' +
                ", znakStatu='" + znakStatu + '\'' +
                '}';
    }
}