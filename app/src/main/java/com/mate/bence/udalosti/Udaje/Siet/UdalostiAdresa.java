package com.mate.bence.udalosti.Udaje.Siet;

public class UdalostiAdresa {
    private UdalostiAdresa() {
    }

    private static final String ADRESA = "http://10.0.2.2/";

    public static Requesty initAdresu() {
        return HTTPRequest.posliRequest(ADRESA).create(Requesty.class);
    }

    public static void initNanovo() {
        HTTPRequest.setRequest(null);
    }

    public static String getAdresa() {
        return ADRESA;
    }
}