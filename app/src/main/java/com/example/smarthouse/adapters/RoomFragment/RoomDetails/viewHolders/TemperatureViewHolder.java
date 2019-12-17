package com.example.smarthouse.adapters.RoomFragment.RoomDetails.viewHolders;

import com.example.myexpandablerecyclerview.viewHolders.ChildViewHolder;
import com.example.smarthouse.data.roomData.Temperature;
import com.example.smarthouse.databinding.TemperatureItemBinding;

public class TemperatureViewHolder extends ChildViewHolder<Temperature> {

    TemperatureItemBinding binding;

    public TemperatureViewHolder(TemperatureItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    @Override
    public void bind(Temperature data) {
        binding.setTemperature(data);
        binding.executePendingBindings();
    }
}
