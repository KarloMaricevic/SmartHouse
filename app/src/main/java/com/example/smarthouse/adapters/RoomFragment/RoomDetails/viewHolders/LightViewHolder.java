package com.example.smarthouse.adapters.RoomFragment.RoomDetails.viewHolders;

import android.view.View;

import com.example.myexpandablerecyclerview.viewHolders.ChildViewHolder;
import com.example.smarthouse.data.roomData.Light;
import com.example.smarthouse.databinding.LightItemBinding;

public class LightViewHolder extends ChildViewHolder<Light> {

    LightItemBinding mBinding;
    RoomAdapterCallback mCallback;

    View.OnClickListener onClickListener = (view) -> {
        if(mBinding.getLight() != null && mCallback != null)
        {
            mCallback.changeValueOf(mBinding.getLight());
        }

    };


    public LightViewHolder(LightItemBinding binding,RoomAdapterCallback callback) {
        super(binding.getRoot());
        this.mBinding =binding;
        this.mCallback = callback;


    }
    public void bind(Light type) {
        mBinding.setLight(type);
        mBinding.setOnClickListner(onClickListener);
        mBinding.executePendingBindings();
    }
}
