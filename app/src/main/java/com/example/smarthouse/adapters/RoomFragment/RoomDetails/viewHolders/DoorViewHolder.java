package com.example.smarthouse.adapters.RoomFragment.RoomDetails.viewHolders;

import android.view.View;

import com.example.myexpandablerecyclerview.viewHolders.ChildViewHolder;
import com.example.smarthouse.data.roomData.Door;
import com.example.smarthouse.databinding.DoorItemBinding;

public class DoorViewHolder extends ChildViewHolder<Door> {


    DoorItemBinding mBinding;
    RoomAdapterCallback mCallback;

    View.OnClickListener onClickListener = (view) -> {
        if(mBinding.getDoor() != null && mCallback != null)
        {
            mCallback.changeValueOf(mBinding.getDoor());
        }

    };



    public DoorViewHolder(DoorItemBinding binding,RoomAdapterCallback callback) {
        super(binding.getRoot());
        this.mBinding = binding;
        this.mCallback = callback;
    }



    public void bind(Door type) {
        mBinding.setDoor(type);
        mBinding.setOnClickListner(onClickListener);
        mBinding.executePendingBindings();
    }


}
