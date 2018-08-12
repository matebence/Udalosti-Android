package com.mate.bence.udalosti.List.Ludia.Priatelia;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mate.bence.udalosti.List.Ludia.Priatelia.Struktura.Meno;
import com.mate.bence.udalosti.List.Ludia.Priatelia.Struktura.Pouzivatel;
import com.mate.bence.udalosti.List.Ludia.Priatelia.Struktura.Zoznam;
import com.mate.bence.udalosti.List.Ludia.TlacidlaGesta;
import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Siet.UdalostiAdresa;

import java.io.InputStream;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class PriateliaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Zoznam> zoznamPriatelov;

    private final TlacidlaGesta tlacidlaGesta;
    private Context context;

    public PriateliaAdapter(List<Zoznam> zoznamPriatelov, TlacidlaGesta tlacidlaGesta, Context context) {
        this.tlacidlaGesta = tlacidlaGesta;
        this.context = context;
        this.zoznamPriatelov = zoznamPriatelov;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int pohlad) {
        RecyclerView.ViewHolder zoznam = null;
        LayoutInflater obsah = LayoutInflater.from(parent.getContext());

        switch (pohlad) {
            case Zoznam.POUZIVATEL:
                View pouzivatel = obsah.inflate(R.layout.list_zoznam_ludia, parent, false);
                zoznam = new PouzivatelHolder(pouzivatel);
                break;

            case Zoznam.MENO:
                View meno = obsah.inflate(R.layout.list_zoznam_hlavicka, parent, false);
                zoznam = new MenoHolder(meno);
                break;
        }

        return zoznam;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case Zoznam.POUZIVATEL:
                Pouzivatel pouzivatel = (Pouzivatel) zoznamPriatelov.get(position);
                PouzivatelHolder pouzivatelHolder = (PouzivatelHolder) viewHolder;

                pouzivatelHolder.meno.setText(pouzivatel.getUdaje().getMeno());
                pouzivatelHolder.idPouzivatel.setText(Integer.toString(pouzivatel.getUdaje().getIdPouzivatel()));

                nacitajObsah(pouzivatelHolder.pouzivatel);

                if (!(pouzivatel.getUdaje().getObrazok().equals("default"))) {
                    new PriateliaAdapter.ziskajObrazok(pouzivatelHolder.obrazokPouzivatela, context).execute(pouzivatel.getUdaje().getObrazok());
                }
                pouzivatelHolder.operacia.setVisibility(View.VISIBLE);

                break;

            case Zoznam.MENO:
                Meno meno = (Meno) zoznamPriatelov.get(position);
                MenoHolder menoHolder = (MenoHolder) viewHolder;

                menoHolder.meno.setText(meno.getMeno().substring(0, 1).toUpperCase());
                break;
        }

        if (position == (getItemCount() - 2)) {
            tlacidlaGesta.nacitajDalsychPouzivatelov();
        }
    }

    @Override
    public int getItemCount() {
        return zoznamPriatelov != null ? zoznamPriatelov.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return zoznamPriatelov.get(position).cast();
    }

    private void nacitajObsah(View view) {
        ScaleAnimation animacia = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animacia.setDuration(350);
        view.startAnimation(animacia);
    }

    private class MenoHolder extends RecyclerView.ViewHolder {
        private TextView meno;

        private MenoHolder(View view) {
            super(view);
            this.meno = view.findViewById(R.id.list_zoznam_hlavicka);

        }
    }

    public class PouzivatelHolder extends RecyclerView.ViewHolder {
        private RelativeLayout pouzivatel;
        private ImageView obrazokPouzivatela;

        private TextView meno;
        private TextView idPouzivatel;

        public LinearLayout obsah;
        RelativeLayout operacia;

        private PouzivatelHolder(View view) {
            super(view);

            this.pouzivatel = view.findViewById(R.id.ludia_vsetci_riadok);
            this.obrazokPouzivatela = view.findViewById(R.id.ludia_vsetci_obrazok);

            this.meno = view.findViewById(R.id.ludia_vsetci_meno);
            this.idPouzivatel = view.findViewById(R.id.id_pouzivatel);

            this.obsah = view.findViewById(R.id.ludia_vsetci_obsah);
            this.operacia = view.findViewById(R.id.ludia_novy_priatel_na_pravo);
        }
    }

    public void odstranPouzivatela(int pozicia) {
        zoznamPriatelov.remove(pozicia);
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