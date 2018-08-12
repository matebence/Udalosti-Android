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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Siet.UdalostiAdresa;

import java.io.InputStream;
import java.util.List;

public class UdalostAdapter extends RecyclerView.Adapter<UdalostAdapter.UdalostHolder> {

    private List<Udalost> zoznamUdalosti;
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

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull UdalostHolder holder, int position) {
        Udalost udalost = zoznamUdalosti.get(position);

        new ziskajObrazok(holder.obrazokUdalosti, context).execute(udalost.getObrazok());

        holder.nazovUdalosti.setText(udalost.getNazov());
        holder.idUdalosti.setText(udalost.getIdUdalost());
        holder.denUdalosti.setText(udalost.getDen());
        holder.mesiacUdalosti.setText(udalost.getMesiac());
        holder.casUdalosti.setText(udalost.getCas());

        nastavMiestoUdalosti(holder, udalost.getMiesto());

        nacitajObsah(holder.udalost);
    }

    @Override
    public int getItemCount() {
        return zoznamUdalosti.size();
    }

    private void nastavMiestoUdalosti(UdalostHolder holder, String miesto) {
        String miestoUdalosti[] = miesto.split(",");
        if (miestoUdalosti.length > 1) {
            holder.mestoUdalosti.setText(miestoUdalosti[0]);
            holder.miestoUdalosti.setText(miestoUdalosti[1]);
        } else {
            holder.mestoUdalosti.setText(miesto);
        }
    }

    private void nacitajObsah(View view) {
        ScaleAnimation animacia = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animacia.setDuration(350);
        view.startAnimation(animacia);
    }

    class UdalostHolder extends RecyclerView.ViewHolder {

        private RelativeLayout udalost;
        private ImageView obrazokUdalosti;

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

            this.idUdalosti = view.findViewById(R.id.id_udalost);
            this.nazovUdalosti = view.findViewById(R.id.udalosti_nazov);
            this.denUdalosti = view.findViewById(R.id.udalosti_den);
            this.mesiacUdalosti = view.findViewById(R.id.udalosti_mesiac);
            this.mestoUdalosti = view.findViewById(R.id.udalosti_mesto);
            this.miestoUdalosti = view.findViewById(R.id.udalosti_miesto);
            this.casUdalosti = view.findViewById(R.id.udalosti_cas);
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