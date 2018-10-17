package com.mate.bence.udalosti.Activity.Udalosti.Podrobnosti;

public class AktualizatorObsahu {

    private static AktualizatorObsahu aktualizatorObsahu;
    private Aktualizator aktualizator;

    private int pozcia;
    private int stav;
    private int idUdalost;
    private String karta;

    private AktualizatorObsahu() {}

    public static AktualizatorObsahu stav() {
        if(aktualizatorObsahu == null) {
            aktualizatorObsahu = new AktualizatorObsahu();
        }
        return aktualizatorObsahu;
    }

    public void nastav(Aktualizator aktualizator) {
        this.aktualizator = aktualizator;
    }

    public void hodnota(int pozicia, int stav, int idUdalost, String karta) {
        if(aktualizator != null) {
            this.pozcia = pozicia;
            this.stav = stav;
            this.idUdalost = idUdalost;
            this.karta = karta;

            aktualizuj();
        }
    }

    private void aktualizuj() {
        aktualizator.aktualizujObsahUdalosti();
    }

    public int getPozcia() {
        return pozcia;
    }

    public int getStav() {
        return stav;
    }

    public int getIdUdalost() {
        return idUdalost;
    }

    public String getKarta() {
        return karta;
    }
}