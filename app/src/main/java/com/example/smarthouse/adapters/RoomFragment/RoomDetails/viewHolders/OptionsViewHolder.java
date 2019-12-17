package com.example.smarthouse.adapters.RoomFragment.RoomDetails.viewHolders;

import android.view.View;

import com.example.myexpandablerecyclerview.viewHolders.GroupViewHolder;
import com.example.smarthouse.databinding.TabItemBinding;

public class OptionsViewHolder extends GroupViewHolder {


    private TabItemBinding binding;


    public OptionsViewHolder(TabItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }


    public void bind(String optionName, View.OnClickListener onClickListener)
    {
        binding.setOptionName(optionName);
        binding.setOnClickListener(onClickListener != null ? onClickListener : null);
        binding.setIsExpanded(false);
        binding.setOnClickListener(
                (view) -> {
                    if(listener != null)
                    {
                        listener.onGroupClick(getAdapterPosition());
                    }
                });
        binding.executePendingBindings();
    }

    @Override
    public void expand() {
        binding.setIsExpanded(true);
        binding.executePendingBindings();
    }

    @Override
    public void collapse() {
        binding.setIsExpanded(false);
        binding.executePendingBindings();
    }
}
