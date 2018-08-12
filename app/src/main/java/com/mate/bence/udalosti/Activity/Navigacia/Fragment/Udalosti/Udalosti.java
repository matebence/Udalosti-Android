package com.mate.bence.udalosti.Activity.Navigacia.Fragment.Udalosti;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mate.bence.udalosti.Activity.Navigacia.Fragment.Udalosti.Karty.Kalendar;
import com.mate.bence.udalosti.Activity.Navigacia.Fragment.Udalosti.Karty.Objavuj;
import com.mate.bence.udalosti.Activity.Navigacia.Fragment.Udalosti.Karty.Oznamenia;
import com.mate.bence.udalosti.Activity.Navigacia.Navigacia;
import com.mate.bence.udalosti.R;

import java.util.ArrayList;
import java.util.List;

public class Udalosti extends Fragment {

    private Bundle bundle;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aplikacia_udalosti, container, false);
        init(view);

        return view;
    }

    public void init(View view) {
        String email = getArguments().getString("email");
        String token = getArguments().getString("token");
        String stat = getArguments().getString("stat");

        this.bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("token", token);
        bundle.putString("stat", stat);

        ViewPager dalsieKarty = view.findViewById(R.id.udalosti_gesta);
        TabLayout karty = view.findViewById(R.id.udalosti_karty);

        nastavKarty(dalsieKarty);
        karty.setupWithViewPager(dalsieKarty);
        karty.addOnTabSelectedListener(zmenFarbuIkonov);

        nastavIkonyKartov(karty);
    }

    private void nastavIkonyKartov(TabLayout karty) {
        int ikonyKartov[] = {R.drawable.ic_objavuj, R.drawable.ic_oznamenia, R.drawable.ic_kalendar};
        for (int i = 0; i < karty.getTabCount(); i++) {
            karty.getTabAt(i).setIcon(ikonyKartov[i]);
        }
    }

    private void nastavKarty(ViewPager viewPager) {
        GestaKariet viewPagerAdapter = new GestaKariet(this.getChildFragmentManager());

        Objavuj objavuj = new Objavuj();
        Oznamenia oznamenia = new Oznamenia();
        Kalendar kalendar = new Kalendar();

        objavuj.setArguments(bundle);
        oznamenia.setArguments(bundle);
        kalendar.setArguments(bundle);

        viewPagerAdapter.pridajFragment(objavuj);
        viewPagerAdapter.pridajFragment(oznamenia);
        viewPagerAdapter.pridajFragment(kalendar);

        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(viewPagerAdapter);
    }

    private TabLayout.OnTabSelectedListener zmenFarbuIkonov = new TabLayout.OnTabSelectedListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            switch (tab.getPosition()) {
                case 0:
                    ((Navigacia) getActivity()).titul.setText("Udalosti");
                    break;
                case 1:
                    ((Navigacia) getActivity()).titul.setText("Oznámenia");
                    break;
                case 2:
                    ((Navigacia) getActivity()).titul.setText("Kalendár");
                    break;
            }

            int tabIconColor = ContextCompat.getColor(getContext(), R.color.tab_ikona_aktivna);
            tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            int tabIconColor = ContextCompat.getColor(getContext(), R.color.tab_ikona_inaktivna);
            tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            int tabIconColor = ContextCompat.getColor(getContext(), R.color.tab_ikona_aktivna);
            tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        }
    };

    private class GestaKariet extends FragmentPagerAdapter {

        private final List<Fragment> fragmenty = new ArrayList<>();

        private GestaKariet(FragmentManager manager) {
            super(manager);
        }

        private void pridajFragment(Fragment fragment) {
            fragmenty.add(fragment);
        }

        @Override
        public Fragment getItem(int pozicia) {
            switch (pozicia) {
                case 0:
                    Objavuj objavuj = new Objavuj();
                    objavuj.setArguments(bundle);

                    return objavuj;
                case 1:
                    Oznamenia oznamenia = new Oznamenia();
                    oznamenia.setArguments(bundle);

                    return oznamenia;
                case 2:
                    Kalendar kalendar = new Kalendar();
                    kalendar.setArguments(bundle);

                    return kalendar;
            }
            return null;
        }

        @Override
        public int getCount() {
            return fragmenty.size();
        }
    }
}