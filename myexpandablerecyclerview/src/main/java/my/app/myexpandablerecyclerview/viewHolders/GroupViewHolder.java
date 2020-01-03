package my.app.myexpandablerecyclerview.viewHolders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import my.app.myexpandablerecyclerview.listeners.OnGroupClickListener;

public abstract class GroupViewHolder extends RecyclerView.ViewHolder{

    protected OnGroupClickListener listener;

    public GroupViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void setOnGroupClickListener(OnGroupClickListener listener)
    {
        this.listener = listener;
    }

    public abstract void expand();

    public abstract void collapse();

    public OnGroupClickListener getListener() {
        return listener;
    }
}