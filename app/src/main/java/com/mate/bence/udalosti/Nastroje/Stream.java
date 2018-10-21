package com.mate.bence.udalosti.Nastroje;

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

    private ImageView obrazok;
    private ProgressBar nacitavanie;
    private Context context;

    public Stream(ImageView obrazok, ProgressBar nacitavanie, Context context) {
        this.obrazok = obrazok;
        this.context = context;
        this.nacitavanie = nacitavanie;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        nacitavanie.setVisibility(View.VISIBLE);
    }

    protected Bitmap doInBackground(String... adresa) {
        String adresaObrazka = UdalostiAdresa.getAdresa() + "udalosti" + "/" + adresa[0];
        Bitmap bitmap;
        try {
            InputStream zdroj = new java.net.URL(adresaObrazka).openStream();
            bitmap = BitmapFactory.decodeStream(zdroj);
        } catch (Exception e) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.obrazok_nenajdeni);
            Log.e("Chyba ", e.getMessage());
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        nacitavanie.setVisibility(View.GONE);
        obrazok.setImageBitmap(result);
    }
}