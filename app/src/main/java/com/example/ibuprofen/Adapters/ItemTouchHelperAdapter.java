package com.example.ibuprofen.Adapters;

public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position, int direction);
}
