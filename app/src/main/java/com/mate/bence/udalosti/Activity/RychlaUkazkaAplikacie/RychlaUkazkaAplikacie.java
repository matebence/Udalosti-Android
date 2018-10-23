package com.mate.bence.udalosti.Activity.RychlaUkazkaAplikacie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mate.bence.udalosti.Activity.Autentifikacia.Autentifikacia;
import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Data.Preferencie;

public class RychlaUkazkaAplikacie extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = RychlaUkazkaAplikacie.class.getName();

    private ViewPager navigaciaObsahu;
    private LinearLayout castUkazky;
    private Button preskocit, dalej;

    private Preferencie preferencie;
    private int[] obsah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_rychla_ukazka_aplikacie);

        init();
        zmenFarbuNaPriehladnu();
        pridajIndikatorObsahu(0);

        ObsahAdapter myViewPagerAdapter = new ObsahAdapter();

        this.preferencie = new Preferencie(this);
        this.navigaciaObsahu.setAdapter(myViewPagerAdapter);
        this.navigaciaObsahu.addOnPageChangeListener(zmenaObsahu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.preskocit:
                Autentifikacia();
                break;
            case R.id.dalej:
                int current = cast();
                if (current < obsah.length) {
                    this.navigaciaObsahu.setCurrentItem(current);
                } else {
                    Autentifikacia();
                }
                break;
        }
    }

    private void init() {
        Log.v(RychlaUkazkaAplikacie.TAG, "Metoda init - RychlaUkazkaAplikacie bola vykonana");

        this.preskocit = findViewById(R.id.preskocit);
        this.preskocit.setOnClickListener(this);

        this.dalej = findViewById(R.id.dalej);
        this.dalej.setOnClickListener(this);

        this.navigaciaObsahu = findViewById(R.id.navigacia_obsahu);
        this.castUkazky = findViewById(R.id.navigacia);

        this.obsah = new int[]{
                R.layout.fragment_rychla_ukazka_aplikacie_cast_1,
                R.layout.fragment_rychla_ukazka_aplikacie_cast_2,
                R.layout.fragment_rychla_ukazka_aplikacie_cast_3};
    }

    private void pridajIndikatorObsahu(int currentPage) {
        Log.v(RychlaUkazkaAplikacie.TAG, "Metoda pridajIndikatorObsahu bola vykonana");

        TextView[] cast = new TextView[obsah.length];

        int[] aktivna = getResources().getIntArray(R.array.pole_farieb_pre_aktivnu_obrazovku);
        int[] inaktivna = getResources().getIntArray(R.array.pole_farieb_pre_inaktivnu_obrazovku);

        this.castUkazky.removeAllViews();
        for (int i = 0; i < cast.length; i++) {
            cast[i] = new TextView(this);
            cast[i].setText(Html.fromHtml("&#8226; "));
            cast[i].setTextSize(35);
            cast[i].setTextColor(inaktivna[currentPage]);
            this.castUkazky.addView(cast[i]);
        }

        if (cast.length > 0)
            cast[currentPage].setTextColor(aktivna[currentPage]);
    }

    private void zmenFarbuNaPriehladnu() {
        Log.v(RychlaUkazkaAplikacie.TAG, "Metoda zmenFarbuNaPriehladnu bola vykonana");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private int cast() {
        Log.v(RychlaUkazkaAplikacie.TAG, "Metoda cast bola vykonana");

        return this.navigaciaObsahu.getCurrentItem() + 1;
    }

    private void Autentifikacia() {
        this.preferencie.nastavPrvyStart(false);
        startActivity(new Intent(RychlaUkazkaAplikacie.this, Autentifikacia.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    ViewPager.OnPageChangeListener zmenaObsahu = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int pozicia) {
            pridajIndikatorObsahu(pozicia);
            if (pozicia == obsah.length - 1) {
                dalej.setText(getString(R.string.rychla_ukazka_aplikacie_tlacidlo_ok));
                preskocit.setVisibility(View.GONE);
            } else {
                dalej.setText(getString(R.string.rychla_ukazka_aplikacie_tlacidlo_dalej));
                preskocit.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    public class ObsahAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            this.layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            assert this.layoutInflater != null;
            View view = this.layoutInflater.inflate(obsah[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return obsah.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}