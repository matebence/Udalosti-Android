package com.mate.bence.udalosti.Udaje.Siet;

public class UdalostiAdresa {
    private UdalostiAdresa() {
    }

    private static final String ADRESA = " http://192.168.55.148/";

    public static Requesty initAdresu() {
        return HTTPRequest.posliRequest(ADRESA).create(Requesty.class);
    }

    public static String getAdresa() {
        return ADRESA;
    }
}