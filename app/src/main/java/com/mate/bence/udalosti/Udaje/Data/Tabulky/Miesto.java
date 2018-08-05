package com.mate.bence.udalosti.Udaje.Data.Tabulky;

public class Miesto {
    private String stat;
    private String okres;
    private String mesto;

    public Miesto(String stat, String okres, String mesto) {
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
        return "Miesto{" +
                "stat='" + stat + '\'' +
                ", okres='" + okres + '\'' +
                ", mesto='" + mesto + '\'' +
                '}';
    }
}
