package com.mate.bence.udalosti.Activity.Navigacia.Fragment.Nastavenia;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mate.bence.udalosti.Activity.Autentifikacia.Autentifikacia;
import com.mate.bence.udalosti.Activity.Navigacia.Navigacia;
import com.mate.bence.udalosti.Dialog.Oznamenie.DialogOznameni;
import com.mate.bence.udalosti.Dialog.Potvrdenie.DialogOdpoved;
import com.mate.bence.udalosti.Dialog.Potvrdenie.DialogPotvrdeni;
import com.mate.bence.udalosti.Nastroje.Obrazok;
import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Nastavenia.Status;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class Nastavenia extends Fragment implements View.OnClickListener, KommunikaciaOdpoved {

    private static final String TAG = Nastavenia.class.getSimpleName();

    private String email, meno, token, obrazok;
    private NastaveniaUdaje nastaveniaUdaje;

    private Uri adresaUpravenejFotky;
    private LinearLayout nacitavanie;
    private View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_aplikacia_nastavenia, container, false);
        init(view);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.ikona_pouzit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_pouzit:
                aktualizujUdaje();
                return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nastavenia_profilova_fotka:
                Obrazok.spustiOrezavanie(getContext(), this);
                break;
            case R.id.nastavenia_odstranit_ucet:
                odstranenieUctu();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult vysledok = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                ImageView ukazkaFotky = view.findViewById(R.id.nastavenia_profilova_fotka);

                try {
                    Bitmap orezanyObrazok = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), vysledok.getUri());
                    RoundedBitmapDrawable okruhliObrazok = RoundedBitmapDrawableFactory.create(getResources(), orezanyObrazok);
                    okruhliObrazok.setCircular(true);
                    ukazkaFotky.setImageDrawable(okruhliObrazok);
                } catch (Exception e) {
                    Log.v(TAG, "Chyba pri zaokrúhlení fotky!");
                }

                this.adresaUpravenejFotky = zasifrujNazovObrazka(vysledok);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getContext(), getContext().getString(R.string.chyba_orezavania) + vysledok.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void odpovedServera(String odpoved, String od, HashMap<String, String> udaje) {
        switch (od) {
            case Status.NASTAVENIA:
                if (odpoved.equals(Status.VSETKO_V_PORIADKU)) {
                    if (!(udaje.get("meno").equals(""))) {
                        meno = udaje.get("meno");
                    }
                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.nastavenia_aktualizacia_prebehla_uspesne), Snackbar.LENGTH_SHORT);
                    View pozadie = snackbar.getView();
                    pozadie.setBackgroundColor(getContext().getResources().getColor(R.color.toolbar));
                    snackbar.setActionTextColor(getContext().getResources().getColor(android.R.color.white));
                    snackbar.show();

                    if (adresaUpravenejFotky != null) {
                        String obrazok = adresaUpravenejFotky.toString();
                        this.obrazok = obrazok.substring(obrazok.lastIndexOf("/") + 1);
                    }

                    ((Navigacia) getActivity()).nastavPouzivatelskeUdaje(this.meno, this.obrazok);
                } else {
                    new DialogOznameni(getActivity(), "Chyba", odpoved);
                }
                break;
            case Status.ODSTRANENIE_UCTU:
                if (odpoved.equals(Status.NASTALA_CHYBA)) {
                    new DialogOznameni(getActivity(), "Chyba", odpoved);
                } else {
                    Intent oPustitAplikaciu = new Intent(getContext(), Autentifikacia.class);
                    oPustitAplikaciu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(oPustitAplikaciu);
                    getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    getActivity().finish();
                }
        }
        this.nacitavanie.setVisibility(View.GONE);
    }

    private void init(View view) {
        this.view = view;

        this.email = getArguments().getString("email");
        this.meno = getArguments().getString("meno");
        this.token = getArguments().getString("token");
        this.obrazok = getArguments().getString("obrazok");

        this.nacitavanie = view.findViewById(R.id.nacitavanie);
        this.nastaveniaUdaje = new NastaveniaUdaje(this, getContext());

        ImageView obrazokPuzivatela = view.findViewById(R.id.nastavenia_profilova_fotka);
        obrazokPuzivatela.setOnClickListener(this);

        Button odstranUcet = view.findViewById(R.id.nastavenia_odstranit_ucet);
        odstranUcet.setOnClickListener(this);

        nastavObrazokPouzivatela(obrazokPuzivatela);
    }

    private Uri zasifrujNazovObrazka(CropImage.ActivityResult vysledok) {
        final String typObrazkaa = ".jpg";
        String obrazok = vysledok.getUri().toString();

        File orezanyObrazok = new File(getContext().getCacheDir(), obrazok.substring(obrazok.lastIndexOf("/") + 1));
        File zasifrenyObrazok = new File(getContext().getCacheDir(), UUID.randomUUID().toString() + typObrazkaa);

        if (orezanyObrazok.exists()) {
            orezanyObrazok.renameTo(zasifrenyObrazok);
            return Uri.fromFile(zasifrenyObrazok);
        } else {
            return null;
        }
    }

    private void odstranenieUctu() {
        this.nacitavanie.setVisibility(View.VISIBLE);

        new DialogPotvrdeni(getActivity(), getString(R.string.nastavenia_potvrdenie), nastaveniaUdaje.hesloPrePotvrdenie(), new DialogOdpoved() {
            @Override
            public void potvrdenie(boolean odpoved, String potvrdenie) {
                if (odpoved) {
                    nastaveniaUdaje.odstranPouzivatelskeKonto(email, token);
                } else {
                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.nastavenia_nespravne_heslo), Snackbar.LENGTH_SHORT);
                    View pozadie = snackbar.getView();
                    pozadie.setBackgroundColor(getContext().getResources().getColor(R.color.toolbar));
                    snackbar.setActionTextColor(getContext().getResources().getColor(android.R.color.white));
                    snackbar.show();
                }
            }
        });
    }

    private void aktualizujUdaje() {
        this.nacitavanie.setVisibility(View.VISIBLE);

        final EditText meno = view.findViewById(R.id.nastavenia_meno);
        final EditText heslo = view.findViewById(R.id.nastavenia_heslo);

        if ((heslo.getText().toString().equals("")) && (meno.getText().toString().equals("")) && (adresaUpravenejFotky == null)) {
            Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.nastavenia_ziadne_nove_udaje), Snackbar.LENGTH_SHORT);
            View pozadie = snackbar.getView();
            pozadie.setBackgroundColor(getContext().getResources().getColor(R.color.toolbar));
            snackbar.setActionTextColor(getContext().getResources().getColor(android.R.color.white));
            snackbar.show();
        } else {
            if (heslo.getText().toString().length() > 0) {
                new DialogPotvrdeni(getActivity(), getString(R.string.nastavenia_nove_heslo), heslo.getText().toString(), new DialogOdpoved() {
                    @Override
                    public void potvrdenie(boolean odpoved, String potvrdenie) {
                        if (odpoved) {
                            nastaveniaUdaje.aktualizujPouzivatelskeKonto(email, meno.getText().toString(), heslo.getText().toString(), potvrdenie, adresaUpravenejFotky, token, UUID.randomUUID().toString());
                        } else {
                            Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.nastavenia_hesla_sa_nezhoduju), Snackbar.LENGTH_SHORT);
                            View pozadie = snackbar.getView();
                            pozadie.setBackgroundColor(getContext().getResources().getColor(R.color.toolbar));
                            snackbar.setActionTextColor(getContext().getResources().getColor(android.R.color.white));
                            snackbar.show();
                        }
                    }
                });
            } else {
                nastaveniaUdaje.aktualizujPouzivatelskeKonto(email, meno.getText().toString(), heslo.getText().toString(), null, adresaUpravenejFotky, token, UUID.randomUUID().toString());
            }
        }

    }

    private void nastavObrazokPouzivatela(ImageView obrazokPuzivatela) {
        try {
            Bitmap orezanyObrazok = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.fromFile(new File(getContext().getCacheDir(), nastaveniaUdaje.fotkaPouzivatela())));
            RoundedBitmapDrawable okruhliObrazok = RoundedBitmapDrawableFactory.create(getResources(), orezanyObrazok);
            okruhliObrazok.setCircular(true);
            obrazokPuzivatela.setImageDrawable(okruhliObrazok);
        } catch (Exception e) {
            Log.v(TAG, "Chyba pri zaokrúhlení fotky " + e.toString());
        }
    }
}