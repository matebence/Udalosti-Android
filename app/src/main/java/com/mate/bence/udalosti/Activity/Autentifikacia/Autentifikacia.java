package com.mate.bence.udalosti.Activity.Autentifikacia;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mate.bence.udalosti.Activity.Autentifikacia.Fragment.Prihlasenie;
import com.mate.bence.udalosti.Activity.Autentifikacia.Fragment.Registracia;
import com.mate.bence.udalosti.Activity.Autentifikacia.Fragment.ZabudnuteHeslo;
import com.mate.bence.udalosti.Activity.Navigacia.Navigacia;
import com.mate.bence.udalosti.Dialog.Oznamenie.DialogOznameni;
import com.mate.bence.udalosti.Nastroje.Pripojenie;
import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Nastavenia.Status;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class Autentifikacia extends AppCompatActivity implements AutentifikaciaOvladanie, KommunikaciaOdpoved {

    private static final String TAG = Prihlasenie.class.getName();
    private FragmentManager fragmentManager;
    private AutentifikaciaUdaje autentifikaciaUdaje;
    private Uri adresaUpravenejFotky;
    private LinearLayout nacitavanie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autentifikacia);

        this.fragmentManager = getFragmentManager();
        this.autentifikaciaUdaje = new AutentifikaciaUdaje(this, getApplicationContext());
        this.nacitavanie = findViewById(R.id.nacitavanie);

        pridajFragment(new Prihlasenie(), Status.AUTENTIFIKACIA_PRIHLASENIE);
        nastavPozadie();
        automatickePrihlasenieChyba();
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult vysledok = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                ImageView ukazkaFotky = findViewById(R.id.registracia_profil);
                try {
                    Bitmap orezanyObrazok = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), vysledok.getUri());
                    RoundedBitmapDrawable okruhliObrazok = RoundedBitmapDrawableFactory.create(getResources(), orezanyObrazok);
                    okruhliObrazok.setCircular(true);
                    ukazkaFotky.setImageDrawable(okruhliObrazok);
                } catch (Exception e) {
                    Log.v(TAG, "Chyba pri zaokrúhlení fotky!");
                }
                this.adresaUpravenejFotky = vysledok.getUri();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, getApplicationContext().getString(R.string.chyba_orezavania) + vysledok.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void odpovedServera(String odpoved, String od, HashMap<String, String> udaje) {
        switch (od) {
            case Status.AUTENTIFIKACIA_REGISTRACIA:
                if (odpoved.equals(Status.VSETKO_V_PORIADKU)) {
                    fragmentManager.popBackStackImmediate();

                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.autentifikacia_uspesna_registracia), Snackbar.LENGTH_SHORT);
                    View pozadie = snackbar.getView();
                    pozadie.setBackgroundColor(getResources().getColor(R.color.toolbar));
                    snackbar.setActionTextColor(getResources().getColor(android.R.color.white));
                    snackbar.show();
                } else {

                    new DialogOznameni(this, "Chyba", odpoved);
                }
                break;

            case Status.AUTENTIFIKACIA_ZABUDNUTE_HESLO:
                if (odpoved.equals(Status.VSETKO_V_PORIADKU)) {
                    autentifikaciaUdaje.ucetJeNePristupny(udaje.get("email"));
                } else {
                    new DialogOznameni(this, "Chyba", odpoved);
                }
                break;

            case Status.AUTENTIFIKACIA_PRIHLASENIE:
                if (odpoved.equals(Status.VSETKO_V_PORIADKU)) {
                    autentifikaciaUdaje.ulozPrihlasovacieUdajeDoDatabazy(
                            udaje.get("email"),
                            udaje.get("heslo"),
                            udaje.get("meno"),
                            udaje.get("obrazok"));

                    Intent uspesnePrihlasenie = new Intent(Autentifikacia.this, Navigacia.class);
                    uspesnePrihlasenie.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    uspesnePrihlasenie.putExtra("email", udaje.get("email"));
                    uspesnePrihlasenie.putExtra("heslo", udaje.get("heslo"));
                    uspesnePrihlasenie.putExtra("token", udaje.get("token"));
                    uspesnePrihlasenie.putExtra("meno", udaje.get("meno"));
                    uspesnePrihlasenie.putExtra("obrazok", udaje.get("obrazok"));
                    startActivity(uspesnePrihlasenie);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                } else {
                    new DialogOznameni(this, "Chyba", odpoved);
                }
                break;
        }
        this.nacitavanie.setVisibility(View.GONE);
    }

    @Override
    public void tlacidloPrihlasitSa(String email, String heslo) {
        if (Pripojenie.pripojenieExistuje(this)) {
            this.nacitavanie.setVisibility(View.VISIBLE);
            autentifikaciaUdaje.miestoPrihlasenia(email, heslo);
        } else {
            new DialogOznameni(this, "Chyba", getString(R.string.chyba_ziadne_spojenie));
        }
    }

    @Override
    public void tlacidloRegistrovatSa(String meno, String email, String heslo, String potvrd, String pohlavie) {
        if (Pripojenie.pripojenieExistuje(this)) {
            String kluc = autentifikaciaUdaje.registracneCisloZariadeniu();
            if (kluc != null) {
                this.nacitavanie.setVisibility(View.VISIBLE);
                autentifikaciaUdaje.registracia(adresaUpravenejFotky, meno, email, heslo, potvrd, pohlavie, kluc);
            } else {
                new DialogOznameni(this, "Chyba", getString(R.string.chyba_gcm_registracia_nedostupna));
            }
        } else {
            new DialogOznameni(this, "Chyba", getString(R.string.chyba_ziadne_spojenie));
        }
    }

    @Override
    public void tlacidloZabudnuteHeslo(String email) {
        if (Pripojenie.pripojenieExistuje(this)) {
            this.nacitavanie.setVisibility(View.VISIBLE);
            autentifikaciaUdaje.zabudnuteHeslo(email);
        } else {
            new DialogOznameni(this, "Chyba", getString(R.string.chyba_ziadne_spojenie));
        }
    }

    @Override
    public void registracia() {
        pridajFragment(new Registracia(), Status.AUTENTIFIKACIA_REGISTRACIA);
    }

    @Override
    public void zabudnuteHeslo() {
        pridajFragment(new ZabudnuteHeslo(), Status.AUTENTIFIKACIA_ZABUDNUTE_HESLO);
    }

    private void nastavPozadie() {
        int pozadie[] = {R.drawable.pozadie_tema_1, R.drawable.pozadie_tema_2, R.drawable.pozadie_tema_3,
                R.drawable.pozadie_tema_4, R.drawable.pozadie_tema_5};
        RelativeLayout aktualnePozadie = findViewById(R.id.autentifikacia_pozadie);
        aktualnePozadie.setBackground(getResources().getDrawable(pozadie[(int) (Math.random() * pozadie.length)]));
    }

    private void pridajFragment(Fragment fragment, String nazov) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        animujObsah(nazov, fragmentTransaction);
        fragmentTransaction.replace(R.id.prihlasenie_registracia_zabudnute_heslo, fragment);
        if (!(nazov.equals(Status.AUTENTIFIKACIA_PRIHLASENIE))) {
            fragmentTransaction.addToBackStack(nazov);
        }
        fragmentTransaction.commit();
    }

    private void animujObsah(String fragment, FragmentTransaction fragmentTransaction) {
        switch (fragment) {
            case Status.AUTENTIFIKACIA_REGISTRACIA:
                fragmentTransaction.setCustomAnimations(R.animator.z_prava_do_lava, R.animator.vysunut_z_lava_do_prava, R.animator.vysunut_z_prava_do_lava, R.animator.z_lava_do_prava);
                break;
            case Status.AUTENTIFIKACIA_ZABUDNUTE_HESLO:
                fragmentTransaction.setCustomAnimations(R.animator.vysunut_z_prava_do_lava, R.animator.z_lava_do_prava, R.animator.z_prava_do_lava, R.animator.vysunut_z_lava_do_prava);
                break;
            default:
                fragmentTransaction.setCustomAnimations(R.animator.zviditelnit, 0, 0, 0);
                break;
        }
    }

    private void automatickePrihlasenieChyba() {
        Bundle udaje = getIntent().getExtras();
        if (udaje != null) {
            if (udaje.getBoolean("neUspesnePrihlasenie")) {
                new DialogOznameni(this, "Chyba", getString(R.string.chyba_automatickeho_prihlasenia));
                if (udaje.getString("email") != null) {
                    this.autentifikaciaUdaje.ucetJeNePristupny(udaje.getString("email"));
                }
            } else if (udaje.getString("chyba") != null) {
                new DialogOznameni(this, "Chyba", udaje.getString("chyba"));
            }
        }
    }
}