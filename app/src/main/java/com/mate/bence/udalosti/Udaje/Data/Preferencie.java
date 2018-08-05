package com.mate.bence.udalosti.Udaje.Data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.mate.bence.udalosti.Udaje.Nastavenia.Nastavenia;

public class Preferencie {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public Preferencie(Context context) {
        sharedPreferences = context.getSharedPreferences(Nastavenia.NAZOV, Nastavenia.MOD);
        editor = sharedPreferences.edit();
    }

    public void nastavPrvyStart(boolean prvyStart) {
        editor.putBoolean(Nastavenia.UKAZKA_APLIKACIE, prvyStart);
        editor.commit();
    }

    public boolean jePrvyStart() {
        return sharedPreferences.getBoolean(Nastavenia.UKAZKA_APLIKACIE, true);
    }
}