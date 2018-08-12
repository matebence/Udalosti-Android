package com.mate.bence.udalosti.Activity.Navigacia.Fragment.Ludia.Karty;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mate.bence.udalosti.Activity.Navigacia.Fragment.Ludia.LudiaUdaje;
import com.mate.bence.udalosti.List.Gesta;
import com.mate.bence.udalosti.List.Ludia.Ludia;
import com.mate.bence.udalosti.List.Ludia.TlacidlaGesta;
import com.mate.bence.udalosti.List.Ludia.Vsetci.VsetciAdapter;
import com.mate.bence.udalosti.List.PoskitovelObsahu;
import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Nastavenia.Pocet;
import com.mate.bence.udalosti.Udaje.Nastavenia.Status;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaData;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Vsetci extends Fragment implements KommunikaciaData, KommunikaciaOdpoved, TlacidlaGesta, Gesta.RecyclerItemTouchHelperListener {

    private String email, token;

    private List<Ludia> pouzivatelia = new ArrayList<>();
    private List<Ludia> vyhladavaniePouzivatelov;

    private LudiaUdaje ludiaUdaje;
    private VsetciAdapter vsetciAdapter;

    private RecyclerView zoznamPouzivatelov;

    private ProgressBar nacitavanie;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_ludia_vsetci, container, false);
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

        searchView.setOnQueryTextListener(hladanyPouzivatelov);
    }

    private SearchView.OnQueryTextListener hladanyPouzivatelov = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String nazov) {
            ludiaUdaje.hladanyPouzivatel(email, nazov, token);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String query) {
            vsetciAdapter.getFilter().filter(query);
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
                        vyhladavaniePouzivatelov = null;
                        nastavZoznamPouzivatelov(pouzivatelia);
                        return true;
                    }
                });
        }
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        Pocet.VSETCI_ZOZNAM_UKONCIL = Pocet.VSETCI_ZOZNAM_POCET;
    }

    @Override
    public void onResume() {
        super.onResume();

        Pocet.VSETCI_ZOZNAM_OD = 0;
        if (Pocet.VSETCI_ZOZNAM_UKONCIL != 0) {
            Pocet.VSETCI_ZOZNAM_OD = Pocet.VSETCI_ZOZNAM_UKONCIL;
            Pocet.VSETCI_ZOZNAM_OD -= 5;
        }

        if (pouzivatelia.isEmpty()) {
            this.nacitavanie.setVisibility(View.VISIBLE);
            ludiaUdaje.zoznamPouzivatelov(email, Pocet.VSETCI_ZOZNAM_OD, Pocet.VSETCI_ZOZNAM_POCET, token);
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof VsetciAdapter.VsetciHolder) {
            if (vyhladavaniePouzivatelov != null) {
                this.ludiaUdaje.novaZiadost(email, vyhladavaniePouzivatelov.get(viewHolder.getAdapterPosition()).getIdPouzivatel(), token);
            } else {
                this.ludiaUdaje.novaZiadost(email, pouzivatelia.get(viewHolder.getAdapterPosition()).getIdPouzivatel(), token);
            }
            this.vsetciAdapter.oznacitAkoPriatel(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void dataZoServera(String odpoved, String od, ArrayList udaje) {
        switch (od) {
            case Status.LUDIA_VSETCI:
                if (odpoved.equals(Status.VSETKO_V_PORIADKU)) {

                    if (udaje != null) {
                        ziskajPouzivatelov(udaje);
                    }
                    zoznamPouzivatelov.setItemViewCacheSize(pouzivatelia.size());
                }
                break;

            case Status.VYHLADAVANIE_LUDI:
                if (odpoved.equals(Status.VSETKO_V_PORIADKU)) {
                    vyhladavaniePouzivatelov = new ArrayList<>();
                    vyhladavaniePouzivatelov.addAll(udaje);
                    nastavZoznamPouzivatelov(vyhladavaniePouzivatelov);
                    vsetciAdapter.notifyDataSetChanged();
                }
                break;
        }
        this.nacitavanie.setVisibility(View.GONE);
    }

    @Override
    public void odpovedServera(String odpoved, String od, HashMap<String, String> udaje) {
        switch (od) {
            case Status.UDALOSTI_ZIADOSTI:
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
        Pocet.VSETCI_ZOZNAM_OD += 5;
        ludiaUdaje.zoznamPouzivatelov(email, Pocet.VSETCI_ZOZNAM_OD, Pocet.VSETCI_ZOZNAM_POCET, token);
    }

    protected void ziskajPouzivatelov(ArrayList<Ludia> pouzivatelia) {
        this.pouzivatelia.addAll(pouzivatelia);
        vsetciAdapter.notifyItemRangeInserted(this.pouzivatelia.size() - 1, pouzivatelia.size());
    }

    private void init(View view) {
        this.email = getArguments().getString("email");
        this.token = getArguments().getString("token");

        this.zoznamPouzivatelov = view.findViewById(R.id.zoznam_pouzivatelov);
        this.nacitavanie = view.findViewById(R.id.nacitavanie);

        this.vyhladavaniePouzivatelov = null;
        this.pouzivatelia = new ArrayList<>();
        nastavZoznamPouzivatelov(pouzivatelia);

        this.ludiaUdaje = new LudiaUdaje(this, this, getContext());
    }

    private void nastavZoznamPouzivatelov(List<Ludia> udaje) {
        this.vsetciAdapter = new VsetciAdapter(udaje, this, getContext());
        PoskitovelObsahu poskitovelObsahu = new PoskitovelObsahu(getContext());
        zoznamPouzivatelov.setLayoutManager(poskitovelObsahu);
        zoznamPouzivatelov.setItemAnimator(new DefaultItemAnimator());
        zoznamPouzivatelov.setAdapter(vsetciAdapter);
        ItemTouchHelper.SimpleCallback gesto = new Gesta(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(gesto).attachToRecyclerView(zoznamPouzivatelov);
    }
}