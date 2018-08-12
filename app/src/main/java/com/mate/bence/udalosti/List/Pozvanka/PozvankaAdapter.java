package com.mate.bence.udalosti.List.Pozvanka;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Siet.UdalostiAdresa;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class PozvankaAdapter extends RecyclerView.Adapter<PozvankaAdapter.PozvankaHolder> {

    private List<Pozvanka> zoznamPriatelov;
    private Context context;

    private final TlacidlaGesta tlacidlaGesta;

    public PozvankaAdapter(List<Pozvanka> zoznamPriatelov, Context context, TlacidlaGesta tlacidlaGesta) {
        this.zoznamPriatelov = zoznamPriatelov;
        this.context = context;
        this.tlacidlaGesta = tlacidlaGesta;
    }

    @NonNull
    @Override
    public PozvankaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View obsah = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_zoznam_pozvanok, parent, false);
        return new PozvankaAdapter.PozvankaHolder(obsah);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PozvankaHolder holder, int position) {
        Pozvanka pozvanka = zoznamPriatelov.get(position);

        if (!(pozvanka.getObrazok().equals("default"))) {
            new ziskajObrazok(holder.obrazokPouzivatela, context).execute(pozvanka.getObrazok());
        }

        holder.meno.setText(pozvanka.getMeno());
        holder.idPouzivatel.setText(Integer.toString(pozvanka.getIdPouzivatel()));

        nacitajObsah(holder.priatel);
    }

    @Override
    public int getItemCount() {
        return zoznamPriatelov.size();
    }

    private void nacitajObsah(View view) {
        ScaleAnimation animacia = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animacia.setDuration(350);
        view.startAnimation(animacia);
    }


    class PozvankaHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RelativeLayout priatel;
        private ImageView obrazokPouzivatela;

        private WeakReference<TlacidlaGesta> tlacidloPozvat;

        private Button pozvat;
        private TextView meno;
        private TextView idPouzivatel;

        private PozvankaHolder(View view) {
            super(view);

            this.priatel = view.findViewById(R.id.pozvanka_riadok);
            this.obrazokPouzivatela = view.findViewById(R.id.pozvanka_obrazok);

            this.tlacidloPozvat = new WeakReference<>(PozvankaAdapter.this.tlacidlaGesta);

            this.pozvat = view.findViewById(R.id.pozvat);
            this.meno = view.findViewById(R.id.pozvanka_meno);
            this.idPouzivatel = view.findViewById(R.id.id_pouzivatel);

            this.pozvat.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == pozvat.getId()) {
                tlacidloPozvat.get().pozvat(Integer.parseInt(idPouzivatel.getText().toString()));
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