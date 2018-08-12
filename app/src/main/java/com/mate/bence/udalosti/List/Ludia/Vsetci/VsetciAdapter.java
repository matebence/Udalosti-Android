package com.mate.bence.udalosti.List.Ludia.Vsetci;

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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mate.bence.udalosti.List.Ludia.Ludia;
import com.mate.bence.udalosti.List.Ludia.TlacidlaGesta;
import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Siet.UdalostiAdresa;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class VsetciAdapter extends RecyclerView.Adapter<VsetciAdapter.VsetciHolder> implements Filterable {

    private boolean animacie = true;

    private List<Ludia> zoznamPouzivatelov;
    private List<Ludia> hladanyPouzivatel;

    private final TlacidlaGesta tlacidlaGesta;
    private Context context;

    public VsetciAdapter(List<Ludia> zoznamPouzivatelov, TlacidlaGesta tlacidlaGesta, Context context) {
        this.tlacidlaGesta = tlacidlaGesta;
        this.context = context;
        this.zoznamPouzivatelov = zoznamPouzivatelov;
        this.hladanyPouzivatel = zoznamPouzivatelov;
    }

    @NonNull
    @Override
    public VsetciHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View obsah = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_zoznam_ludia, parent, false);
        return new VsetciHolder(obsah);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull VsetciHolder holder, int position) {
        Ludia ludia = hladanyPouzivatel.get(position);

        if (!(ludia.getObrazok().equals("default"))) {
            new VsetciAdapter.ziskajObrazok(holder.obrazokPouzivatela, context).execute(ludia.getObrazok());
        }
        holder.operacia.setVisibility(View.VISIBLE);

        if (position == (getItemCount() - 2)) {
            tlacidlaGesta.nacitajDalsychPouzivatelov();
        }

        holder.meno.setText(ludia.getMeno());
        holder.idPouzivatel.setText(Integer.toString(ludia.getIdPouzivatel()));

        if (animacie) {
            nacitajObsah(holder.pouzivatel);
        }
    }

    @Override
    public int getItemCount() {
        return hladanyPouzivatel.size();
    }

    public void oznacitAkoPriatel(int position) {
        hladanyPouzivatel.remove(position);
        notifyItemRemoved(position);
    }

    private void nacitajObsah(View view) {
        ScaleAnimation animacia = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animacia.setDuration(350);
        view.startAnimation(animacia);
    }

    @Override
    public Filter getFilter() {
        animacie = false;
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence hladanyPouzivatel) {
                String slovo = hladanyPouzivatel.toString();
                if (slovo.isEmpty()) {
                    VsetciAdapter.this.hladanyPouzivatel = zoznamPouzivatelov;
                } else {
                    List<Ludia> udaje = new ArrayList<>();
                    for (Ludia pouzivatel : zoznamPouzivatelov) {
                        if (pouzivatel.getMeno().toLowerCase().contains(slovo.toLowerCase())) {
                            udaje.add(pouzivatel);
                        }
                    }
                    VsetciAdapter.this.hladanyPouzivatel = udaje;
                }

                FilterResults vysledok = new FilterResults();
                vysledok.values = VsetciAdapter.this.hladanyPouzivatel;
                return vysledok;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                hladanyPouzivatel = (ArrayList<Ludia>) filterResults.values;
                if (hladanyPouzivatel.size() < zoznamPouzivatelov.size()) {
                    animacie = true;
                }
                notifyDataSetChanged();
            }
        };
    }

    public class VsetciHolder extends RecyclerView.ViewHolder {

        private RelativeLayout pouzivatel;
        private ImageView obrazokPouzivatela;

        private TextView meno;
        private TextView idPouzivatel;

        public LinearLayout obsah;
        RelativeLayout operacia;

        private VsetciHolder(View view) {
            super(view);

            this.pouzivatel = view.findViewById(R.id.ludia_vsetci_riadok);
            this.obrazokPouzivatela = view.findViewById(R.id.ludia_vsetci_obrazok);

            this.meno = view.findViewById(R.id.ludia_vsetci_meno);
            this.idPouzivatel = view.findViewById(R.id.id_pouzivatel);

            this.obsah = view.findViewById(R.id.ludia_vsetci_obsah);
            this.operacia = view.findViewById(R.id.ludia_novy_priatel_na_lavo);
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