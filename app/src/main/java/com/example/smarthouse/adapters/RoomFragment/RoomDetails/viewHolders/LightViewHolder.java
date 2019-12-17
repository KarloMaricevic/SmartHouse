package com.example.smarthouse.adapters.RoomFragment.RoomDetails.viewHolders;

import com.example.myexpandablerecyclerview.viewHolders.ChildViewHolder;
import com.example.smarthouse.data.roomData.Light;
import com.example.smarthouse.databinding.LightItemBinding;

public class LightViewHolder extends ChildViewHolder<Light> {

    LightItemBinding binding;

    public LightViewHolder(LightItemBinding binding) {
        super(binding.getRoot());
        this.binding  =binding;

    }
    public void bind(Light type) {
        binding.setLight(type);
        binding.executePendingBindings();
    }
}
