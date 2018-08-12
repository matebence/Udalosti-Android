package com.mate.bence.udalosti.List.Oznamenie;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Siet.UdalostiAdresa;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class OznamenieAdapter extends RecyclerView.Adapter<OznamenieAdapter.ZiadostHolder> {

    private List<Oznamenie> zoznamOznameni;
    private final TlacidloGesta tlacidloGesta;
    private Context context;

    public OznamenieAdapter(List<Oznamenie> zoznamOznameni, TlacidloGesta tlacidloGesta, Context context) {
        this.zoznamOznameni = zoznamOznameni;
        this.tlacidloGesta = tlacidloGesta;
        this.context = context;
    }

    @NonNull
    @Override
    public OznamenieAdapter.ZiadostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View obsah = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_zoznam_oznameni, parent, false);
        return new OznamenieAdapter.ZiadostHolder(obsah);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OznamenieAdapter.ZiadostHolder holder, int position) {
        Oznamenie oznamenie = zoznamOznameni.get(position);

        if (!(oznamenie.getObrazok().equals("default"))) {
            new OznamenieAdapter.ziskajObrazok(holder.obrazokPouzivatela, context).execute(oznamenie.getObrazok());
        }

        if (oznamenie.getPrecitana() == 0) {
            holder.oznamenia.setBackgroundResource(R.color.udalosti_neprecitana_sprava);
        }

        holder.meno.setText(oznamenie.getMeno());
        holder.oznamenie.setText("Pozval Vás na udalosť " + oznamenie.getNazov() + " - " + oznamenie.getMesto());
        holder.idUdalost.setText(Integer.toString(oznamenie.getIdUdalost()));

        nacitajObsah(holder.oznamenia);
    }

    @Override
    public int getItemCount() {
        return zoznamOznameni.size();
    }

    private void nacitajObsah(View view) {
        ScaleAnimation animacia = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animacia.setDuration(350);
        view.startAnimation(animacia);
    }

    class ZiadostHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RelativeLayout oznamenia;
        private ImageView obrazokPouzivatela;

        private WeakReference<TlacidloGesta> tlacidloGesta;

        private TextView meno;
        private TextView oznamenie;
        private TextView idUdalost;

        private ZiadostHolder(View view) {
            super(view);
            this.oznamenia = view.findViewById(R.id.oznamenie_riadok);
            this.obrazokPouzivatela = view.findViewById(R.id.oznamenie_obrazok);

            this.tlacidloGesta = new WeakReference<>(OznamenieAdapter.this.tlacidloGesta);

            this.meno = view.findViewById(R.id.oznamenie_meno);
            this.oznamenie = view.findViewById(R.id.oznamenie_pozvanka);
            this.idUdalost = view.findViewById(R.id.id_udalost);

            this.oznamenia.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == oznamenia.getId()) {
                tlacidloGesta.get().ukazUdalost(Integer.parseInt(idUdalost.getText().toString()));
            }
        }
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