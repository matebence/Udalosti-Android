package com.mate.bence.udalosti.Activity.Udalosti.Podrobnosti;

public class AktualizatorObsahu {

    private static AktualizatorObsahu aktualizatorObsahu;
    private Aktualizator aktualizator;

    private AktualizatorObsahu() {}

    public static AktualizatorObsahu zaujmy() {
        if(aktualizatorObsahu == null) {
            aktualizatorObsahu = new AktualizatorObsahu();
        }
        return aktualizatorObsahu;
    }

    public void nastav(Aktualizator aktualizator) {
        this.aktualizator = aktualizator;
    }

    private void aktualizuj() {
        aktualizator.aktualizujObsahZaujmov();
    }

    public void hodnota() {
        if(aktualizator != null) {
            aktualizuj();
        }
    }
}