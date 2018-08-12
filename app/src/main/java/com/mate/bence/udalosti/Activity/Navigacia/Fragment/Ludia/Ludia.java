package com.mate.bence.udalosti.Activity.Navigacia.Fragment.Ludia;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mate.bence.udalosti.Activity.Navigacia.Fragment.Ludia.Karty.Priatelia;
import com.mate.bence.udalosti.Activity.Navigacia.Fragment.Ludia.Karty.Vsetci;
import com.mate.bence.udalosti.R;

import java.util.ArrayList;
import java.util.List;

public class Ludia extends Fragment {

    private String email, token;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aplikacia_ludia, container, false);
        init(view);

        return view;
    }

    public void init(View view) {
        this.email = getArguments().getString("email");
        this.token = getArguments().getString("token");

        ViewPager dalsieKarty = view.findViewById(R.id.ludia_gesta);
        TabLayout karty = view.findViewById(R.id.ludia_karty);

        nastavKarty(dalsieKarty);
        karty.setupWithViewPager(dalsieKarty);
    }

    private void nastavKarty(ViewPager viewPager) {
        GestaKariet gestaKariet = new GestaKariet(this.getChildFragmentManager());
        Bundle bundle = new Bundle();

        Vsetci vsetci = new Vsetci();
        Priatelia priatelia = new Priatelia();

        bundle.putString("email", email);
        bundle.putString("token", token);

        vsetci.setArguments(bundle);
        priatelia.setArguments(bundle);

        gestaKariet.pridajFragment(vsetci, "VŠETCI");
        gestaKariet.pridajFragment(priatelia, "PRIATELIA");
        viewPager.setAdapter(gestaKariet);
    }

    private class GestaKariet extends FragmentPagerAdapter {

        private final List<Fragment> fragmenty = new ArrayList<>();
        private final List<String> nazvyFragmentov = new ArrayList<>();

        private GestaKariet(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int pozicia) {
            Bundle bundle = new Bundle();

            Vsetci vsetci = new Vsetci();
            Priatelia priatelia = new Priatelia();

            bundle.putString("email", email);
            bundle.putString("token", token);

            vsetci.setArguments(bundle);
            priatelia.setArguments(bundle);

            switch (pozicia) {
                case 0:
                    return vsetci;
                case 1:
                    return priatelia;
            }
            return null;
        }

        @Override
        public int getCount() {
            return fragmenty.size();
        }

        void pridajFragment(Fragment fragment, String titul) {
            fragmenty.add(fragment);
            nazvyFragmentov.add(titul);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return nazvyFragmentov.get(position);
        }
    }
}