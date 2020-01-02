package com.example.smarthouse.adapters.RoomFragment.Drawer;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RoomListAdapterDecorator extends RecyclerView.ItemDecoration {

    private final int mBottomItemMargin;
    private final int mLeftItemMargin;

    public RoomListAdapterDecorator(int bottomItemMargin, int leftItemMargin) {
        this.mBottomItemMargin = bottomItemMargin;
        this.mLeftItemMargin = leftItemMargin;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if( parent.getChildAdapterPosition(view) != 0)
        {
            outRect.bottom = mBottomItemMargin;
        }

        outRect.left = mLeftItemMargin;
    }
}
