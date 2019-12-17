package com.example.smarthouse.adapters.RoomFragment.RoomDetails;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthouse.adapters.RoomFragment.RoomDetails.viewHolders.OptionsViewHolder;

public class ItemDecoration extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;
    private final int childVerticalSpaceHeight;


    public ItemDecoration(int parentVerticalSpaceHeight,int childVerticalSpaceHeight) {
        this.verticalSpaceHeight = parentVerticalSpaceHeight;
        this.childVerticalSpaceHeight = childVerticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        RecyclerView.ViewHolder viewHolder = parent.getChildViewHolder(view);
        if(viewHolder instanceof OptionsViewHolder){
            outRect.bottom = verticalSpaceHeight;
        }
        else {
            outRect.bottom = childVerticalSpaceHeight;
        }
    }
}
