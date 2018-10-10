package com.mate.bence.udalosti.Udaje.Data.Tabulky;

public class Miesto {

    private String pozicia;
    private String okres;
    private String kraj;
    private String psc;
    private String stat;
    private String znakStatu;

    public Miesto(String pozicia, String okres, String kraj, String psc, String stat, String znakStatu) {
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
        return "Miesto{" +
                "pozicia='" + pozicia + '\'' +
                ", okres='" + okres + '\'' +
                ", kraj='" + kraj + '\'' +
                ", psc='" + psc + '\'' +
                ", stat='" + stat + '\'' +
                ", znakStatu='" + znakStatu + '\'' +
                '}';
    }
}