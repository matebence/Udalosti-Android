package com.mate.bence.udalosti.List;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.mate.bence.udalosti.List.Kalendar.KalendarAdapter;
import com.mate.bence.udalosti.List.Ludia.Priatelia.PriateliaAdapter;
import com.mate.bence.udalosti.List.Ludia.Vsetci.VsetciAdapter;

public class Gesta extends ItemTouchHelper.SimpleCallback {

    private RecyclerItemTouchHelperListener gesto;

    public Gesta(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener gesto) {
        super(dragDirs, swipeDirs);
        this.gesto = gesto;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            if (viewHolder instanceof VsetciAdapter.VsetciHolder) {
                final View riadok = ((VsetciAdapter.VsetciHolder) viewHolder).obsah;
                getDefaultUIUtil().onSelected(riadok);
            } else if (viewHolder instanceof PriateliaAdapter.PouzivatelHolder) {
                final View riadok = ((PriateliaAdapter.PouzivatelHolder) viewHolder).obsah;
                getDefaultUIUtil().onSelected(riadok);
            } else if (viewHolder instanceof KalendarAdapter.NaplanovanaUdalostHolder) {
                final View riadok = ((KalendarAdapter.NaplanovanaUdalostHolder) viewHolder).obsah;
                getDefaultUIUtil().onSelected(riadok);
            }
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (viewHolder instanceof VsetciAdapter.VsetciHolder) {
            final View riadok = ((VsetciAdapter.VsetciHolder) viewHolder).obsah;
            getDefaultUIUtil().onDrawOver(c, recyclerView, riadok, dX, dY, actionState, isCurrentlyActive);
        } else if (viewHolder instanceof PriateliaAdapter.PouzivatelHolder) {
            final View riadok = ((PriateliaAdapter.PouzivatelHolder) viewHolder).obsah;
            getDefaultUIUtil().onDrawOver(c, recyclerView, riadok, dX, dY, actionState, isCurrentlyActive);
        } else if (viewHolder instanceof KalendarAdapter.NaplanovanaUdalostHolder) {
            final View riadok = ((KalendarAdapter.NaplanovanaUdalostHolder) viewHolder).obsah;
            getDefaultUIUtil().onDrawOver(c, recyclerView, riadok, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof VsetciAdapter.VsetciHolder) {
            final View riadok = ((VsetciAdapter.VsetciHolder) viewHolder).obsah;
            getDefaultUIUtil().clearView(riadok);
        } else if (viewHolder instanceof PriateliaAdapter.PouzivatelHolder) {
            final View riadok = ((PriateliaAdapter.PouzivatelHolder) viewHolder).obsah;
            getDefaultUIUtil().clearView(riadok);
        } else if (viewHolder instanceof KalendarAdapter.NaplanovanaUdalostHolder) {
            final View riadok = ((KalendarAdapter.NaplanovanaUdalostHolder) viewHolder).obsah;
            getDefaultUIUtil().clearView(riadok);
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (viewHolder instanceof VsetciAdapter.VsetciHolder) {
            final View riadok = ((VsetciAdapter.VsetciHolder) viewHolder).obsah;
            getDefaultUIUtil().onDraw(c, recyclerView, riadok, dX, dY, actionState, isCurrentlyActive);
        } else if (viewHolder instanceof PriateliaAdapter.PouzivatelHolder) {
            final View riadok = ((PriateliaAdapter.PouzivatelHolder) viewHolder).obsah;
            getDefaultUIUtil().onDraw(c, recyclerView, riadok, dX, dY, actionState, isCurrentlyActive);
        } else if (viewHolder instanceof KalendarAdapter.NaplanovanaUdalostHolder) {
            final View riadok = ((KalendarAdapter.NaplanovanaUdalostHolder) viewHolder).obsah;
            getDefaultUIUtil().onDraw(c, recyclerView, riadok, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        gesto.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}