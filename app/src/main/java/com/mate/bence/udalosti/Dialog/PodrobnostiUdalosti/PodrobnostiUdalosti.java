package com.mate.bence.udalosti.Dialog.PodrobnostiUdalosti;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mate.bence.udalosti.Activity.Navigacia.Fragment.Udalosti.UdalostiUdaje;
import com.mate.bence.udalosti.List.Udalosti.TlacidlaGesta;
import com.mate.bence.udalosti.List.Udalosti.Udalost;
import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Nastavenia.Status;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaData;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;
import com.mate.bence.udalosti.Udaje.Siet.UdalostiAdresa;

import java.io.InputStream;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;

public class PodrobnostiUdalosti extends DialogFragment implements View.OnClickListener, TlacidlaGesta, KommunikaciaData, KommunikaciaOdpoved {

    private ImageView obrazokUdalosti;

    private TextView den;
    private TextView mesiac;
    private TextView nazov;
    private TextView mesto;
    private TextView miesto;
    private TextView cas;

    private ProgressBar nacitavanie;
    private Button mamZaujem;

    private String email, token;
    private int idUdalost;

    private UdalostiUdaje udalostiUdaje;
    private DialogPodrobnostiUdalostiUdaje dialogPodrobnostiUdalostiUdaje;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_podrobnosti_udalosti, container, false);
        init(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        dialogPodrobnostiUdalostiUdaje.podrobnostiUdalosti(email, idUdalost, token);
    }

    @Override
    public void dataZoServera(String odpoved, String od, ArrayList udaje) {
        switch (od) {
            case Status.UDALOSTI_OBJAVUJ:
                if (odpoved.equals(Status.VSETKO_V_PORIADKU)) {
                    if (udaje != null) {
                        ziskajInformacieUdalosti(udaje);
                    } else {
                        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getContext().getString(R.string.chyba_udalost_je_nedostupna), Snackbar.LENGTH_SHORT);
                        View pozadie = snackbar.getView();
                        pozadie.setBackgroundColor(getContext().getResources().getColor(R.color.toolbar));
                        snackbar.setActionTextColor(getContext().getResources().getColor(android.R.color.white));
                        snackbar.show();
                        getDialog().dismiss();
                    }
                }
                break;
        }
        this.nacitavanie.setVisibility(View.GONE);
    }

    @Override
    public void odpovedServera(String odpoved, String od, HashMap<String, String> udaje) {
        switch (od) {
            case Status.UDALOSTI_KALENDAR:
                if (odpoved.equals(Status.VSETKO_V_PORIADKU)) {
                    break;
                }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.budem_tam:
                udalostiUdaje.zaujemOudalost(email, idUdalost, token);

                if (mamZaujem.getText().toString().equals(getString(R.string.list_zoznam_udalosti_tlacidlo_mam_zaujem))) {
                    mamZaujem.setText(getString(R.string.list_zoznam_udalosti_tlacidlo_budem_tam));
                    mamZaujem.setBackground(getResources().getDrawable(R.drawable.ic_tlacidlo_udalosti_aktivna));
                } else {
                    mamZaujem.setText(getString(R.string.list_zoznam_udalosti_tlacidlo_mam_zaujem));
                    mamZaujem.setBackground(getResources().getDrawable(R.drawable.ic_tlacidlo_udalosti_inaktivna));
                }
                getDialog().dismiss();
                break;
        }
    }

    private void ziskajInformacieUdalosti(ArrayList udaje) {
        Udalost udalost = (Udalost) udaje.get(0);

        new ziskajObrazok(obrazokUdalosti, getContext()).execute(udalost.getObrazok());

        nazov.setText(udalost.getNazov());
        den.setText(nastavDenUdalosti(udalost.getDatum()));
        mesiac.setText(nastavMesiacUdalosti(udalost.getDatum()));
        cas.setText(ziskajHodinyMinuty(udalost.getCas()));

        if (udalost.getIdZaujem() != null) {
            mamZaujem.setBackground(getResources().getDrawable(R.drawable.ic_tlacidlo_udalosti_aktivna));
            mamZaujem.setText(getString(R.string.list_zoznam_udalosti_tlacidlo_budem_tam));
        }

        nastavMiestoUdalosti(mesto, miesto, udalost.getMesto());
    }

    private void nastavMiestoUdalosti(TextView mestoUdalosti, TextView miestoUdalosti, String mesto) {
        String miesto[] = mesto.split(" ");
        if (miesto.length > 1) {
            mestoUdalosti.setText(miesto[0]);
            miestoUdalosti.setText(miesto[1]);
        } else {
            mestoUdalosti.setText(mesto);
        }
    }

    private String nastavDenUdalosti(String den) {
        return den.substring(den.lastIndexOf("-") + 1, den.length()) + ".";
    }

    private String nastavMesiacUdalosti(String mesiac) {
        return new DateFormatSymbols().getMonths()[Integer.parseInt(mesiac.substring(mesiac.indexOf("-") + 1, mesiac.lastIndexOf("-"))) - 1];
    }

    private String ziskajHodinyMinuty(String cas) {
        return cas.substring(0, cas.lastIndexOf(":"));
    }

    private void init(View view) {
        this.dialogPodrobnostiUdalostiUdaje = new DialogPodrobnostiUdalostiUdaje(getContext(), this);
        this.udalostiUdaje = new UdalostiUdaje(this, this, getContext());

        this.email = getArguments().getString("email");
        this.token = getArguments().getString("token");
        this.idUdalost = getArguments().getInt("idUdalost");

        this.obrazokUdalosti = view.findViewById(R.id.udalosti_obrazok);

        this.den = view.findViewById(R.id.udalosti_den);
        this.mesiac = view.findViewById(R.id.udalosti_mesiac);
        this.nazov = view.findViewById(R.id.udalosti_nazov);
        this.mesto = view.findViewById(R.id.udalosti_mesto);
        this.miesto = view.findViewById(R.id.udalosti_miesto);
        this.cas = view.findViewById(R.id.udalosti_cas);

        this.mamZaujem = view.findViewById(R.id.budem_tam);
        this.mamZaujem.setOnClickListener(this);

        this.nacitavanie = view.findViewById(R.id.nacitavanie);
        this.nacitavanie.setVisibility(View.VISIBLE);

        Button pozvat = view.findViewById(R.id.pozvat);
        pozvat.setVisibility(View.GONE);

        View tien = view.findViewById(R.id.zoznam_udalosti_tien);
        tien.setVisibility(View.GONE);

        RelativeLayout riadok = view.findViewById(R.id.udalosti_riadok);
        LinearLayout.LayoutParams margin = (LinearLayout.LayoutParams) riadok.getLayoutParams();
        margin.setMargins(0, 0, 0, 0);
        riadok.setLayoutParams(margin);
    }

    @Override
    public void nacitajDalsieUdalosti() {
    }

    @Override
    public void pozvat(int idUdalost) {
    }

    @Override
    public void budemTam(int idUdalost) {
    }

    @SuppressLint("StaticFieldLeak")
    private class ziskajObrazok extends AsyncTask<String, Void, Bitmap> {
        private ImageView obrazok;
        private Context context;

        ziskajObrazok(ImageView obrazok, Context context) {
            this.obrazok = obrazok;
            this.context = context;
        }

        protected Bitmap doInBackground(String... adresa) {
            String adresaObrazka = UdalostiAdresa.getAdresa() + "udalosti" + "/" + adresa[0];
            Bitmap bitmap;
            try {
                InputStream zdroj = new java.net.URL(adresaObrazka).openStream();
                bitmap = BitmapFactory.decodeStream(zdroj);
            } catch (Exception e) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.udalosti_chyba_obrazka);
                Log.e("Chyba ", e.getMessage());
                e.printStackTrace();

            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            obrazok.setImageBitmap(result);
        }
    }
}