package my.app.smarthouse.adapters.RoomFragment.RoomDetails;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import my.app.smarthouse.adapters.RoomFragment.RoomDetails.viewHolders.OptionsViewHolder;

public class ItemDecoration extends RecyclerView.ItemDecoration {

    private final int mVerticalSpaceHeight;
    private final int mChildVerticalSpaceHeight;


    public ItemDecoration(int parentVerticalSpaceHeight,int childVerticalSpaceHeight) {
        this.mVerticalSpaceHeight = parentVerticalSpaceHeight;
        this.mChildVerticalSpaceHeight = childVerticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        RecyclerView.ViewHolder viewHolder = parent.getChildViewHolder(view);
        if(viewHolder instanceof OptionsViewHolder){
            outRect.bottom = mVerticalSpaceHeight;
        }
        else {
            outRect.bottom = mChildVerticalSpaceHeight;
        }
    }
}
