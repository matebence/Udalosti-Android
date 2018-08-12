package com.mate.bence.udalosti.Dialog.Zoznam;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mate.bence.udalosti.Activity.Navigacia.Fragment.Ludia.LudiaUdaje;
import com.mate.bence.udalosti.List.PoskitovelObsahu;
import com.mate.bence.udalosti.List.Pozvanka.Pozvanka;
import com.mate.bence.udalosti.List.Pozvanka.PozvankaAdapter;
import com.mate.bence.udalosti.List.Pozvanka.TlacidlaGesta;
import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Nastavenia.Pocet;
import com.mate.bence.udalosti.Udaje.Nastavenia.Status;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaData;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DialogZoznam extends DialogFragment implements TlacidlaGesta, KommunikaciaData, KommunikaciaOdpoved {

    private PozvankaAdapter pozvankaAdapter;

    private RecyclerView zoznamPriatelov;
    private TextView prazdnyZoznam;
    private ProgressBar nacitavanie;

    private DialogZoznamUdaje dialogUdaje;
    private LudiaUdaje ludiaUdaje;

    private String email, token;
    private int idUdalost;
    private List<Pozvanka> obsahPriatelov = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_pozvanok, container, false);
        init(view);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Pocet.DIALOG_ZOZNAM_UKONCIL = Pocet.DIALOG_ZOZNAM_POCET;
    }

    @Override
    public void onResume() {
        super.onResume();

        Pocet.DIALOG_ZOZNAM_OD = 0;
        if (Pocet.DIALOG_ZOZNAM_UKONCIL != 0) {
            Pocet.DIALOG_ZOZNAM_OD = Pocet.DIALOG_ZOZNAM_UKONCIL;
            Pocet.DIALOG_ZOZNAM_OD -= 5;
        }

        if (obsahPriatelov.isEmpty()) {
            this.nacitavanie.setVisibility(View.VISIBLE);
            this.ludiaUdaje.zoznamPriatelov(email, Pocet.DIALOG_ZOZNAM_OD, Pocet.DIALOG_ZOZNAM_POCET, token);
        }
    }

    @Override
    public void dataZoServera(String odpoved, String od, ArrayList udaje) {
        switch (od) {
            case Status.LUDIA_PRIATELIA:
                if (odpoved.equals(Status.VSETKO_V_PORIADKU)) {
                    if (udaje != null) {
                        obsahPriatelov.addAll(udaje);
                        pozvankaAdapter.notifyDataSetChanged();
                    } else {
                        if (!(Pocet.DIALOG_ZOZNAM_OD > 0)) {
                            this.prazdnyZoznam.setVisibility(View.VISIBLE);
                        }
                    }
                    zoznamPriatelov.setItemViewCacheSize(obsahPriatelov.size());
                }
                break;
        }
        this.nacitavanie.setVisibility(View.GONE);
    }

    @Override
    public void odpovedServera(String odpoved, String od, HashMap<String, String> udaje) {
        switch (od) {
            case Status.UDALOSTI_OZNAMENIA:
                if (odpoved.equals(Status.VSETKO_V_PORIADKU)) {
                    if (udaje != null) {
                        if (udaje.get("uspech") != null) {
                            Toast.makeText(getContext(), udaje.get("uspech"), Toast.LENGTH_LONG).show();
                        } else if (udaje.get("chyba") != null) {
                            Toast.makeText(getContext(), udaje.get("chyba"), Toast.LENGTH_LONG).show();
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void nacitajDalsychPriatelov() {
        Pocet.DIALOG_ZOZNAM_OD += 5;
        ludiaUdaje.zoznamPriatelov(email, Pocet.DIALOG_ZOZNAM_OD, Pocet.DIALOG_ZOZNAM_POCET, token);
    }

    @Override
    public void pozvat(int idPouzivatela) {
        dialogUdaje.novaPozvanka(email, idPouzivatela, idUdalost, token, UUID.randomUUID().toString());
    }

    private void init(View view) {
        this.zoznamPriatelov = view.findViewById(R.id.zoznam_priatelov_pre_pozvanku);

        this.nacitavanie = view.findViewById(R.id.nacitavanie);
        this.prazdnyZoznam = view.findViewById(R.id.dialog_pozvanok_priatelia);

        this.email = getArguments().getString("email");
        this.token = getArguments().getString("token");
        this.idUdalost = getArguments().getInt("idUdalost");

        this.ludiaUdaje = new LudiaUdaje(this, this, getContext());
        this.dialogUdaje = new DialogZoznamUdaje(this, getContext());

        this.pozvankaAdapter = new PozvankaAdapter(this.obsahPriatelov, getContext(), this);

        zoznamPriatelov.setLayoutManager(new PoskitovelObsahu(getContext()));
        zoznamPriatelov.setAdapter(pozvankaAdapter);
    }
}