package com.example.ibuprofen.Controllers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.ibuprofen.Adapters.ItemTouchHelperAdapter;
import com.example.ibuprofen.Adapters.ItemTouchHelperViewHolder;


public class SwipeController extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mAdapter;
//    private boolean swipeBack;

    public SwipeController(ItemTouchHelperAdapter adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(0, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder source, @NonNull RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            setBackgroundColor(viewHolder, dX);
        } else {
            viewHolder.itemView.setBackgroundColor(Color.WHITE);
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private void setBackgroundColor(final RecyclerView.ViewHolder viewHolder, final float dX) {
        int transparency = Math.min(255, Math.abs((int) dX / 3));
        if (dX < 0) {
            viewHolder.itemView.setBackgroundColor(ColorUtils.setAlphaComponent(Color.parseColor("#BD4958"), transparency));
        } else {
            viewHolder.itemView.setBackgroundColor(ColorUtils.setAlphaComponent(Color.parseColor("#659E47"), transparency));
        }
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition(), direction);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onItemSelected();
        }

        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
        itemViewHolder.onItemClear();
    }

}
