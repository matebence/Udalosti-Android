package com.mate.bence.udalosti.Activity.Udalosti.Podrobnosti;

public class AktualizatorObsahu {

    private static AktualizatorObsahu aktualizatorObsahu;
    private Aktualizator aktualizator;

    private int pozcia, stavTlacidla, zaujemcovia;

    private AktualizatorObsahu() {}

    public static AktualizatorObsahu udalosti() {
        if(aktualizatorObsahu == null) {
            aktualizatorObsahu = new AktualizatorObsahu();
        }
        return aktualizatorObsahu;
    }

    public void nastav(Aktualizator aktualizator) {
        this.aktualizator = aktualizator;
    }

    private void aktualizuj() {
        aktualizator.aktualizujObsahUdalosti();
    }

    public void hodnota(int pozicia, int stavTlacidla, int zaujemcovia) {
        if(aktualizator != null) {
            this.pozcia = pozicia;
            this.stavTlacidla = stavTlacidla;
            this.zaujemcovia = zaujemcovia;

            aktualizuj();
        }
    }

    public int getPozcia() {
        return pozcia;
    }

    public int getStavTlacidla() {
        return stavTlacidla;
    }

    public int getZaujemcovia() {
        return zaujemcovia;
    }
}