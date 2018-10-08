package com.mate.bence.udalosti.Activity.Udalosti.Karty;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.mate.bence.udalosti.Activity.Udalosti.UdalostiUdaje;
import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Nastavenia.Nastavenia;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaData;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;
import com.mate.bence.udalosti.Zoznam.PoskitovelObsahu;
import com.mate.bence.udalosti.Zoznam.Udalost;
import com.mate.bence.udalosti.Zoznam.UdalostAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Objavuj extends Fragment implements KommunikaciaData, KommunikaciaOdpoved {

    private List<Udalost> obsahUdalosti = new ArrayList<>();
    private SwipeRefreshLayout aktualizujUdalosti;

    private UdalostiUdaje udalostiUdaje;
    private UdalostAdapter udalostAdapter;

    private RecyclerView zoznamUdalosti;
    private LinearLayout ziadneUdalosti;

    private String email, stat, token;
    private ProgressBar nacitavanie;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_udalosti, container, false);
        init(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (obsahUdalosti.isEmpty()) {
            this.nacitavanie.setVisibility(View.VISIBLE);
            udalostiUdaje.zoznamUdalosti(email, stat, token);
        }
    }

    @Override
    public void dataZoServera(String odpoved, String od, ArrayList udaje) {
        switch (od) {
            case Nastavenia.UDALOSTI_OBJAVUJ:
                if (odpoved.equals(Nastavenia.VSETKO_V_PORIADKU)) {
                    if (udaje != null) {
                        ziadneUdalosti.setVisibility(View.GONE);
                        ziskajUdalosti(udaje);
                    } else {
                        ziadneUdalosti.setVisibility(View.VISIBLE);
                    }
                    zoznamUdalosti.setItemViewCacheSize(obsahUdalosti.size());
                }
                break;
        }
        this.nacitavanie.setVisibility(View.GONE);
    }

    @Override
    public void odpovedServera(String odpoved, String od, HashMap<String, String> udaje) {
    }

    private void init(View view) {
        this.email = getArguments().getString("email");
        this.stat = getArguments().getString("stat");
        this.token = getArguments().getString("token");

        this.zoznamUdalosti = view.findViewById(R.id.zoznam_udalosti);
        this.ziadneUdalosti = view.findViewById(R.id.ziadne_udalosti);
        this.nacitavanie = view.findViewById(R.id.nacitavanie);
        this.aktualizujUdalosti = view.findViewById(R.id.aktualizuj);

        this.aktualizujUdalosti.setOnRefreshListener(noveUdalosti);
        this.aktualizujUdalosti.setColorSchemeColors(getResources().getColor(R.color.nacitavanie_progressbar));

        this.obsahUdalosti = new ArrayList<>();
        nastavZoznamUdalosti(obsahUdalosti);

        this.udalostiUdaje = new UdalostiUdaje(this, this, getContext());
    }

    protected void ziskajUdalosti(ArrayList<Udalost> udalosti) {
        obsahUdalosti.addAll(udalosti);
        udalostAdapter.notifyItemRangeInserted(0, udalosti.size());
    }

    private SwipeRefreshLayout.OnRefreshListener noveUdalosti = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            obsahUdalosti.clear();
            udalostAdapter.notifyItemRangeRemoved(0, obsahUdalosti.size());

            nacitavanie.setVisibility(View.VISIBLE);
            udalostiUdaje.zoznamUdalosti(email, stat, token);

            aktualizujUdalosti.setRefreshing(false);
        }
    };

    private void nastavZoznamUdalosti(List<Udalost> udaje) {
        PoskitovelObsahu poskitovelObsahu = new PoskitovelObsahu(getContext());

        udalostAdapter = new UdalostAdapter(udaje, getContext());
        zoznamUdalosti.setLayoutManager(poskitovelObsahu);
        zoznamUdalosti.setItemAnimator(new DefaultItemAnimator());
        zoznamUdalosti.setAdapter(udalostAdapter);
    }
}