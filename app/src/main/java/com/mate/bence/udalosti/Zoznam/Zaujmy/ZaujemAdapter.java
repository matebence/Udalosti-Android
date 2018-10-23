package com.mate.bence.udalosti.Zoznam.Zaujmy;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Zoznam.Udalosti.ZvolenaUdalost;
import com.mate.bence.udalosti.Zoznam.Zaujmy.Struktura.Mesiac;
import com.mate.bence.udalosti.Zoznam.Zaujmy.Struktura.MesiacZaujmov;
import com.mate.bence.udalosti.Zoznam.Zaujmy.Struktura.Zaujem;

import java.util.List;

public class ZaujemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = ZaujemAdapter.class.getName();

    private List<Zaujem> zoznamZaujmov;
    private ZvolenaUdalost zvolenaUdalost;

    public ZaujemAdapter(List<Zaujem> zoznamZaujmov) {
        Log.v(ZaujemAdapter.TAG, "Metoda ZaujemAdapter bola vykonana");

        this.zoznamZaujmov = zoznamZaujmov;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int struktura) {
        RecyclerView.ViewHolder zaujmy = null;
        LayoutInflater zaujem = LayoutInflater.from(parent.getContext());

        switch (struktura) {
            case Zaujem.MESIAC_ZAUJMOV:
                View mesiacZaujmov = zaujem.inflate(R.layout.list_zoznam_zaujmy, parent, false);
                zaujmy = new MesiacZaujmovHolder(mesiacZaujmov);
                break;

            case Zaujem.MESIAC:
                View mesiac = zaujem.inflate(R.layout.list_zoznam_zaujmy_hlavicka, parent, false);
                zaujmy = new MesiacHolder(mesiac);
                break;
        }

        return zaujmy;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case Zaujem.MESIAC_ZAUJMOV:
                MesiacZaujmov mesiacZaujmov = (MesiacZaujmov) zoznamZaujmov.get(position);
                MesiacZaujmovHolder mesiacZaujmovHolder = (MesiacZaujmovHolder) viewHolder;

                mesiacZaujmovHolder.den.setText(mesiacZaujmov.getUdalost().getDen() + ".");
                mesiacZaujmovHolder.nazov.setText(mesiacZaujmov.getUdalost().getNazov());
                mesiacZaujmovHolder.mesto.setText(mesiacZaujmov.getUdalost().getMesto() + ", ");
                mesiacZaujmovHolder.ulica.setText(mesiacZaujmov.getUdalost().getUlica());

                nacitajObsah(mesiacZaujmovHolder.odstranitZaujem);
                break;

            case Zaujem.MESIAC:
                Mesiac mesiac = (Mesiac) zoznamZaujmov.get(position);
                MesiacHolder mesiacHolder = (MesiacHolder) viewHolder;

                mesiacHolder.mesiac.setText(mesiac.getMesiac());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return this.zoznamZaujmov != null ? this.zoznamZaujmov.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return this.zoznamZaujmov.get(position).struktura();
    }

    private void nacitajObsah(View view) {
        Log.v(ZaujemAdapter.TAG, "Metoda nacitajObsah bola vykonana");

        ScaleAnimation animacia = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animacia.setDuration(350);
        view.startAnimation(animacia);
    }

    public void zvolenaUdalost(ZvolenaUdalost zvolenaUdalost) {
        Log.v(ZaujemAdapter.TAG, "Metoda zvolenaUdalost bola vykonana");

        this.zvolenaUdalost = zvolenaUdalost;
    }

    public void odstranZaujem(int pozicia) {
        Log.v(ZaujemAdapter.TAG, "Metoda odstranZaujem bola vykonana");

        this.zoznamZaujmov.remove(pozicia);
        notifyItemRemoved(pozicia);
    }

    private class MesiacHolder extends RecyclerView.ViewHolder {

        private TextView mesiac;

        private MesiacHolder(View view) {
            super(view);

            this.mesiac = view.findViewById(R.id.zaujmy_hlavicka);
        }
    }

    public class MesiacZaujmovHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout odstranitZaujem;
        private TextView nazov, den, mesto, ulica;
        public LinearLayout zaujem;

        private MesiacZaujmovHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            this.den = view.findViewById(R.id.zaujmy_den);
            this.nazov = view.findViewById(R.id.zaujmy_nazov);
            this.mesto = view.findViewById(R.id.zaujmy_mesto);
            this.ulica = view.findViewById(R.id.zaujmy_ulica);

            this.zaujem = view.findViewById(R.id.zaujmy);
            this.odstranitZaujem = view.findViewById(R.id.udalosti_odstranit_zaujmy);
        }

        @Override
        public void onClick(View view) {
            if (zvolenaUdalost != null) {
                MesiacZaujmov mesiacZaujmov = (MesiacZaujmov) zoznamZaujmov.get(getAdapterPosition());
                zvolenaUdalost.podrobnostiUdalosti(getAdapterPosition(), mesiacZaujmov.getUdalost());
            }
        }
    }
}