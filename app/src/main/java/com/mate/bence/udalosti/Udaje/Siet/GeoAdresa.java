package com.mate.bence.udalosti.Udaje.Siet;

public class GeoAdresa {
    private GeoAdresa() {
    }

    private static final String ADRESA = "https://eu1.locationiq.com/v1/";

    public static Requesty initAdresu() {
        return HTTPRequest.posliRequest(ADRESA).create(Requesty.class);
    }

    public static void initNanovo() {
        HTTPRequest.setRequest(null);
    }
}