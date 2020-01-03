package my.app.smarthouse.adapters.RoomFragment.RoomDetails.viewHolders;

import android.view.View;

import my.app.smarthouse.databinding.TabItemBinding;

import my.app.myexpandablerecyclerview.viewHolders.GroupViewHolder;

public class OptionsViewHolder extends GroupViewHolder {


    private TabItemBinding mBinding;


    public OptionsViewHolder(TabItemBinding binding) {
        super(binding.getRoot());
        this.mBinding = binding;
    }


    public void bind(String optionName, View.OnClickListener onClickListener)
    {
        mBinding.setOptionName(optionName);
        mBinding.setOnClickListener(onClickListener != null ? onClickListener : null);
        mBinding.setIsExpanded(false);
        mBinding.setOnClickListener(
                (view) -> {
                    if(listener != null)
                    {
                        listener.onGroupClick(getAdapterPosition());
                    }
                });
        mBinding.executePendingBindings();
    }

    @Override
    public void expand() {
        mBinding.setIsExpanded(true);
        mBinding.executePendingBindings();
    }

    @Override
    public void collapse() {
        mBinding.setIsExpanded(false);
        mBinding.executePendingBindings();
    }
}
