package com.mate.bence.udalosti.Activity.Udalosti.Karty;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mate.bence.udalosti.Activity.Udalosti.Podrobnosti.Aktualizator;
import com.mate.bence.udalosti.Activity.Udalosti.Podrobnosti.AktualizatorObsahu;
import com.mate.bence.udalosti.Activity.Udalosti.Podrobnosti.Podrobnosti;
import com.mate.bence.udalosti.Activity.Udalosti.UdalostiUdaje;
import com.mate.bence.udalosti.Dialog.DialogOdpoved;
import com.mate.bence.udalosti.Dialog.DialogOznameni;
import com.mate.bence.udalosti.Dialog.DialogPotvrdeni;
import com.mate.bence.udalosti.Nastroje.Pripojenie;
import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Nastavenia.Nastavenia;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaData;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;
import com.mate.bence.udalosti.Zoznam.PoskitovelObsahu;
import com.mate.bence.udalosti.Zoznam.Udalost;
import com.mate.bence.udalosti.Zoznam.Udalosti.ZvolenaUdalost;
import com.mate.bence.udalosti.Zoznam.Zaujmy.OdstranenieZaujmu;
import com.mate.bence.udalosti.Zoznam.Zaujmy.ZaujemAdapterGesto;
import com.mate.bence.udalosti.Zoznam.Zaujmy.Struktura.Mesiac;
import com.mate.bence.udalosti.Zoznam.Zaujmy.Struktura.MesiacZaujmov;
import com.mate.bence.udalosti.Zoznam.Zaujmy.Struktura.Zaujem;
import com.mate.bence.udalosti.Zoznam.Zaujmy.ZaujemAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class Zaujmy extends Fragment implements KommunikaciaData, KommunikaciaOdpoved, OdstranenieZaujmu, ZvolenaUdalost, Aktualizator {

    private static final String TAG = Zaujmy.class.getName();

    private String email, token;
    private List<Udalost> obsahZaujmov;
    private List<Zaujem> mesiaceZaujmov;

    private UdalostiUdaje udalostiUdaje;
    private ZaujemAdapter zaujemAdapter;

    private RecyclerView zoznamZaujmov;
    private LinearLayout chybaZaujmov, spracovanieZaujmu;
    private ProgressBar nacitavanie;
    private ImageView chybaZaujmovObrazok;
    private TextView chybaZaujmovText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_udalosti_zaujmy, container, false);
        return init(view);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (this.obsahZaujmov.isEmpty()) {
            this.nacitavanie.setVisibility(View.VISIBLE);
            this.udalostiUdaje.zoznamZaujmov(this.email, this.token);
        }
    }

    @Override
    public void dataZoServera(String odpoved, String od, ArrayList udaje) {
        Log.v(Zaujmy.TAG, "Metoda dataZoServera - Zaujmy bola vykonana");

        switch (od) {
            case Nastavenia.ZAUJEM_ZOZNAM:
                if (odpoved.equals(Nastavenia.VSETKO_V_PORIADKU)) {
                    this.chybaZaujmov.setVisibility(View.GONE);

                    this.chybaZaujmovObrazok.setBackgroundResource(R.drawable.ic_udalosti);
                    this.chybaZaujmovText.setText(getResources().getString(R.string.zaujmy_ziadne_zaujmy));

                    if (udaje != null) {
                        this.chybaZaujmov.setVisibility(View.GONE);
                        ziskajZaujmy(udaje);
                    } else {
                        this.chybaZaujmov.setVisibility(View.VISIBLE);
                    }
                    this.zoznamZaujmov.setItemViewCacheSize(obsahZaujmov.size());
                } else {
                    this.chybaZaujmov.setVisibility(View.VISIBLE);

                    this.chybaZaujmovObrazok.setBackgroundResource(R.drawable.ic_spojenie);
                    this.chybaZaujmovText.setText(getResources().getString(R.string.chyba_spojenie_zlyhalo));
                }
                break;
        }

        this.spracovanieZaujmu.setVisibility(View.GONE);
        this.nacitavanie.setVisibility(View.GONE);
    }

    @Override
    public void odpovedServera(String odpoved, String od, HashMap<String, String> udaje) {
        Log.v(Zaujmy.TAG, "Metoda odpovedServera - Zaujmy bola vykonana");

        switch (od) {
            case Nastavenia.ZAUJEM_ODSTRANENIE:
                if (odpoved.equals(Nastavenia.VSETKO_V_PORIADKU)) {

                    if (udaje.get("uspech") == null) {
                        Toast.makeText(getContext(), udaje.get("chyba"), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    new DialogOznameni(getActivity(), "Chyba", odpoved);
                }
                break;
        }

        this.spracovanieZaujmu.setVisibility(View.GONE);
    }

    @Override
    public void podrobnostiUdalosti(int pozicia, Udalost udalost) {
        Log.v(Zaujmy.TAG, "Metoda podrobnostiUdalosti - Zaujmy bola vykonana");

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
    public void podrobnostiUdalosti(View view, int pozicia) {
    }

    @Override
    public void odstranit(final RecyclerView.ViewHolder viewHolder, int smer, final int pozicia) {
        Log.v(Zaujmy.TAG, "Metoda odstranit - Zaujmy bola vykonana");

        if (viewHolder instanceof ZaujemAdapter.MesiacZaujmovHolder) {
            final MesiacZaujmov mesiacZaujmov = (MesiacZaujmov) this.mesiaceZaujmov.get(pozicia);
            this.zaujemAdapter.odstranZaujem(viewHolder.getAdapterPosition());

            if (Pripojenie.pripojenieExistuje(getContext())) {
                new DialogPotvrdeni(getActivity(), getResources().getString(R.string.zaujmy_odstranenie_tiltul), getResources().getString(R.string.zaujmy_odstranenie_text) + " " + mesiacZaujmov.getUdalost().getNazov() + "?", getResources().getString(R.string.dialog_potvrdeni_odstranit_zaujem_a), getResources().getString(R.string.dialog_potvrdeni_odstranit_zaujem_b), new DialogOdpoved() {
                    @Override
                    public void tlacidloA() {
                        spracovanieZaujmu.setVisibility(View.VISIBLE);
                        udalostiUdaje.odstranZaujem(email, token, mesiacZaujmov.getUdalost().getIdUdalost());
                    }

                    @Override
                    public void tlacidloB() {
                        spracovanieZaujmu.setVisibility(View.VISIBLE);
                        AktualizatorObsahu.zaujmy().hodnota();
                    }
                }).show();
            } else {
                new DialogOznameni(getActivity(), "Chyba", getResources().getString(R.string.chyba_servera));
            }
        }
    }

    @Override
    public void aktualizujObsahZaujmov() {
        Log.v(Zaujmy.TAG, "Metoda aktualizujObsahZaujmov - Zaujmy bola vykonana");

        this.mesiaceZaujmov.clear();
        this.obsahZaujmov.clear();

        this.zaujemAdapter.notifyItemRangeRemoved(0, this.obsahZaujmov.size());
        this.udalostiUdaje.zoznamZaujmov(this.email, this.token);
    }

    private View init(View view) {
        Log.v(Zaujmy.TAG, "Metoda init - Zaujmy bola vykonana");

        AktualizatorObsahu.zaujmy().nastav(this);

        this.email = getArguments().getString("email");
        this.token = getArguments().getString("token");

        this.spracovanieZaujmu = view.findViewById(R.id.spracovanie_zaujmu);

        this.zoznamZaujmov = view.findViewById(R.id.zoznam_zaujmov);
        this.chybaZaujmov = view.findViewById(R.id.chyba_zaujmov);
        this.nacitavanie = view.findViewById(R.id.nacitavanie);
        this.chybaZaujmovObrazok = view.findViewById(R.id.chyba_zaujmov_obrazok);
        this.chybaZaujmovText = view.findViewById(R.id.chyba_zaujmov_text);

        this.obsahZaujmov = new ArrayList<>();
        this.mesiaceZaujmov = new ArrayList<>();
        nastavZoznamZaujmov();

        this.udalostiUdaje = new UdalostiUdaje(this, this, getContext());
        return view;
    }

    protected void ziskajZaujmy(ArrayList<Udalost> zaujmy) {
        Log.v(Zaujmy.TAG, "Metoda ziskajZaujmy bola vykonana");

        this.obsahZaujmov.addAll(zaujmy);

        for (String skupina : zoSkupinovanieUdajov(zaujmy).keySet()) {
            Mesiac mesiac = new Mesiac();
            mesiac.setMesiac(skupina);
            this.mesiaceZaujmov.add(mesiac);

            for (Udalost udalost : zoSkupinovanieUdajov(zaujmy).get(skupina)) {
                MesiacZaujmov mesiacZaujmov = new MesiacZaujmov();
                mesiacZaujmov.setUdalost(udalost);
                this.mesiaceZaujmov.add(mesiacZaujmov);
            }
        }

        this.zaujemAdapter.notifyItemRangeInserted(0, mesiaceZaujmov.size());
    }

    private void nastavZoznamZaujmov() {
        Log.v(Zaujmy.TAG, "Metoda nastavZoznamZaujmov bola vykonana");

        PoskitovelObsahu poskitovelObsahu = new PoskitovelObsahu(getContext());

        this.zaujemAdapter = new ZaujemAdapter(mesiaceZaujmov);
        this.zaujemAdapter.zvolenaUdalost(this);

        this.zoznamZaujmov.setLayoutManager(poskitovelObsahu);
        this.zoznamZaujmov.setItemAnimator(new DefaultItemAnimator());
        this.zoznamZaujmov.setAdapter(this.zaujemAdapter);

        ItemTouchHelper.SimpleCallback zaujemAdapterGesto = new ZaujemAdapterGesto(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(zaujemAdapterGesto).attachToRecyclerView(zoznamZaujmov);
    }

    private TreeMap<String, List<Udalost>> zoSkupinovanieUdajov(List<Udalost> udaje) {
        Log.v(Zaujmy.TAG, "Metoda zoSkupinovanieUdajov bola vykonana");

        TreeMap<String, List<Udalost>> zaujem = new TreeMap<>(
                new Comparator<String>() {
                    public int compare(String a, String b) {
                        return b.toLowerCase().compareTo(a.toLowerCase());
                    }
                }
        );

        for (Udalost udalost : udaje) {
            String skupina = udalost.getMesiac();
            if (zaujem.containsKey(skupina)) {
                zaujem.get(skupina).add(udalost);
            } else {
                List<Udalost> zaujmy = new ArrayList<>();
                zaujmy.add(udalost);
                zaujem.put(skupina, zaujmy);
            }
        }
        return zaujem;
    }
}