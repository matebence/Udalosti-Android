package com.mate.bence.udalosti.Udaje.Data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.mate.bence.udalosti.Udaje.Nastavenia.Nastavenia;

public class Preferencie {

    private static final String TAG = Preferencie.class.getName();

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public Preferencie(Context context) {
        Log.v(Preferencie.TAG, "Metoda Preferencie bola vykonana");

        this.sharedPreferences = context.getSharedPreferences(Nastavenia.NAZOV, Nastavenia.MOD);
        this.editor = this.sharedPreferences.edit();
    }

    public void nastavPrvyStart(boolean prvyStart) {
        Log.v(Preferencie.TAG, "Metoda nastavPrvyStart bola vykonana");

        this.editor.putBoolean(Nastavenia.UKAZKA_APLIKACIE, prvyStart);
        this.editor.commit();
    }

    public boolean jePrvyStart() {
        Log.v(Preferencie.TAG, "Metoda jePrvyStart bola vykonana");

        return this.sharedPreferences.getBoolean(Nastavenia.UKAZKA_APLIKACIE, true);
    }
}