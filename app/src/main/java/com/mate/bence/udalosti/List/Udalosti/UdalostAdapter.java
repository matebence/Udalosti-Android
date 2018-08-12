package com.mate.bence.udalosti.List.Udalosti;

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
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Siet.UdalostiAdresa;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class UdalostAdapter extends RecyclerView.Adapter<UdalostAdapter.UdalostHolder> implements Filterable {

    private boolean animacie = true;

    private List<Udalost> zoznamUdalosti;
    private List<Udalost> hladanaUdalost;

    private final TlacidlaGesta tlacidlaGesta;
    private Context context;


    public UdalostAdapter(List<Udalost> zoznamUdalosti, TlacidlaGesta tlacidlaGesta, Context context) {
        this.tlacidlaGesta = tlacidlaGesta;
        this.context = context;
        this.zoznamUdalosti = zoznamUdalosti;
        this.hladanaUdalost = zoznamUdalosti;
    }

    @NonNull
    @Override
    public UdalostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View obsah = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_zoznam_udalosti, parent, false);
        return new UdalostHolder(obsah);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull UdalostHolder holder, int position) {
        Udalost udalost = hladanaUdalost.get(position);

        new ziskajObrazok(holder.obrazokUdalosti, context).execute(udalost.getObrazok());

        if (udalost.getIdZaujem() != null) {
            holder.budemTam.setBackground(context.getResources().getDrawable(R.drawable.ic_tlacidlo_udalosti_aktivna));
            holder.budemTam.setText(context.getString(R.string.list_zoznam_udalosti_tlacidlo_budem_tam));
        }

        if (position == (getItemCount() - 2)) {
            tlacidlaGesta.nacitajDalsieUdalosti();
        }

        holder.nazovUdalosti.setText(udalost.getNazov());
        holder.idUdalosti.setText(udalost.getIdUdalost());
        holder.denUdalosti.setText(nastavDenUdalosti(udalost.getDatum()));
        holder.mesiacUdalosti.setText(nastavMesiacUdalosti(udalost.getDatum()));
        holder.casUdalosti.setText(ziskajHodinyMinuty(udalost.getCas()));

        nastavMiestoUdalosti(holder, udalost.getMesto());

        if (animacie) {
            nacitajObsah(holder.udalost);
        }
    }

    @Override
    public int getItemCount() {
        return hladanaUdalost.size();
    }

    @Override
    public Filter getFilter() {
        animacie = false;
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence hladanaUdalost) {
                String slovo = hladanaUdalost.toString();
                if (slovo.isEmpty()) {
                    UdalostAdapter.this.hladanaUdalost = zoznamUdalosti;
                } else {
                    List<Udalost> udaje = new ArrayList<>();
                    for (Udalost udalost : zoznamUdalosti) {
                        if (udalost.getNazov().toLowerCase().contains(slovo.toLowerCase())) {
                            udaje.add(udalost);
                        }
                    }
                    UdalostAdapter.this.hladanaUdalost = udaje;
                }
                FilterResults vysledok = new FilterResults();
                vysledok.values = UdalostAdapter.this.hladanaUdalost;
                return vysledok;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                hladanaUdalost = (ArrayList<Udalost>) filterResults.values;
                if (hladanaUdalost.size() < zoznamUdalosti.size()) {
                    animacie = true;
                }
                notifyDataSetChanged();
            }
        };
    }

    private void nastavMiestoUdalosti(UdalostHolder holder, String miesto) {
        String miestoUdalosti[] = miesto.split(" ");
        if (miestoUdalosti.length > 1) {
            holder.mestoUdalosti.setText(miestoUdalosti[0]);
            holder.miestoUdalosti.setText(miestoUdalosti[1]);
        } else {
            holder.mestoUdalosti.setText(miesto);
        }
    }

    private String nastavDenUdalosti(String den) {
        return den.substring(den.lastIndexOf("-") + 1, den.length()) + ".";
    }

    private String nastavMesiacUdalosti(String mesiac) {
        return new DateFormatSymbols().getMonths()[Integer.parseInt(mesiac.substring(mesiac.indexOf("-") + 1, mesiac.lastIndexOf("-"))) - 1];
    }

    private String ziskajHodinyMinuty(String cas) {
        return cas.substring(0, cas.lastIndexOf(":"));
    }

    private void nacitajObsah(View view) {
        ScaleAnimation animacia = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animacia.setDuration(350);
        view.startAnimation(animacia);
    }

    public class UdalostHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RelativeLayout udalost;
        private ImageView obrazokUdalosti;

        private WeakReference<TlacidlaGesta> tlacidlaGesta;
        private Button budemTam;
        private Button pozvat;

        private TextView idUdalosti;
        private TextView nazovUdalosti;
        private TextView denUdalosti;
        private TextView mesiacUdalosti;
        private TextView mestoUdalosti;
        private TextView miestoUdalosti;
        private TextView casUdalosti;

        private UdalostHolder(View view) {
            super(view);

            this.udalost = view.findViewById(R.id.udalosti_riadok);
            this.obrazokUdalosti = view.findViewById(R.id.udalosti_obrazok);

            this.tlacidlaGesta = new WeakReference<>(UdalostAdapter.this.tlacidlaGesta);
            this.budemTam = view.findViewById(R.id.budem_tam);
            this.pozvat = view.findViewById(R.id.pozvat);

            this.budemTam.setOnClickListener(this);
            this.pozvat.setOnClickListener(this);

            this.idUdalosti = view.findViewById(R.id.id_udalost);
            this.nazovUdalosti = view.findViewById(R.id.udalosti_nazov);
            this.denUdalosti = view.findViewById(R.id.udalosti_den);
            this.mesiacUdalosti = view.findViewById(R.id.udalosti_mesiac);
            this.mestoUdalosti = view.findViewById(R.id.udalosti_mesto);
            this.miestoUdalosti = view.findViewById(R.id.udalosti_miesto);
            this.casUdalosti = view.findViewById(R.id.udalosti_cas);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == budemTam.getId()) {
                tlacidlaGesta.get().budemTam(Integer.parseInt(idUdalosti.getText().toString()));

                if (budemTam.getText().toString().equals(context.getString(R.string.list_zoznam_udalosti_tlacidlo_mam_zaujem))) {
                    budemTam.setText(context.getString(R.string.list_zoznam_udalosti_tlacidlo_budem_tam));
                    budemTam.setBackground(context.getResources().getDrawable(R.drawable.ic_tlacidlo_udalosti_aktivna));
                } else {
                    budemTam.setText(context.getString(R.string.list_zoznam_udalosti_tlacidlo_mam_zaujem));
                    budemTam.setBackground(context.getResources().getDrawable(R.drawable.ic_tlacidlo_udalosti_inaktivna));
                }
            } else if (v.getId() == pozvat.getId()) {
                tlacidlaGesta.get().pozvat(Integer.parseInt(idUdalosti.getText().toString()));
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
            Bitmap bitmap;
            try {
                InputStream zdroj = new java.net.URL(adresaObrazka).openStream();
                bitmap = BitmapFactory.decodeStream(zdroj);
            } catch (Exception e) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.udalosti_chyba_obrazka);
                Log.e("Chyba ", e.getMessage());
                e.printStackTrace();

            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            obrazok.setImageBitmap(result);
        }
    }
}