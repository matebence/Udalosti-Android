package com.mate.bence.udalosti.Nastroje;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Siet.UdalostiAdresa;

import java.io.InputStream;

public class Stream extends AsyncTask<String, Void, Bitmap> {

    private static final String TAG = Stream.class.getName();

    @SuppressLint("StaticFieldLeak")
    private ImageView obrazok;
    @SuppressLint("StaticFieldLeak")
    private ProgressBar nacitavanie;
    @SuppressLint("StaticFieldLeak")
    private Context context;

    public Stream(ImageView obrazok, ProgressBar nacitavanie, Context context) {
        Log.v(Stream.TAG, "Metoda Stream bola vykonana");

        this.obrazok = obrazok;
        this.context = context;
        this.nacitavanie = nacitavanie;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.nacitavanie.setVisibility(View.VISIBLE);
    }

    protected Bitmap doInBackground(String... adresa) {
        String adresaObrazka = UdalostiAdresa.getAdresa() + "udalosti" + "/" + adresa[0];
        Bitmap bitmap;
        try {
            InputStream zdroj = new java.net.URL(adresaObrazka).openStream();
            bitmap = BitmapFactory.decodeStream(zdroj);
        } catch (Exception e) {
            bitmap = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.obrazok_nenajdeni);
            Log.e("Chyba ", e.getMessage());
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        this.nacitavanie.setVisibility(View.GONE);
        this.obrazok.setImageBitmap(result);
    }
}