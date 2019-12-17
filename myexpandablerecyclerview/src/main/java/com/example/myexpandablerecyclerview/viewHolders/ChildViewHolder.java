package com.example.myexpandablerecyclerview.viewHolders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ChildViewHolder<T> extends RecyclerView.ViewHolder {
    public ChildViewHolder(@NonNull View itemView) {
        super(itemView);
    }


    public abstract void bind(T data);

}
