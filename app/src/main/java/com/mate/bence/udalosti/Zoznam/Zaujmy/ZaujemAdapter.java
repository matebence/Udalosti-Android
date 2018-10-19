package com.mate.bence.udalosti.Zoznam.Zaujmy;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Zoznam.Zaujmy.Struktura.Mesiac;
import com.mate.bence.udalosti.Zoznam.Zaujmy.Struktura.NaplanovanaUdalost;
import com.mate.bence.udalosti.Zoznam.Zaujmy.Struktura.Zoznam;

import java.text.DateFormatSymbols;
import java.util.List;

public class ZaujemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Zoznam> naplanovaneUdalosti;

    public ZaujemAdapter(List<Zoznam> naplanovaneUdalosti) {
        this.naplanovaneUdalosti = naplanovaneUdalosti;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int podhlad) {
        RecyclerView.ViewHolder zoznam = null;
        LayoutInflater obsah = LayoutInflater.from(parent.getContext());

        switch (podhlad) {
            case Zoznam.NAPLANOVANA_UDALOST:
                View naplaNovanaUdalost = obsah.inflate(R.layout.list_zoznam_zaujmy, parent, false);
                zoznam = new NaplanovanaUdalostHolder(naplaNovanaUdalost);
                break;

            case Zoznam.MESIAC:
                View mesiac = obsah.inflate(R.layout.list_zoznam_zaujmy_hlavicka, parent, false);
                zoznam = new MesiacHolder(mesiac);
                break;
        }

        return zoznam;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case Zoznam.NAPLANOVANA_UDALOST:
                NaplanovanaUdalost naplanovanaUdalost = (NaplanovanaUdalost) naplanovaneUdalosti.get(position);
                NaplanovanaUdalostHolder naplanovanaUdalostHolder = (NaplanovanaUdalostHolder) viewHolder;

                naplanovanaUdalostHolder.den.setText(naplanovanaUdalost.getUdalost().getDen());
                naplanovanaUdalostHolder.nazov.setText(naplanovanaUdalost.getUdalost().getNazov());
                naplanovanaUdalostHolder.mesto.setText(naplanovanaUdalost.getUdalost().getMesto());
                naplanovanaUdalostHolder.miesto.setText(naplanovanaUdalost.getUdalost().getUlica());

                nacitajObsah(naplanovanaUdalostHolder.kalendar);

                break;

            case Zoznam.MESIAC:
                Mesiac mesiac = (Mesiac) naplanovaneUdalosti.get(position);
                MesiacHolder mesiacHolder = (MesiacHolder) viewHolder;

                mesiacHolder.mesiac.setText(mesiac.getMesiac());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return naplanovaneUdalosti != null ? naplanovaneUdalosti.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return naplanovaneUdalosti.get(position).cast();
    }

    private void nacitajObsah(View view) {
        ScaleAnimation animacia = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animacia.setDuration(350);
        view.startAnimation(animacia);
    }

    private class MesiacHolder extends RecyclerView.ViewHolder {
        private TextView mesiac;

        private MesiacHolder(View view) {
            super(view);

            this.mesiac = view.findViewById(R.id.list_zoznam_hlavicka);
        }
    }

    public class NaplanovanaUdalostHolder extends RecyclerView.ViewHolder {
        public RelativeLayout kalendar;

        private TextView den, nazov, mesto, miesto;
        public LinearLayout obsah;

        private NaplanovanaUdalostHolder(View view) {
            super(view);

            this.den = view.findViewById(R.id.udalosti_kalendar_den);
            this.nazov = view.findViewById(R.id.udalosti_kalendar_nazov);
            this.miesto = view.findViewById(R.id.udalosti_kalendar_miesto);
            this.mesto = view.findViewById(R.id.udalosti_kalendar_mesto);

            this.obsah = view.findViewById(R.id.udalosti_kalendar_obsah);
            this.kalendar = view.findViewById(R.id.udalosti_kalendar_riadok);
        }
    }

    public void odstranNaplanovanuUdalost(int pozicia) {
        naplanovaneUdalosti.remove(pozicia);
        notifyItemRemoved(pozicia);
    }
}