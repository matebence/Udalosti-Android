package com.mate.bence.udalosti.Zoznam;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class PoskitovelObsahu extends LinearLayoutManager {

    private static final String TAG = PoskitovelObsahu.class.getName();

    public PoskitovelObsahu(Context context) {
        super(context);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, "Pri poskitovanie obsahu nastala chyba");
        }
    }
}