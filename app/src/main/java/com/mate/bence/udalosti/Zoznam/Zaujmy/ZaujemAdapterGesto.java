package com.mate.bence.udalosti.Zoznam.Zaujmy;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

public class ZaujemAdapterGesto extends ItemTouchHelper.SimpleCallback {

    private static final String TAG = ZaujemAdapterGesto.class.getName();
    private OdstranenieZaujmu odstranenieZaujmu;

    public ZaujemAdapterGesto(int dragDirs, int swipeDirs, OdstranenieZaujmu odstranenieZaujmu) {
        super(dragDirs, swipeDirs);

        Log.v(ZaujemAdapterGesto.TAG, "Metoda ZaujemAdapterGesto bola vykonana");
        this.odstranenieZaujmu = odstranenieZaujmu;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            final View foregroundView = ((ZaujemAdapter.MesiacZaujmovHolder) viewHolder).zaujem;

            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((ZaujemAdapter.MesiacZaujmovHolder) viewHolder).zaujem;
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((ZaujemAdapter.MesiacZaujmovHolder) viewHolder).zaujem;
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((ZaujemAdapter.MesiacZaujmovHolder) viewHolder).zaujem;

        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        odstranenieZaujmu.odstranit(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }
}