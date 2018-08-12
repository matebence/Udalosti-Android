package com.mate.bence.udalosti.Activity.Navigacia.Fragment.Udalosti.Karty;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.mate.bence.udalosti.Activity.Navigacia.Fragment.Udalosti.UdalostiUdaje;
import com.mate.bence.udalosti.List.Gesta;
import com.mate.bence.udalosti.List.Kalendar.KalendarAdapter;
import com.mate.bence.udalosti.List.Kalendar.Struktura.Informacie;
import com.mate.bence.udalosti.List.Kalendar.Struktura.Mesiac;
import com.mate.bence.udalosti.List.Kalendar.Struktura.NaplanovanaUdalost;
import com.mate.bence.udalosti.List.Kalendar.Struktura.Zoznam;
import com.mate.bence.udalosti.List.Kalendar.TlacidlaGesta;
import com.mate.bence.udalosti.List.PoskitovelObsahu;
import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Nastavenia.Pocet;
import com.mate.bence.udalosti.Udaje.Nastavenia.Status;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaData;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class Kalendar extends Fragment implements KommunikaciaData, KommunikaciaOdpoved, TlacidlaGesta, Gesta.RecyclerItemTouchHelperListener {

    private String email, token;

    private List<Informacie> naplnovaneUdalosti;
    private List<Zoznam> skupina;

    private UdalostiUdaje udalostiUdaje;
    private KalendarAdapter kalendarAdapter;

    private RecyclerView zoznamNaplnovanychUdalosti;
    private LinearLayout ziadneNaplanovaneUdalosti;
    private ProgressBar nacitavanie;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_udalosti_kalendar, container, false);
        init(view);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Pocet.KALENDAR_ZOZNAM_UKONCIL = Pocet.KALENDAR_ZOZNAM_POCET;
    }

    @Override
    public void onResume() {
        super.onResume();

        Pocet.KALENDAR_ZOZNAM_OD = 0;
        if (Pocet.KALENDAR_ZOZNAM_UKONCIL != 0) {
            Pocet.KALENDAR_ZOZNAM_OD = Pocet.KALENDAR_ZOZNAM_UKONCIL;
            Pocet.KALENDAR_ZOZNAM_OD -= 5;
        }

        if (naplnovaneUdalosti.isEmpty()) {
            this.nacitavanie.setVisibility(View.VISIBLE);
            udalostiUdaje.zoznamNaplanovanychUdalosti(email, Pocet.KALENDAR_ZOZNAM_OD, Pocet.KALENDAR_ZOZNAM_POCET, token);
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof KalendarAdapter.NaplanovanaUdalostHolder) {

            NaplanovanaUdalost naplanovanaUdalost = (NaplanovanaUdalost) skupina.get(position);

            this.udalostiUdaje.odstranenieUdalostiKalendara(email, naplanovanaUdalost.getInformacie().getIdZaujem(), token);
            this.kalendarAdapter.odstranNaplanovanuUdalost(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void dataZoServera(String odpoved, String od, ArrayList udaje) {
        switch (od) {
            case Status.UDALOSTI_KALENDAR:
                if (odpoved.equals(Status.VSETKO_V_PORIADKU)) {
                    if (udaje != null) {
                        ziadneNaplanovaneUdalosti.setVisibility(View.GONE);
                        ziskajNaplanovaneUdalosti(udaje);
                    } else {
                        if (!(Pocet.KALENDAR_ZOZNAM_OD > 0)) {
                            ziadneNaplanovaneUdalosti.setVisibility(View.VISIBLE);
                        }
                    }
                    zoznamNaplnovanychUdalosti.setItemViewCacheSize(naplnovaneUdalosti.size());
                }
                break;
        }
        this.nacitavanie.setVisibility(View.GONE);
    }

    @Override
    public void odpovedServera(String odpoved, String od, HashMap<String, String> udaje) {
        switch (od) {
            case Status.ODSTRANENIE_UDALOSTI_ZO_ZAUJMOV:
                if (odpoved.equals(Status.VSETKO_V_PORIADKU)) {
                    if (udaje != null) {
                        Snackbar snackbar = null;

                        if (udaje.get("uspech") != null) {
                            snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), udaje.get("uspech"), Snackbar.LENGTH_SHORT);
                        } else if (udaje.get("chyba") != null) {
                            snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), udaje.get("chyba"), Snackbar.LENGTH_SHORT);

                        }

                        View pozadie = snackbar.getView();
                        pozadie.setBackgroundColor(getResources().getColor(R.color.toolbar));
                        snackbar.setActionTextColor(getResources().getColor(android.R.color.white));
                        snackbar.show();
                    }
                }
                break;
        }
    }

    @Override
    public void nacitajDalsieNaplanovaneUdalosti() {
        Pocet.KALENDAR_ZOZNAM_OD += 5;
        udalostiUdaje.zoznamNaplanovanychUdalosti(email, Pocet.KALENDAR_ZOZNAM_OD, Pocet.KALENDAR_ZOZNAM_POCET, token);
    }

    protected void ziskajNaplanovaneUdalosti(ArrayList<Informacie> udalosti) {
        int posladnaPozicia = skupina.size() - 1;

        this.naplnovaneUdalosti.addAll(udalosti);
        for (String skupina : zoSkupinovanieUdajov(udalosti).keySet()) {
            Mesiac mesiac = new Mesiac();
            mesiac.setMesiac(skupina);
            this.skupina.add(mesiac);

            for (Informacie informacie : zoSkupinovanieUdajov(udalosti).get(skupina)) {
                NaplanovanaUdalost naplanovanaUdalost = new NaplanovanaUdalost();
                naplanovanaUdalost.setInformacie(informacie);
                this.skupina.add(naplanovanaUdalost);
            }
        }

        kalendarAdapter.notifyItemRangeInserted(posladnaPozicia, skupina.size());
    }

    private void init(View view) {
        this.email = getArguments().getString("email");
        this.token = getArguments().getString("token");

        this.naplnovaneUdalosti = new ArrayList<>();
        this.skupina = new ArrayList<>();

        this.zoznamNaplnovanychUdalosti = view.findViewById(R.id.zoznam_udalosti_o_ktorych_ma_pouzivatel_zaujem);
        this.ziadneNaplanovaneUdalosti = view.findViewById(R.id.ziadne_naplanovane_udalosti);
        this.nacitavanie = view.findViewById(R.id.nacitavanie);

        nastavZoznamNaplnovanychUdalosti();

        this.udalostiUdaje = new UdalostiUdaje(this, this, getContext());
    }

    private void nastavZoznamNaplnovanychUdalosti() {
        this.kalendarAdapter = new KalendarAdapter(skupina, this);
        PoskitovelObsahu poskitovelObsahu = new PoskitovelObsahu(getContext());

        zoznamNaplnovanychUdalosti.setLayoutManager(poskitovelObsahu);
        zoznamNaplnovanychUdalosti.setItemAnimator(new DefaultItemAnimator());
        zoznamNaplnovanychUdalosti.setAdapter(kalendarAdapter);

        ItemTouchHelper.SimpleCallback gesto = new Gesta(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(gesto).attachToRecyclerView(zoznamNaplnovanychUdalosti);
    }

    private TreeMap<String, List<Informacie>> zoSkupinovanieUdajov(List<Informacie> udaje) {
        TreeMap<String, List<Informacie>> udalosti = new TreeMap<>();
        for (Informacie informacie : udaje) {
            String skupina = informacie.getDatum();
            if (udalosti.containsKey(skupina)) {
                udalosti.get(skupina).add(informacie);
            } else {
                List<Informacie> zoznam = new ArrayList<>();
                zoznam.add(informacie);
                udalosti.put(skupina, zoznam);
            }
        }
        return udalosti;
    }
}