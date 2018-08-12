package com.mate.bence.udalosti.Udaje.Data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.mate.bence.udalosti.Udaje.Nastavenia.Premenne;

public class Preferencie {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public Preferencie(Context context) {
        sharedPreferences = context.getSharedPreferences(Premenne.NAZOV, Premenne.MOD);
        editor = sharedPreferences.edit();
    }

    public void ulozRegistracneCislo(String cislo) {
        editor.putString(Premenne.REGISTRACNE_CISLO, cislo);
        editor.commit();
    }

    public String vratRegistracneCislo() {
        return sharedPreferences.getString(Premenne.REGISTRACNE_CISLO, null);
    }

    public void nastavPrvyStart(boolean prvyStart) {
        editor.putBoolean(Premenne.UKAZKA_APLIKACIE, prvyStart);
        editor.commit();
    }

    public boolean jePrvyStart() {
        return sharedPreferences.getBoolean(Premenne.UKAZKA_APLIKACIE, true);
    }
}