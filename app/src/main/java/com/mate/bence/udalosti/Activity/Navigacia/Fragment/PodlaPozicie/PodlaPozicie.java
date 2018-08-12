package com.mate.bence.udalosti.Activity.Navigacia.Fragment.PodlaPozicie;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.mate.bence.udalosti.Dialog.Zoznam.DialogZoznam;
import com.mate.bence.udalosti.List.PoskitovelObsahu;
import com.mate.bence.udalosti.List.Udalosti.TlacidlaGesta;
import com.mate.bence.udalosti.List.Udalosti.Udalost;
import com.mate.bence.udalosti.List.Udalosti.UdalostAdapter;
import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Nastavenia.Status;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaData;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PodlaPozicie extends Fragment implements KommunikaciaData, KommunikaciaOdpoved, TlacidlaGesta {

    private String email, stat, okres, mesto, token;

    private List<Udalost> obsahUdalostiPodlaPozicie;

    private PodlaPozicieUdaje podlaPozicieUdaje;
    private UdalostAdapter udalostAdapter;

    private RecyclerView zoznamUdalostiPodlaPozcie;
    private LinearLayout ziadneUdalostiPodlaPozcie;

    private SwipeRefreshLayout aktualizujZoznam;
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (obsahUdalostiPodlaPozicie.isEmpty()) {
            this.nacitavanie.setVisibility(View.VISIBLE);
            podlaPozicieUdaje.zoznamUdalostiPodlaPozicie(email, null, null, stat, okres, mesto, token);
        }
    }

    @Override
    public void dataZoServera(String odpoved, String od, ArrayList udaje) {
        switch (od) {
            case Status.PODLA_POZICIE:
                if (odpoved.equals(Status.VSETKO_V_PORIADKU)) {

                    if (udaje != null) {
                        ziadneUdalostiPodlaPozcie.setVisibility(View.GONE);
                        ziskajUdalosti(udaje);
                    } else {
                        ziadneUdalostiPodlaPozcie.setVisibility(View.VISIBLE);
                    }
                    zoznamUdalostiPodlaPozcie.setItemViewCacheSize(obsahUdalostiPodlaPozicie.size());
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
        podlaPozicieUdaje.zaujemOudalost(email, idUdalost, token);
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
    }

    private void ziskajUdalosti(ArrayList<Udalost> udalosti) {
        obsahUdalostiPodlaPozicie.addAll(udalosti);
        udalostAdapter.notifyItemRangeInserted(obsahUdalostiPodlaPozicie.size() - 1, udalosti.size());
    }

    private void init(View view) {
        this.email = getArguments().getString("email");
        this.token = getArguments().getString("token");
        this.stat = getArguments().getString("stat");
        this.okres = getArguments().getString("okres");
        this.mesto = getArguments().getString("mesto");

        this.aktualizujZoznam = view.findViewById(R.id.aktualizuj_udalosti);
        this.zoznamUdalostiPodlaPozcie = view.findViewById(R.id.zoznam_udalosti);
        this.ziadneUdalostiPodlaPozcie = view.findViewById(R.id.ziadne_udalosti);
        this.nacitavanie = view.findViewById(R.id.nacitavanie);

        this.obsahUdalostiPodlaPozicie = new ArrayList<>();
        nastavZoznamUdalosti();

        this.podlaPozicieUdaje = new PodlaPozicieUdaje(this, this, getContext());
        this.aktualizujZoznam.setOnRefreshListener(aktualizuj);

    }

    private void nastavZoznamUdalosti() {
        this.udalostAdapter = new UdalostAdapter(obsahUdalostiPodlaPozicie, this, getContext());

        PoskitovelObsahu poskitovelObsahu = new PoskitovelObsahu(getContext());
        zoznamUdalostiPodlaPozcie.setLayoutManager(poskitovelObsahu);
        zoznamUdalostiPodlaPozcie.setItemAnimator(new DefaultItemAnimator());
        zoznamUdalostiPodlaPozcie.setAdapter(udalostAdapter);
    }

    private SwipeRefreshLayout.OnRefreshListener aktualizuj = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            aktualizujZoznam.setRefreshing(false);
        }
    };
}