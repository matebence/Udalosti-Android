package com.mate.bence.udalosti.Activity.Udalosti.Karty;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mate.bence.udalosti.Activity.Autentifikacia.AutentifikaciaUdaje;
import com.mate.bence.udalosti.Activity.Udalosti.Podrobnosti.Podrobnosti;
import com.mate.bence.udalosti.Activity.Udalosti.UdalostiPanel;
import com.mate.bence.udalosti.Activity.Udalosti.UdalostiUdaje;
import com.mate.bence.udalosti.Dialog.DialogOznameni;
import com.mate.bence.udalosti.Nastroje.Casovac;
import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Nastavenia.Nastavenia;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaData;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;
import com.mate.bence.udalosti.Udaje.Siet.Model.DlzkaRequestu;
import com.mate.bence.udalosti.Zoznam.PoskitovelObsahu;
import com.mate.bence.udalosti.Zoznam.Udalost;
import com.mate.bence.udalosti.Zoznam.Udalosti.UdalostAdapter;
import com.mate.bence.udalosti.Zoznam.Udalosti.ZvolenaUdalost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Lokalizator extends Fragment implements KommunikaciaData, KommunikaciaOdpoved, ZvolenaUdalost, LocationListener, DlzkaRequestu {

    private static final String TAG = Lokalizator.class.getName();
    private final int REQUEST_CODE_GPS = 101;

    private String email, heslo, stat, okres, pozicia, token;
    private List<Udalost> obsahUdalostiPodlaPozicie;

    private UdalostiUdaje udalostiUdaje;
    private AutentifikaciaUdaje autentifikaciaUdaje;

    private UdalostAdapter udalostAdapter;
    private LocationManager managerPozicie;

    private UdalostiPanel udalostiPanel;
    private Casovac casovac;

    private SwipeRefreshLayout aktualizujUdalosti;
    private RecyclerView zoznamUdalostiPodlaPozcie;
    private LinearLayout chybaUdalostiPodlaPozicie;
    private ProgressBar nacitavanie;
    private ImageView chybaUdalostiObrazok;
    private TextView chybaUdalostiText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_udalosti_lokalizator_udalosti, container, false);
        return init(view);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.udalostiPanel = (UdalostiPanel) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(Lokalizator.TAG + " Interface UdalostiPanel nie je implementovana");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (this.obsahUdalostiPodlaPozicie.isEmpty()) {
            this.nacitavanie.setVisibility(View.VISIBLE);
            this.udalostiUdaje.zoznamUdalostiPodlaPozicie(this.email, this.stat, this.okres, this.pozicia, this.token);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        this.autentifikaciaUdaje.miestoPrihlasenia(this.email, this.heslo, location.getLatitude(), location.getLongitude(), true, true);
        this.casovac.cancel();
        this.managerPozicie.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_GPS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    zistiPoziciu();
                } else {
                    this.nacitavanie.setVisibility(View.GONE);
                    new DialogOznameni(getActivity(), "Chyba", getString(R.string.udalosti_gps_pristup));
                }
            }
        }
    }

    @Override
    public void dataZoServera(String odpoved, String od, ArrayList udaje) {
        Log.v(Lokalizator.TAG, "Metoda dataZoServera - Lokalizator bola vykonana");

        switch (od) {
            case Nastavenia.UDALOSTI_PODLA_POZICIE:
                if (odpoved.equals(Nastavenia.VSETKO_V_PORIADKU)) {
                    this.chybaUdalostiPodlaPozicie.setVisibility(View.GONE);

                    this.chybaUdalostiObrazok.setBackgroundResource(R.drawable.ic_udalosti);
                    this.chybaUdalostiText.setText(getResources().getString(R.string.udalosti_ziadne_udalosti));

                    if (udaje != null) {
                        this.chybaUdalostiPodlaPozicie.setVisibility(View.GONE);
                        ziskajUdalosti(udaje);
                    } else {
                        this.chybaUdalostiPodlaPozicie.setVisibility(View.VISIBLE);
                    }
                    this.zoznamUdalostiPodlaPozcie.setItemViewCacheSize(obsahUdalostiPodlaPozicie.size());
                } else {
                    this.chybaUdalostiPodlaPozicie.setVisibility(View.VISIBLE);

                    this.chybaUdalostiObrazok.setBackgroundResource(R.drawable.ic_spojenie);
                    this.chybaUdalostiText.setText(getResources().getString(R.string.chyba_spojenie_zlyhalo));
                }
                break;
        }
        this.nacitavanie.setVisibility(View.GONE);
    }

    @Override
    public void odpovedServera(String odpoved, String od, HashMap<String, String> udaje) {
        Log.v(Lokalizator.TAG, "Metoda odpovedServera - Lokalizator bola vykonana");

        switch (od) {
            case Nastavenia.UDALOSTI_AKTUALIZUJ:
                if (odpoved.equals(Nastavenia.VSETKO_V_PORIADKU)) {
                    HashMap<String, String> miestoPrihlasenia = udalostiUdaje.miestoPrihlasenia();

                    this.stat = miestoPrihlasenia.get("stat");
                    this.okres = miestoPrihlasenia.get("okres");
                    this.pozicia = miestoPrihlasenia.get("pozicia");

                    this.udalostiPanel.aktualizujPanel(this.pozicia);
                    this.udalostiUdaje.zoznamUdalostiPodlaPozicie(this.email, this.stat, this.okres, this.pozicia, this.token);
                } else {
                    this.chybaUdalostiPodlaPozicie.setVisibility(View.VISIBLE);

                    this.chybaUdalostiObrazok.setBackgroundResource(R.drawable.ic_spojenie);
                    this.chybaUdalostiText.setText(getResources().getString(R.string.chyba_spojenie_zlyhalo));
                }
                break;
        }
    }

    @Override
    public void podrobnostiUdalosti(View view, int pozicia) {
        Log.v(Lokalizator.TAG, "Metoda podrobnostiUdalosti - Lokalizator bola vykonana");

        Udalost udalost = this.obsahUdalostiPodlaPozicie.get(pozicia);
        Intent zvolenaUdalost = new Intent(getActivity(), Podrobnosti.class);

        zvolenaUdalost.putExtra("email", this.email);
        zvolenaUdalost.putExtra("token", this.token);

        zvolenaUdalost.putExtra("idUdalost", udalost.getIdUdalost());
        zvolenaUdalost.putExtra("zaujemUdalosti", udalost.getZaujem());
        zvolenaUdalost.putExtra("pozicia", pozicia);

        zvolenaUdalost.putExtra("obrazok", udalost.getObrazok());
        zvolenaUdalost.putExtra("nazov", udalost.getNazov());
        zvolenaUdalost.putExtra("den", udalost.getDen());
        zvolenaUdalost.putExtra("mesiac", udalost.getMesiac());
        zvolenaUdalost.putExtra("cas", udalost.getCas());
        zvolenaUdalost.putExtra("mesto", udalost.getMesto());
        zvolenaUdalost.putExtra("ulica", udalost.getUlica());
        zvolenaUdalost.putExtra("vstupenka", udalost.getVstupenka());
        zvolenaUdalost.putExtra("zaujemcovia", udalost.getZaujemcovia());

        startActivity(zvolenaUdalost);
        getActivity().overridePendingTransition(R.anim.vstupit_vychod_activity, R.anim.vstupit_vchod_activity);
    }

    @Override
    public void podrobnostiUdalosti(int pozicia, Udalost udalost) {
    }

    @Override
    public void zistiPoziciuPodlaSiete() {
        Log.v(Lokalizator.TAG, "Metoda zistiPoziciuPodlaSiete - Lokalizator bola vykonana");

        this.udalostiUdaje.zoznamUdalostiPodlaPozicie(this.email, this.stat, this.okres, this.pozicia, this.token);
    }

    private View init(View view) {
        Log.v(Lokalizator.TAG, "Metoda init - Lokalizator bola vykonana");

        this.email = getArguments().getString("email");
        this.heslo = getArguments().getString("heslo");
        this.token = getArguments().getString("token");

        this.stat = getArguments().getString("stat");
        this.okres = getArguments().getString("okres");
        this.pozicia = getArguments().getString("pozicia");

        this.managerPozicie = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        this.casovac = new Casovac(Nastavenia.DLZKA_REQUESTU, this.managerPozicie, this, this);

        this.zoznamUdalostiPodlaPozcie = view.findViewById(R.id.zoznam_udalosti);
        this.chybaUdalostiPodlaPozicie = view.findViewById(R.id.chyba_udalosti);
        this.nacitavanie = view.findViewById(R.id.nacitavanie);
        this.aktualizujUdalosti = view.findViewById(R.id.aktualizuj);
        this.chybaUdalostiObrazok = view.findViewById(R.id.chyba_udalosti_obrazok);
        this.chybaUdalostiText = view.findViewById(R.id.chyba_udalosti_text);

        this.aktualizujUdalosti.setOnRefreshListener(aktualizuj);
        this.aktualizujUdalosti.setColorSchemeColors(getResources().getColor(R.color.nacitavanie));

        this.obsahUdalostiPodlaPozicie = new ArrayList<>();
        nastavZoznamUdalosti(this.obsahUdalostiPodlaPozicie);

        this.autentifikaciaUdaje = new AutentifikaciaUdaje(this, getContext());
        this.udalostiUdaje = new UdalostiUdaje(this, this, getContext());
        return view;
    }

    private void ziskajUdalosti(ArrayList<Udalost> udalosti) {
        Log.v(Lokalizator.TAG, "Metoda ziskajUdalosti bola vykonana");

        this.obsahUdalostiPodlaPozicie.addAll(udalosti);
        this.udalostAdapter.notifyItemRangeInserted(0, udalosti.size());
        this.zoznamUdalostiPodlaPozcie.setVisibility(View.VISIBLE);
    }

    private void nastavZoznamUdalosti(List<Udalost> udaje) {
        Log.v(Lokalizator.TAG, "Metoda nastavZoznamUdalosti bola vykonana");

        PoskitovelObsahu poskitovelObsahu = new PoskitovelObsahu(getContext());

        this.udalostAdapter = new UdalostAdapter(udaje, getContext());
        this.udalostAdapter.zvolenaUdalost(this);

        this.zoznamUdalostiPodlaPozcie.setLayoutManager(poskitovelObsahu);
        this.zoznamUdalostiPodlaPozcie.setItemAnimator(new DefaultItemAnimator());
        this.zoznamUdalostiPodlaPozcie.setAdapter(this.udalostAdapter);
    }

    public void zistiPoziciu() {
        Log.v(Lokalizator.TAG, "Metoda zistiPoziciu bola vykonana");

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_GPS);
        } else {
            this.managerPozicie.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            this.casovac.start();
        }
    }

    private SwipeRefreshLayout.OnRefreshListener aktualizuj = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            obsahUdalostiPodlaPozicie.clear();
            udalostAdapter.notifyItemRangeRemoved(0, obsahUdalostiPodlaPozicie.size());

            chybaUdalostiPodlaPozicie.setVisibility(View.GONE);
            zoznamUdalostiPodlaPozcie.setVisibility(View.GONE);
            nacitavanie.setVisibility(View.VISIBLE);

            if (pozicia != null) {
                udalostiUdaje.zoznamUdalostiPodlaPozicie(email, stat, okres, pozicia, token);
            } else {
                zistiPoziciu();
            }
            aktualizujUdalosti.setRefreshing(false);
        }
    };
}