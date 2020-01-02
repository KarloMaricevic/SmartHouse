package com.example.smarthouse.adapters.RoomFragment.RoomDetails.viewHolders;

import android.view.View;

import com.example.myexpandablerecyclerview.viewHolders.ChildViewHolder;
import com.example.smarthouse.data.roomData.Temperature;
import com.example.smarthouse.databinding.TemperatureItemBinding;

public class TemperatureViewHolder extends ChildViewHolder<Temperature> {

    TemperatureItemBinding mBinding;

    RoomAdapterCallback mCallback;

    View.OnClickListener onClickListener = (view) -> {
        if(mBinding.getTemperature() != null && mCallback != null)
        {
            mCallback.changeValueOf(mBinding.getTemperature());
        }
    };

    public TemperatureViewHolder(TemperatureItemBinding binding,RoomAdapterCallback callback) {
        super(binding.getRoot());
        this.mBinding = binding;
        this.mCallback = callback;
    }

    @Override
    public void bind(Temperature data) {
        mBinding.setTemperature(data);
        mBinding.setOnClickListner(onClickListener);
        mBinding.executePendingBindings();
    }
}
