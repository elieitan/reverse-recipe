package com.example.reverserecipe.util;


public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPostion);
    void onItemDismiss(int position);
}
