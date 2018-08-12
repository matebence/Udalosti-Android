package com.mate.bence.udalosti.Activity.Navigacia.Fragment.Ludia.Karty;

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

import com.mate.bence.udalosti.Activity.Navigacia.Fragment.Ludia.LudiaUdaje;
import com.mate.bence.udalosti.List.Gesta;
import com.mate.bence.udalosti.List.Ludia.Ludia;
import com.mate.bence.udalosti.List.Ludia.Priatelia.PriateliaAdapter;
import com.mate.bence.udalosti.List.Ludia.Priatelia.Struktura.Meno;
import com.mate.bence.udalosti.List.Ludia.Priatelia.Struktura.Pouzivatel;
import com.mate.bence.udalosti.List.Ludia.Priatelia.Struktura.Zoznam;
import com.mate.bence.udalosti.List.Ludia.TlacidlaGesta;
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

public class Priatelia extends Fragment implements KommunikaciaData, KommunikaciaOdpoved, TlacidlaGesta, Gesta.RecyclerItemTouchHelperListener {

    private String email, token;

    private List<Ludia> priatelia;
    private List<Zoznam> skupina;

    private LudiaUdaje ludiaUdaje;
    private PriateliaAdapter priateliaAdapter;

    private RecyclerView zoznamPriatelov;
    private LinearLayout ziadnyPriatelia;
    private ProgressBar nacitavanie;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ludia_priatelia, container, false);
        init(view);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Pocet.PRIATELIA_ZOZNAM_UKONCIL = Pocet.PRIATELIA_ZOZNAM_POCET;
    }

    @Override
    public void onResume() {
        super.onResume();

        Pocet.PRIATELIA_ZOZNAM_OD = 0;
        if (Pocet.PRIATELIA_ZOZNAM_UKONCIL != 0) {
            Pocet.PRIATELIA_ZOZNAM_OD = Pocet.PRIATELIA_ZOZNAM_UKONCIL;
            Pocet.PRIATELIA_ZOZNAM_OD -= 5;
        }

        if (priatelia.isEmpty()) {
            this.nacitavanie.setVisibility(View.VISIBLE);
            ludiaUdaje.zoznamPriatelov(email, Pocet.PRIATELIA_ZOZNAM_OD, Pocet.PRIATELIA_ZOZNAM_POCET, token);
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof PriateliaAdapter.PouzivatelHolder) {

            Pouzivatel pouzivatel = (Pouzivatel) skupina.get(position);

            this.ludiaUdaje.odstraneniePriatelstva(email, pouzivatel.getUdaje().getIdPouzivatel(), token);
            this.priateliaAdapter.odstranPouzivatela(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void dataZoServera(String odpoved, String od, ArrayList udaje) {
        switch (od) {
            case Status.LUDIA_PRIATELIA:
                if (odpoved.equals(Status.VSETKO_V_PORIADKU)) {
                    if (udaje != null) {
                        ziadnyPriatelia.setVisibility(View.GONE);
                        ziskajPriatelov(udaje);
                    } else {
                        if (!(Pocet.PRIATELIA_ZOZNAM_OD > 0)) {
                            ziadnyPriatelia.setVisibility(View.VISIBLE);
                        }
                    }
                    zoznamPriatelov.setItemViewCacheSize(priatelia.size());
                }
                break;
        }
        this.nacitavanie.setVisibility(View.GONE);
    }

    @Override
    public void odpovedServera(String odpoved, String od, HashMap<String, String> udaje) {
        switch (od) {
            case Status.ODSTRANENIE_PRIATELA:
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
    public void nacitajDalsychPouzivatelov() {
        Pocet.PRIATELIA_ZOZNAM_OD += 5;
        ludiaUdaje.zoznamPriatelov(email, Pocet.PRIATELIA_ZOZNAM_OD, Pocet.PRIATELIA_ZOZNAM_POCET, token);
    }

    protected void ziskajPriatelov(ArrayList<Ludia> priatelia) {
        int posladnaPozicia = skupina.size() - 1;

        this.priatelia.addAll(priatelia);
        for (String skupina : zoSkupinovanieUdajov(priatelia).keySet()) {
            Meno meno = new Meno();
            meno.setMeno(skupina);
            this.skupina.add(meno);

            for (Ludia ludia : zoSkupinovanieUdajov(priatelia).get(skupina)) {
                Pouzivatel pouzivatel = new Pouzivatel();
                pouzivatel.setUdaje(ludia);
                this.skupina.add(pouzivatel);
            }
        }

        priateliaAdapter.notifyItemRangeInserted(posladnaPozicia, skupina.size());
    }

    private void init(View view) {
        this.email = getArguments().getString("email");
        this.token = getArguments().getString("token");

        this.priatelia = new ArrayList<>();
        this.skupina = new ArrayList<>();

        this.zoznamPriatelov = view.findViewById(R.id.zoznam_priatelov);
        this.ziadnyPriatelia = view.findViewById(R.id.ziadny_priatelia);
        this.nacitavanie = view.findViewById(R.id.nacitavanie);

        nastavZoznamPriatelov();

        this.ludiaUdaje = new LudiaUdaje(this, this, getContext());
    }

    private void nastavZoznamPriatelov() {
        this.priateliaAdapter = new PriateliaAdapter(skupina, this, getContext());
        PoskitovelObsahu poskitovelObsahu = new PoskitovelObsahu(getContext());

        zoznamPriatelov.setLayoutManager(poskitovelObsahu);
        zoznamPriatelov.setItemAnimator(new DefaultItemAnimator());
        zoznamPriatelov.setAdapter(priateliaAdapter);

        ItemTouchHelper.SimpleCallback gesto = new Gesta(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(gesto).attachToRecyclerView(zoznamPriatelov);
    }

    private TreeMap<String, List<Ludia>> zoSkupinovanieUdajov(List<Ludia> udaje) {
        TreeMap<String, List<Ludia>> pouzivatelia = new TreeMap<>();
        for (Ludia ludia : udaje) {
            String skupina = ludia.getMeno().substring(0, 1).toUpperCase();
            if (pouzivatelia.containsKey(skupina)) {
                pouzivatelia.get(skupina).add(ludia);
            } else {
                List<Ludia> zoznam = new ArrayList<>();
                zoznam.add(ludia);
                pouzivatelia.put(skupina, zoznam);
            }
        }
        return pouzivatelia;
    }
}