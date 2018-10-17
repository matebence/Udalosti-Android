package com.mate.bence.udalosti.Zoznam;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mate.bence.udalosti.Nastroje.Stream;
import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Siet.UdalostiAdresa;

import java.io.InputStream;
import java.util.List;

public class UdalostAdapter extends RecyclerView.Adapter<UdalostAdapter.UdalostHolder> {

    private List<Udalost> zoznamUdalosti;
    private ZvolenaUdalost zvolenaUdalost;
    private Context context;

    public UdalostAdapter(List<Udalost> zoznamUdalosti, Context context) {
        this.context = context;
        this.zoznamUdalosti = zoznamUdalosti;
    }

    @NonNull
    @Override
    public UdalostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View obsah = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_zoznam_udalosti, parent, false);
        return new UdalostHolder(obsah);
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull UdalostHolder holder, int position) {
        Udalost udalost = zoznamUdalosti.get(position);

        new Stream(holder.obrazokUdalosti, holder.nacitavenie, context).execute(udalost.getObrazok());

        holder.nazovUdalosti.setText(udalost.getNazov());
        holder.idUdalosti.setText(Integer.toString(udalost.getIdUdalost()));
        holder.zaujemUdalost.setText(Integer.toString(udalost.getZaujem()));
        holder.denUdalosti.setText(udalost.getDen()+".");
        holder.mesiacUdalosti.setText(udalost.getMesiac().substring(0, 3) + ".");
        holder.casUdalosti.setText(udalost.getCas());
        holder.mestoUdalosti.setText(udalost.getMesto()+",");
        holder.miestoUdalosti.setText(udalost.getUlica());

        nacitajObsah(holder.udalost);
    }

    @Override
    public int getItemCount() {
        return zoznamUdalosti.size();
    }

    private void nacitajObsah(View view) {
        ScaleAnimation animacia = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animacia.setDuration(350);
        view.startAnimation(animacia);
    }

    public void zvolenaUdalost(ZvolenaUdalost zvolenaUdalost) {
        this.zvolenaUdalost = zvolenaUdalost;
    }

    class UdalostHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RelativeLayout udalost;
        private ImageView obrazokUdalosti;
        private ProgressBar nacitavenie;

        private TextView idUdalosti;
        private TextView zaujemUdalost;
        private TextView nazovUdalosti;
        private TextView denUdalosti;
        private TextView mesiacUdalosti;
        private TextView mestoUdalosti;
        private TextView miestoUdalosti;
        private TextView casUdalosti;

        private UdalostHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            this.udalost = view.findViewById(R.id.udalosti_riadok);
            this.obrazokUdalosti = view.findViewById(R.id.udalosti_obrazok);
            this.nacitavenie = view.findViewById(R.id.nacitavenieObrazka);

            this.idUdalosti = view.findViewById(R.id.id_udalost);
            this.zaujemUdalost = view.findViewById(R.id.zaujem_o_udalosti);
            this.nazovUdalosti = view.findViewById(R.id.udalosti_nazov);
            this.denUdalosti = view.findViewById(R.id.udalosti_den);
            this.mesiacUdalosti = view.findViewById(R.id.udalosti_mesiac);
            this.mestoUdalosti = view.findViewById(R.id.udalosti_mesto);
            this.miestoUdalosti = view.findViewById(R.id.udalosti_miesto);
            this.casUdalosti = view.findViewById(R.id.udalosti_cas);
        }

        @Override
        public void onClick(View view) {
            if (zvolenaUdalost != null) {
                zvolenaUdalost.podrobnostiUdalosti(view, getAdapterPosition());
            }
        }
    }
}