package com.mate.bence.udalosti.Udaje.Siet.Model.Pozicia;

import com.google.gson.annotations.SerializedName;

public class Pozicia {

    @SerializedName("country")
    private String stat;

    @SerializedName("regionName")
    private String okres;

    @SerializedName("city")
    private String mesto;

    public Pozicia(String stat, String okres, String mesto) {
        this.stat = stat;
        this.okres = okres;
        this.mesto = mesto;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getOkres() {
        return okres;
    }

    public void setOkres(String okres) {
        this.okres = okres;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    @Override
    public String toString() {
        return "Pozicia{" +
                "stat='" + stat + '\'' +
                ", okres='" + okres + '\'' +
                ", mesto='" + mesto + '\'' +
                '}';
    }
}
