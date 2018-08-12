package com.mate.bence.udalosti.Activity.Navigacia.Fragment.Udalosti.Karty;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.mate.bence.udalosti.Activity.Navigacia.Fragment.Udalosti.UdalostiUdaje;
import com.mate.bence.udalosti.Dialog.Zoznam.DialogZoznam;
import com.mate.bence.udalosti.List.PoskitovelObsahu;
import com.mate.bence.udalosti.List.Udalosti.TlacidlaGesta;
import com.mate.bence.udalosti.List.Udalosti.Udalost;
import com.mate.bence.udalosti.List.Udalosti.UdalostAdapter;
import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Nastavenia.Pocet;
import com.mate.bence.udalosti.Udaje.Nastavenia.Status;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaData;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Objavuj extends Fragment implements KommunikaciaData, KommunikaciaOdpoved, TlacidlaGesta {

    private String email, stat, token;
    private boolean aktualizacia = false;

    private List<Udalost> obsahUdalosti = new ArrayList<>();
    private List<Udalost> udalostiNaServeri;

    private UdalostiUdaje udalostiUdaje;
    private UdalostAdapter udalostAdapter;

    private RecyclerView zoznamUdalosti;
    private LinearLayout ziadneUdalosti;

    private PoskitovelObsahu poskitovelObsahu;
    private SwipeRefreshLayout aktualizujZoznamUdalosti;
    private ProgressBar nacitavanie;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_udalosti, container, false);
        init(view);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.ikona_hladat, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.nav_hladat).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(hladanaUdalost);
    }

    private SearchView.OnQueryTextListener hladanaUdalost = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String nazov) {
            udalostiUdaje.hladanaUdalost(email, nazov, stat, token);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String query) {
            udalostAdapter.getFilter().filter(query);
            return false;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_hladat:
                item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        udalostiNaServeri = null;
                        nastavZoznamUdalosti(obsahUdalosti);
                        return true;
                    }
                });
        }
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        Pocet.UDALOSTI_ZOZNAM_UKONCIL = Pocet.UDALOSTI_ZOZNAM_POCET;
    }

    @Override
    public void onResume() {
        super.onResume();

        Pocet.UDALOSTI_ZOZNAM_OD = 0;
        if (Pocet.UDALOSTI_ZOZNAM_UKONCIL != 0) {
            Pocet.UDALOSTI_ZOZNAM_OD = Pocet.UDALOSTI_ZOZNAM_UKONCIL;
            Pocet.UDALOSTI_ZOZNAM_OD -= 5;
        }

        if (obsahUdalosti.isEmpty()) {
            this.nacitavanie.setVisibility(View.VISIBLE);
            udalostiUdaje.zoznamUdalosti(email, null, null, stat, Pocet.UDALOSTI_ZOZNAM_OD, Pocet.UDALOSTI_ZOZNAM_POCET, token);
        }
    }

    @Override
    public void dataZoServera(String odpoved, String od, ArrayList udaje) {
        switch (od) {
            case Status.UDALOSTI_OBJAVUJ:
                if (odpoved.equals(Status.VSETKO_V_PORIADKU)) {
                    if (udaje != null) {
                        aktualizujZoznamUdalosti.setVisibility(View.VISIBLE);
                        ziadneUdalosti.setVisibility(View.GONE);
                        if (!aktualizacia) {
                            ziskajUdalosti(udaje);
                        } else {
                            nacitajNovuUdalost(udaje);
                        }
                    } else {
                        if (!(Pocet.UDALOSTI_ZOZNAM_OD > 0) && !(aktualizacia)) {
                            aktualizujZoznamUdalosti.setVisibility(View.GONE);
                            ziadneUdalosti.setVisibility(View.VISIBLE);
                        }
                    }
                    zoznamUdalosti.setItemViewCacheSize(obsahUdalosti.size());
                }
                break;

            case Status.VYHLADAVANIE_UDALOSTI:
                if (odpoved.equals(Status.VSETKO_V_PORIADKU)) {
                    udalostiNaServeri = new ArrayList<>();
                    udalostiNaServeri.addAll(udaje);
                    nastavZoznamUdalosti(udalostiNaServeri);
                    udalostAdapter.notifyDataSetChanged();
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

                    if (udaje != null) {
                        Snackbar snackbar = null;
                        if (udaje.get("uspech") != null) {
                            snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), udaje.get("uspech"), Snackbar.LENGTH_SHORT);

                        } else if (udaje.get("chyba") != null) {
                            snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), udaje.get("chyba"), Snackbar.LENGTH_SHORT);
                        }
                        View pozadie = snackbar.getView();
                        pozadie.setBackgroundColor(getContext().getResources().getColor(R.color.toolbar));
                        snackbar.setActionTextColor(getContext().getResources().getColor(android.R.color.white));
                        snackbar.show();
                    }
                }
                break;
        }
    }

    @Override
    public void budemTam(int idUdalost) {
        udalostiUdaje.zaujemOudalost(email, idUdalost, token);
    }

    @Override
    public void pozvat(int idUdalost) {
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("token", token);
        bundle.putInt("idUdalost", idUdalost);

        DialogZoznam dialogZoznam = new DialogZoznam();
        dialogZoznam.setArguments(bundle);

        dialogZoznam.show(getFragmentManager(), "pozvanka");
    }

    @Override
    public void nacitajDalsieUdalosti() {
        Pocet.UDALOSTI_ZOZNAM_OD += 5;
        udalostiUdaje.zoznamUdalosti(email, null, null, stat, Pocet.UDALOSTI_ZOZNAM_OD, Pocet.UDALOSTI_ZOZNAM_POCET, token);
    }

    protected void ziskajUdalosti(ArrayList<Udalost> udalosti) {
        obsahUdalosti.addAll(udalosti);
        udalostAdapter.notifyItemRangeInserted(obsahUdalosti.size() - 1, udalosti.size());
    }

    protected void nacitajNovuUdalost(ArrayList<Udalost> udalosti) {
        aktualizacia = false;

        for (Udalost udalost : udalosti) {
            if (!(obsahUdalosti.isEmpty())) {
                obsahUdalosti.add(0, udalost);
            }
        }

        udalostAdapter.notifyItemRangeInserted(0, udalosti.size());
        poskitovelObsahu.scrollToPosition(0);
    }

    private void init(View view) {
        this.email = getArguments().getString("email");
        this.stat = getArguments().getString("stat");
        this.token = getArguments().getString("token");

        this.aktualizujZoznamUdalosti = view.findViewById(R.id.aktualizuj_udalosti);
        this.zoznamUdalosti = view.findViewById(R.id.zoznam_udalosti);
        this.ziadneUdalosti = view.findViewById(R.id.ziadne_udalosti);
        this.nacitavanie = view.findViewById(R.id.nacitavanie);

        this.udalostiNaServeri = null;
        this.obsahUdalosti = new ArrayList<>();
        nastavZoznamUdalosti(obsahUdalosti);

        this.udalostiUdaje = new UdalostiUdaje(this, this, getContext());
        this.aktualizujZoznamUdalosti.setOnRefreshListener(aktualizujZoznam);
    }

    private void nastavZoznamUdalosti(List<Udalost> udaje) {
        this.udalostAdapter = new UdalostAdapter(udaje, this, getContext());
        this.poskitovelObsahu = new PoskitovelObsahu(getContext());
        zoznamUdalosti.setLayoutManager(poskitovelObsahu);
        zoznamUdalosti.setItemAnimator(new DefaultItemAnimator());
        zoznamUdalosti.setAdapter(udalostAdapter);
    }

    private SwipeRefreshLayout.OnRefreshListener aktualizujZoznam = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (obsahUdalosti != null && !(obsahUdalosti.isEmpty())) {
                udalostiUdaje.zoznamUdalosti(email, obsahUdalosti.get(0).getIdUdalost(), obsahUdalosti.get(0).getDatum(), stat, Pocet.UDALOSTI_ZOZNAM_OD, Pocet.UDALOSTI_ZOZNAM_POCET, token);
            }
            aktualizacia = true;
            aktualizujZoznamUdalosti.setRefreshing(false);
        }
    };
}