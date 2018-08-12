package com.mate.bence.udalosti.Activity.Navigacia.Fragment.Udalosti.Karty;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.firebase.messaging.FirebaseMessaging;
import com.mate.bence.udalosti.Activity.Navigacia.Fragment.Udalosti.UdalostiUdaje;
import com.mate.bence.udalosti.Dialog.PodrobnostiUdalosti.PodrobnostiUdalosti;
import com.mate.bence.udalosti.List.Oznamenie.Oznamenie;
import com.mate.bence.udalosti.List.Oznamenie.OznamenieAdapter;
import com.mate.bence.udalosti.List.Oznamenie.TlacidloGesta;
import com.mate.bence.udalosti.List.PoskitovelObsahu;
import com.mate.bence.udalosti.List.Ziadost.TlacidlaGesta;
import com.mate.bence.udalosti.List.Ziadost.Ziadost;
import com.mate.bence.udalosti.List.Ziadost.ZiadostAdapter;
import com.mate.bence.udalosti.Nastroje.Notifikacie;
import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Nastavenia.Pocet;
import com.mate.bence.udalosti.Udaje.Nastavenia.Spravy;
import com.mate.bence.udalosti.Udaje.Nastavenia.Status;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaData;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Oznamenia extends Fragment implements KommunikaciaData, KommunikaciaOdpoved, TlacidlaGesta, TlacidloGesta {

    private String email, token;

    private BroadcastReceiver broadcastReceiver;

    private List<Ziadost> ziadost;
    private List<Oznamenie> oznamenie;

    private UdalostiUdaje udalostiUdaje;
    private ZiadostAdapter ziadostAdapter;
    private OznamenieAdapter oznamenieAdapter;

    private RecyclerView zoznamZiadosti;
    private RecyclerView zoznamOznameni;

    private PoskitovelObsahu poskitovelObsahu;
    private LinearLayout ziadneOznamenia;
    private ProgressBar nacitavanie;
    private View ciara;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_udalosti_oznamenia, container, false);
        init(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Spravy.USPESNA_REGISTRACIA)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(Spravy.TEMA);
                } else if (intent.getAction().equals(Spravy.POSLI_NOTIFIKACIU)) {
                    spracujSpravuZoServera(intent);
                }
            }
        };
    }

    @Override
    public void onPause() {
        super.onPause();
        Pocet.OZNAMENIA_ZOZNAM_UKONCIL = Pocet.OZNAMENIA_ZOZNAM_POCET;
        Pocet.ZIADOSTI_ZOZNAM_UKONCIL = Pocet.ZIADOSTI_ZOZNAM_POCET;

        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();

        Pocet.ZIADOSTI_ZOZNAM_OD = 0;
        if (Pocet.ZIADOSTI_ZOZNAM_UKONCIL != 0) {
            Pocet.ZIADOSTI_ZOZNAM_OD = Pocet.ZIADOSTI_ZOZNAM_UKONCIL;
            Pocet.ZIADOSTI_ZOZNAM_OD -= 5;
        }

        Pocet.OZNAMENIA_ZOZNAM_OD = 0;
        if (Pocet.OZNAMENIA_ZOZNAM_UKONCIL != 0) {
            Pocet.OZNAMENIA_ZOZNAM_OD = Pocet.OZNAMENIA_ZOZNAM_UKONCIL;
            Pocet.OZNAMENIA_ZOZNAM_OD -= 5;
        }

        if (ziadost.isEmpty() || oznamenie.isEmpty()) {
            this.nacitavanie.setVisibility(View.VISIBLE);
            udalostiUdaje.zoznamOznameniZiadosti(email, Pocet.OZNAMENIA_ZOZNAM_OD, Pocet.OZNAMENIA_ZOZNAM_POCET, token);
        }

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, new IntentFilter(Spravy.USPESNA_REGISTRACIA));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, new IntentFilter(Spravy.POSLI_NOTIFIKACIU));
        Notifikacie.odstranNotifikacie(getContext());
    }

    @Override
    public void dataZoServera(String odpoved, String od, ArrayList udaje) {
        switch (od) {
            case Status.UDALOSTI_OZNAMENIA:
                if (odpoved.equals(Status.VSETKO_V_PORIADKU)) {
                    if (udaje != null) {
                        ziadneOznamenia.setVisibility(View.GONE);
                        ziskajOznamenia(udaje);
                    } else {
                        if (!(Pocet.OZNAMENIA_ZOZNAM_OD > 0)) {
                            ziadneOznamenia.setVisibility(View.VISIBLE);
                        }
                    }
                    zoznamOznameni.setItemViewCacheSize(oznamenie.size());
                }
                break;

            case Status.UDALOSTI_ZIADOSTI:
                if (odpoved.equals(Status.VSETKO_V_PORIADKU)) {
                    if (udaje != null) {
                        ziadneOznamenia.setVisibility(View.GONE);
                        ziskajZiadosti(udaje);
                    } else {
                        ciara.setVisibility(View.GONE);
                    }
                    zoznamZiadosti.setItemViewCacheSize(ziadost.size());
                }
                break;
        }
        this.nacitavanie.setVisibility(View.GONE);
    }

    @Override
    public void odpovedServera(String odpoved, String od, HashMap<String, String> udaje) {

    }

    @Override
    public void nacitajDalsieZiadostiPriatelstvo() {
        Pocet.ZIADOSTI_ZOZNAM_OD += 5;
        udalostiUdaje.zoznamOznameniZiadosti(email, Pocet.ZIADOSTI_ZOZNAM_OD, Pocet.ZIADOSTI_ZOZNAM_POCET, token);
    }

    @Override
    public void nacitajDalsieOznamenia() {
        Pocet.OZNAMENIA_ZOZNAM_OD += 5;
        udalostiUdaje.zoznamOznameniZiadosti(email, Pocet.OZNAMENIA_ZOZNAM_OD, Pocet.OZNAMENIA_ZOZNAM_POCET, token);
    }

    @Override
    public void ukazUdalost(int idUdalost) {
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("token", token);
        bundle.putInt("idUdalost", idUdalost);

        PodrobnostiUdalosti podrobnostiUdalosti = new PodrobnostiUdalosti();
        podrobnostiUdalosti.setArguments(bundle);

        podrobnostiUdalosti.show(getFragmentManager(), "podrobnosti_udalosti");
    }

    @Override
    public void tlacidloPotrvdit(int pozicia) {
        int PRIJATI = 1;
        udalostiUdaje.odpovedNaZiadost(email, PRIJATI, ziadost.get(pozicia).getIdVztah(), token);
        if (ziadostAdapter.getItemCount() - 1 == 0) {
            ciara.setVisibility(View.GONE);
        }
        ziadostAdapter.odpovedNaZiadost(pozicia);
    }

    @Override
    public void tlacidloOdmietnut(int pozicia) {
        int ODMIETNUTI = 2;
        udalostiUdaje.odpovedNaZiadost(email, ODMIETNUTI, ziadost.get(pozicia).getIdVztah(), token);
        if (ziadostAdapter.getItemCount() - 1 == 0) {
            ciara.setVisibility(View.GONE);
        }
        ziadostAdapter.odpovedNaZiadost(pozicia);
    }

    protected void ziskajZiadosti(ArrayList udaje) {
        ziadost.addAll(udaje);
        ziadostAdapter.notifyItemRangeInserted(ziadost.size() - 1, udaje.size());
    }

    protected void ziskajOznamenia(ArrayList udaje) {
        oznamenie.addAll(udaje);
        oznamenieAdapter.notifyItemRangeInserted(oznamenie.size() - 1, udaje.size());
    }

    private void init(View view) {
        this.email = getArguments().getString("email");
        this.token = getArguments().getString("token");

        this.zoznamOznameni = view.findViewById(R.id.zoznam_oznameni);
        this.zoznamZiadosti = view.findViewById(R.id.zoznam_ziadosti);
        this.ziadneOznamenia = view.findViewById(R.id.ziadne_oznamenia);
        this.nacitavanie = view.findViewById(R.id.nacitavanie);
        this.ciara = view.findViewById(R.id.ciara);

        this.oznamenie = new ArrayList<>();
        this.ziadost = new ArrayList<>();

        nastavZoznamOznameni(oznamenie);
        nastavZoznamZiadosti(ziadost);

        this.udalostiUdaje = new UdalostiUdaje(this, this, getContext());
    }

    private void spracujSpravuZoServera(Intent intent) {
        ziadneOznamenia.setVisibility(View.GONE);
        if (TextUtils.isEmpty(intent.getStringExtra("idVztah"))) {
            nacitajNoveOznamenie(intent);
        } else if (TextUtils.isEmpty(intent.getStringExtra("obrazok"))) {
            potvrdenieZiadosti(intent);
        } else {
            nacitajNovuZiadost(intent);
        }
    }

    private void nacitajNoveOznamenie(Intent intent) {
        oznamenie.add(0, new Oznamenie(
                intent.getStringExtra("obrazok"),
                intent.getStringExtra("meno"),
                Integer.parseInt(intent.getStringExtra("idUdalost")),
                Integer.parseInt(intent.getStringExtra("precitana")),
                intent.getStringExtra("nazov"),
                intent.getStringExtra("mesto")));
        oznamenieAdapter.notifyItemInserted(0);
    }

    private void nacitajNovuZiadost(Intent intent) {
        ziadost.add(0, new Ziadost(
                intent.getStringExtra("obrazok"),
                intent.getStringExtra("meno"),
                Integer.parseInt(intent.getStringExtra("idVztah"))));
        ziadostAdapter.notifyItemInserted(0);
    }

    private void potvrdenieZiadosti(Intent intent) {
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), intent.getStringExtra("meno") + " potvrdil priatelstvo", Snackbar.LENGTH_SHORT);
        View pozadie = snackbar.getView();
        pozadie.setBackgroundColor(getResources().getColor(R.color.toolbar));
        snackbar.setActionTextColor(getResources().getColor(android.R.color.white));
        snackbar.show();
    }


    private void nastavZoznamOznameni(List<Oznamenie> udaje) {
        this.oznamenieAdapter = new OznamenieAdapter(udaje, this, getContext());
        this.poskitovelObsahu = new PoskitovelObsahu(getContext());
        zoznamOznameni.setLayoutManager(poskitovelObsahu);
        zoznamOznameni.setItemAnimator(new DefaultItemAnimator());
        zoznamOznameni.setAdapter(oznamenieAdapter);
    }

    private void nastavZoznamZiadosti(List<Ziadost> udaje) {
        this.ziadostAdapter = new ZiadostAdapter(udaje, this, getContext());
        this.poskitovelObsahu = new PoskitovelObsahu(getContext());
        zoznamZiadosti.setLayoutManager(poskitovelObsahu);
        zoznamZiadosti.setItemAnimator(new DefaultItemAnimator());
        zoznamZiadosti.setAdapter(ziadostAdapter);
    }
}