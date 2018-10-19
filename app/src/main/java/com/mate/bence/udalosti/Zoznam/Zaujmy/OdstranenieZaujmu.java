package com.mate.bence.udalosti.Zoznam.Zaujmy;

import android.support.v7.widget.RecyclerView;

public interface OdstranenieZaujmu {
    void odstranit(RecyclerView.ViewHolder viewHolder, int smer, int pozicia);
}
