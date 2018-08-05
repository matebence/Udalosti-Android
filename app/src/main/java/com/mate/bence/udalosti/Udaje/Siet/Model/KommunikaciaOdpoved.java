package com.mate.bence.udalosti.Udaje.Siet.Model;

import java.util.HashMap;

public interface KommunikaciaOdpoved {
    void odpovedServera(String odpoved, String od, HashMap<String, String> udaje);
}