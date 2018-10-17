package com.mate.bence.udalosti.Activity.Udalosti.Karty;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.mate.bence.udalosti.Activity.Udalosti.Podrobnosti.Aktualizator;
import com.mate.bence.udalosti.Activity.Udalosti.Podrobnosti.Podrobnosti;
import com.mate.bence.udalosti.Activity.Udalosti.Podrobnosti.AktualizatorObsahu;
import com.mate.bence.udalosti.Activity.Udalosti.UdalostiUdaje;
import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Nastavenia.Nastavenia;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaData;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;
import com.mate.bence.udalosti.Zoznam.PoskitovelObsahu;
import com.mate.bence.udalosti.Zoznam.Udalost;
import com.mate.bence.udalosti.Zoznam.UdalostAdapter;
import com.mate.bence.udalosti.Zoznam.ZvolenaUdalost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Objavuj extends Fragment implements KommunikaciaData, KommunikaciaOdpoved, ZvolenaUdalost, Aktualizator {

    private ArrayList<Integer> zmeneneUdalosti;
    private List<Udalost> obsahUdalosti;

    private SwipeRefreshLayout aktualizujUdalosti;

    private UdalostiUdaje udalostiUdaje;
    private UdalostAdapter udalostAdapter;

    private RecyclerView zoznamUdalosti;
    private LinearLayout ziadneUdalosti;

    private String email, stat, token;
    private ProgressBar nacitavanie;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_udalosti_lokalizator_udalosti, container, false);
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

    @Override
    public void podrobnostiUdalosti(View view, int pozicia) {
        boolean zmenene = false;
        Udalost udalost = obsahUdalosti.get(pozicia);
        Intent zvolenaUdalost = new Intent(getActivity(), Podrobnosti.class);

        if(!zmeneneUdalosti.isEmpty()){
            for(int i = 0; i<zmeneneUdalosti.size();i++){
                if(zmeneneUdalosti.get(i) == udalost.getIdUdalost()){
                    zmeneneUdalosti.remove(i);
                    zmenene = true;
                }
            }
        }

        if(!zmenene){
            zvolenaUdalost.putExtra("pozicia", pozicia);
            zvolenaUdalost.putExtra("karta", Objavuj.class.getSimpleName());

            zvolenaUdalost.putExtra("zaujemUdalosti", udalost.getZaujem());

            zvolenaUdalost.putExtra("obrazok", udalost.getObrazok());
            zvolenaUdalost.putExtra("nazov", udalost.getNazov());
            zvolenaUdalost.putExtra("den", udalost.getDen());
            zvolenaUdalost.putExtra("mesiac", udalost.getMesiac());
            zvolenaUdalost.putExtra("cas", udalost.getCas());
            zvolenaUdalost.putExtra("mesto", udalost.getMesto());
            zvolenaUdalost.putExtra("ulica", udalost.getUlica());
            zvolenaUdalost.putExtra("vstupenka", udalost.getVstupenka());
            zvolenaUdalost.putExtra("zaujemcovia", udalost.getZaujemcovia());
        }

        zvolenaUdalost.putExtra("idUdalost", udalost.getIdUdalost());
        zvolenaUdalost.putExtra("token", token);
        zvolenaUdalost.putExtra("email", email);

        AktualizatorObsahu.stav().nastav(this);
        startActivity(zvolenaUdalost);
        getActivity().overridePendingTransition(R.anim.vstupit_vychod_activity, R.anim.vstupit_vchod_activity);
    }

    @Override
    public void aktualizujObsahUdalosti() {
        Log.v("MainAct", "Objavuj");

        if(AktualizatorObsahu.stav().getKarta().equals(Objavuj.class.getSimpleName())){
            obsahUdalosti.get(AktualizatorObsahu.stav().getPozcia()).setZaujem(AktualizatorObsahu.stav().getStav());
        }else{
            zmeneneUdalosti.add(AktualizatorObsahu.stav().getIdUdalost());
        }
    }

    private void init(View view) {

        this.email = getArguments().getString("email");
        this.stat = getArguments().getString("stat");
        this.token = getArguments().getString("token");

        this.zoznamUdalosti = view.findViewById(R.id.zoznam_udalosti);
        this.ziadneUdalosti = view.findViewById(R.id.ziadne_udalosti);
        this.nacitavanie = view.findViewById(R.id.nacitavanie);
        this.aktualizujUdalosti = view.findViewById(R.id.aktualizuj);

        this.aktualizujUdalosti.setOnRefreshListener(aktualizuj);
        this.aktualizujUdalosti.setColorSchemeColors(getResources().getColor(R.color.nacitavanie));

        this.obsahUdalosti = new ArrayList<>();
        this.zmeneneUdalosti = new ArrayList<>();

        nastavZoznamUdalosti(obsahUdalosti);
        this.udalostiUdaje = new UdalostiUdaje(this, this, getContext());
    }

    protected void ziskajUdalosti(ArrayList<Udalost> udalosti) {
        obsahUdalosti.addAll(udalosti);
        udalostAdapter.notifyItemRangeInserted(0, udalosti.size());
        zoznamUdalosti.setVisibility(View.VISIBLE);
    }

    private void nastavZoznamUdalosti(List<Udalost> udaje) {
        PoskitovelObsahu poskitovelObsahu = new PoskitovelObsahu(getContext());

        udalostAdapter = new UdalostAdapter(udaje, getContext());
        zoznamUdalosti.setLayoutManager(poskitovelObsahu);
        zoznamUdalosti.setItemAnimator(new DefaultItemAnimator());
        zoznamUdalosti.setAdapter(udalostAdapter);
        udalostAdapter.zvolenaUdalost(this);
    }

    private SwipeRefreshLayout.OnRefreshListener aktualizuj = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            obsahUdalosti.clear();
            udalostAdapter.notifyItemRangeRemoved(0, obsahUdalosti.size());

            ziadneUdalosti.setVisibility(View.GONE);
            zoznamUdalosti.setVisibility(View.GONE);
            nacitavanie.setVisibility(View.VISIBLE);

            udalostiUdaje.zoznamUdalosti(email, stat, token);

            aktualizujUdalosti.setRefreshing(false);
        }
    };
}