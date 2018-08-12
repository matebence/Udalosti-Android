package com.mate.bence.udalosti.List.Ziadost;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Siet.UdalostiAdresa;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class ZiadostAdapter extends RecyclerView.Adapter<ZiadostAdapter.PozvankyHolder> {

    private List<Ziadost> zoznamZiadosti;
    private final TlacidlaGesta tlacidlaGesta;
    private Context context;

    public ZiadostAdapter(List<Ziadost> zoznamZiadosti, TlacidlaGesta tlacidlaGesta, Context context) {
        this.zoznamZiadosti = zoznamZiadosti;
        this.tlacidlaGesta = tlacidlaGesta;
        this.context = context;
    }

    @NonNull
    @Override
    public PozvankyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View obsah = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_zoznam_ziadosti, parent, false);
        return new ZiadostAdapter.PozvankyHolder(obsah);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PozvankyHolder holder, int position) {
        Ziadost ziadost = zoznamZiadosti.get(position);

        if (!(ziadost.getObrazok().equals("default"))) {
            new ZiadostAdapter.ziskajObrazok(holder.obrazokPouzivatela, context).execute(ziadost.getObrazok());
        }

        holder.meno.setText(ziadost.getMeno());
        holder.idVztah.setText(Integer.toString(ziadost.getIdVztah()));

        nacitajObsah(holder.ziadost);
    }

    @Override
    public int getItemCount() {
        return zoznamZiadosti.size();
    }

    private void nacitajObsah(View view) {
        ScaleAnimation animacia = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animacia.setDuration(350);
        view.startAnimation(animacia);
    }

    class PozvankyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LinearLayout ziadost;
        private ImageView obrazokPouzivatela;

        private WeakReference<TlacidlaGesta> tlacidloGesta;

        private Button potvrdit;
        private Button odmietnut;
        private TextView meno;
        private TextView idVztah;

        private PozvankyHolder(View view) {
            super(view);

            this.ziadost = view.findViewById(R.id.poziadavka_riadok);
            this.obrazokPouzivatela = view.findViewById(R.id.poziadavka_obrazok);
            this.tlacidloGesta = new WeakReference<>(ZiadostAdapter.this.tlacidlaGesta);

            this.potvrdit = view.findViewById(R.id.poziadavka_potvrdit);
            this.odmietnut = view.findViewById(R.id.poziadavka_odmietnut);

            this.meno = view.findViewById(R.id.poziadavka_meno);
            this.idVztah = view.findViewById(R.id.id_vztah);

            this.potvrdit.setOnClickListener(this);
            this.odmietnut.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == potvrdit.getId()) {
                tlacidlaGesta.tlacidloPotrvdit(this.getAdapterPosition());
            } else if (v.getId() == odmietnut.getId()) {
                tlacidlaGesta.tlacidloOdmietnut(this.getAdapterPosition());
            }
        }
    }

    public void odpovedNaZiadost(int pozicia) {
        zoznamZiadosti.remove(pozicia);
        notifyItemRemoved(pozicia);
    }

    @SuppressLint("StaticFieldLeak")
    private class ziskajObrazok extends AsyncTask<String, Void, Bitmap> {
        private ImageView obrazok;
        private Context context;

        ziskajObrazok(ImageView obrazok, Context context) {
            this.obrazok = obrazok;
            this.context = context;
        }

        protected Bitmap doInBackground(String... adresa) {
            String adresaObrazka = UdalostiAdresa.getAdresa() + "udalosti" + "/" + adresa[0];
            Bitmap bitmap = null;
            try {
                InputStream zdroj = new java.net.URL(adresaObrazka).openStream();
                bitmap = BitmapFactory.decodeStream(zdroj);
            } catch (Exception e) {
                Log.e("Chyba ", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            obrazok.setImageBitmap(result);
            try {
                RoundedBitmapDrawable okruhliObrazok = RoundedBitmapDrawableFactory.create(context.getResources(), result);
                okruhliObrazok.setCircular(true);
                obrazok.setImageDrawable(okruhliObrazok);
            } catch (Exception e) {
                Log.v(TAG, "Chyba pri zaokrúhlení fotky " + e.toString());
            }
        }
    }
}