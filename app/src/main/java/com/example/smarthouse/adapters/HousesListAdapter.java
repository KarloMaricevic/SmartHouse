package com.example.smarthouse.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.example.smarthouse.R;
import com.example.smarthouse.data.UsersHouseInfo;
import com.example.smarthouse.databinding.HouseItemBinding;
import com.example.smarthouse.di.Scopes.PerFragment;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@PerFragment
public class HousesListAdapter extends RecyclerView.Adapter<HousesListAdapter.HouseItemViewHolder> {


    Context appContext;
    List<UsersHouseInfo> usersHausesList;

    @Inject
    public HousesListAdapter(Context context) {
        usersHausesList = null;
        appContext = context;
    }

    public HousesListAdapter(List<UsersHouseInfo> usersHausesList,Context context)
    {
        this.usersHausesList = usersHausesList;
        appContext = context;
    }




    @NonNull
    @Override
    public HouseItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HouseItemBinding binding = HouseItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        HouseItemViewHolder vh = new HouseItemViewHolder(binding);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull HouseItemViewHolder holder, int position) {
        UsersHouseInfo usersHauses = usersHausesList.get(position);
        holder.bind(usersHauses);

        Observable.just(
                Glide.with(appContext).load(usersHauses.getPicture_url()).centerCrop()
                        .placeholder(R.drawable.ic_photo_blue_24dp)
                        .error(R.drawable.broken_image_black_24dp)
                        .fallback(R.drawable.ic_photo_blue_24dp)
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                (drawable) ->
                {
                    holder.bindPicture(drawable);
                },
                (e) -> {}

        );
    }

    @Override
    public int getItemCount() {
        return usersHausesList !=null ? usersHausesList.size() : 0;
    }








    public void setUsersHausesList(List<UsersHouseInfo> usersHausesList) {
        this.usersHausesList = usersHausesList;
    }

    public static class  HouseItemViewHolder extends RecyclerView.ViewHolder
    {
        HouseItemBinding binding;

        public HouseItemViewHolder(HouseItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(UsersHouseInfo usersHouseInfo)
        {
            binding.setUsersHouse(usersHouseInfo);
            binding.setClickListener(
                    (view) ->
                    {
                        Bundle bundle =  new Bundle();
                        bundle.putString("houseId",usersHouseInfo.getHauseId());
                        bundle.putString("houseName",usersHouseInfo.getName());
                        Navigation.findNavController(view).navigate(R.id.action_housesListFragmnet_to_houseFragment,bundle);
                    }

            );
            binding.executePendingBindings();
        }

        public void bindPicture(RequestBuilder<Drawable> drawable)
        {
            drawable.into(binding.houseImage);
            binding.executePendingBindings();
        }




    }

}
