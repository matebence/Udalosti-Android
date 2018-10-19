package com.mate.bence.udalosti.Activity.Udalosti.Karty;

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

import com.mate.bence.udalosti.Activity.Udalosti.UdalostiUdaje;
import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Nastavenia.Nastavenia;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaData;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;
import com.mate.bence.udalosti.Zoznam.PoskitovelObsahu;
import com.mate.bence.udalosti.Zoznam.Udalost;
import com.mate.bence.udalosti.Zoznam.Zaujmy.OdstranenieZaujmu;
import com.mate.bence.udalosti.Zoznam.Zaujmy.Struktura.Mesiac;
import com.mate.bence.udalosti.Zoznam.Zaujmy.Struktura.NaplanovanaUdalost;
import com.mate.bence.udalosti.Zoznam.Zaujmy.Struktura.Zoznam;
import com.mate.bence.udalosti.Zoznam.Zaujmy.ZaujemAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class Zaujmy extends Fragment implements KommunikaciaData, KommunikaciaOdpoved, OdstranenieZaujmu.RecyclerItemTouchHelperListener {

    private String email, token;

    private List<Udalost> naplnovaneUdalosti;
    private List<Zoznam> skupina;

    private UdalostiUdaje udalostiUdaje;
    private ZaujemAdapter kalendarAdapter;

    private RecyclerView zoznamNaplnovanychUdalosti;
    private LinearLayout ziadneNaplanovaneUdalosti;
    private ProgressBar nacitavanie;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_udalosti_zaujmy, container, false);
        init(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (naplnovaneUdalosti.isEmpty()) {
            this.nacitavanie.setVisibility(View.VISIBLE);
            udalostiUdaje.zoznamZaujmov(email, token);
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ZaujemAdapter.NaplanovanaUdalostHolder) {

            NaplanovanaUdalost naplanovanaUdalost = (NaplanovanaUdalost) skupina.get(position);

            this.udalostiUdaje.odstranZaujem(token, email, naplanovanaUdalost.getUdalost().getIdUdalost());
            this.kalendarAdapter.odstranNaplanovanuUdalost(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void dataZoServera(String odpoved, String od, ArrayList udaje) {
        switch (od) {
            case Nastavenia.ZAUJEM_ZOZNAM:
                if (odpoved.equals(Nastavenia.VSETKO_V_PORIADKU)) {
                    if (udaje != null) {
                        ziadneNaplanovaneUdalosti.setVisibility(View.GONE);
                        ziskajNaplanovaneUdalosti(udaje);
                    } else {
                       ziadneNaplanovaneUdalosti.setVisibility(View.VISIBLE);
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
            case Nastavenia.ZAUJEM_ODSTRANENIE:
                if (odpoved.equals(Nastavenia.VSETKO_V_PORIADKU)) {
                    if (udaje != null) {
                        Snackbar snackbar = null;

                        if (udaje.get("uspech") != null) {
                            snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), udaje.get("uspech"), Snackbar.LENGTH_SHORT);
                        } else if (udaje.get("chyba") != null) {
                            snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), udaje.get("chyba"), Snackbar.LENGTH_SHORT);

                        }

                        View pozadie = snackbar.getView();
                        pozadie.setBackgroundColor(getResources().getColor(R.color.farba_primarna));
                        snackbar.setActionTextColor(getResources().getColor(android.R.color.white));
                        snackbar.show();
                    }
                }
                break;
        }
    }

    protected void ziskajNaplanovaneUdalosti(ArrayList<Udalost> udalosti) {
        int posladnaPozicia = skupina.size() - 1;

        this.naplnovaneUdalosti.addAll(udalosti);
        for (String skupina : zoSkupinovanieUdajov(udalosti).keySet()) {
            Mesiac mesiac = new Mesiac();
            mesiac.setMesiac(skupina);
            this.skupina.add(mesiac);

            for (Udalost informacie : zoSkupinovanieUdajov(udalosti).get(skupina)) {
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
        this.kalendarAdapter = new ZaujemAdapter(skupina);
        PoskitovelObsahu poskitovelObsahu = new PoskitovelObsahu(getContext());

        zoznamNaplnovanychUdalosti.setLayoutManager(poskitovelObsahu);
        zoznamNaplnovanychUdalosti.setItemAnimator(new DefaultItemAnimator());
        zoznamNaplnovanychUdalosti.setAdapter(kalendarAdapter);

        ItemTouchHelper.SimpleCallback gesto = new OdstranenieZaujmu(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(gesto).attachToRecyclerView(zoznamNaplnovanychUdalosti);
    }

    private TreeMap<String, List<Udalost>> zoSkupinovanieUdajov(List<Udalost> udaje) {
        TreeMap<String, List<Udalost>> udalosti = new TreeMap<>();
        for (Udalost informacie : udaje) {
            String skupina = informacie.getMesiac();
            if (udalosti.containsKey(skupina)) {
                udalosti.get(skupina).add(informacie);
            } else {
                List<Udalost> zoznam = new ArrayList<>();
                zoznam.add(informacie);
                udalosti.put(skupina, zoznam);
            }
        }
        return udalosti;
    }
}