package com.example.smarthouse.adapters.RoomFragment.RoomDetails.viewHolders;

import com.example.myexpandablerecyclerview.viewHolders.ChildViewHolder;
import com.example.smarthouse.data.roomData.Door;
import com.example.smarthouse.databinding.DoorItemBinding;

public class DoorViewHolder extends ChildViewHolder<Door> {


    DoorItemBinding binding;


    public DoorViewHolder(DoorItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }



    public void bind(Door type) {
        binding.setDoor(type);
        binding.executePendingBindings();
    }
}
