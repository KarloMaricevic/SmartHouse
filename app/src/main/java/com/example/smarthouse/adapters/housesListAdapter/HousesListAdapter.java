package com.example.smarthouse.adapters.housesListAdapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.example.smarthouse.R;
import com.example.smarthouse.UI.HousesListFragmentDirections;
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


    Context mAppContext;
    List<UsersHouseInfo> mUsersHausesList;
    IOption mIOption;


    @Inject
    public HousesListAdapter(Context context,IOption iOption) {
        mUsersHausesList = null;
        mAppContext = context;
        this.mIOption = iOption;
    }

    public HousesListAdapter(List<UsersHouseInfo> usersHausesList,Context context,IOption iOption)
    {
        this.mUsersHausesList = usersHausesList;
        mAppContext = context;
        this.mIOption = iOption;
    }




    @NonNull
    @Override
    public HouseItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HouseItemBinding binding = HouseItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        HouseItemViewHolder vh = new HouseItemViewHolder(binding, mIOption);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull HouseItemViewHolder holder, int position) {
        UsersHouseInfo usersHauses = mUsersHausesList.get(position);
        holder.bind(usersHauses);

        Observable.just(
                Glide.with(mAppContext).load(usersHauses.getPicture_url()).centerCrop()
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
        return mUsersHausesList !=null ? mUsersHausesList.size() : 0;
    }


    public void setmUsersHausesList(List<UsersHouseInfo> mUsersHausesList) {
        this.mUsersHausesList = mUsersHausesList;
    }

    public static class  HouseItemViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener
    {
        HouseItemBinding binding;
        private final MenuItem.OnMenuItemClickListener onMenuItemClickListener;
        IOption iOption;



        public HouseItemViewHolder(HouseItemBinding binding,IOption iOption) {
            super(binding.getRoot());
            this.iOption = iOption;
            onMenuItemClickListener  = (menuItem) -> {
                iOption.onMenuItemClicked(menuItem.getItemId(),binding.getUsersHouse().getHauseId());
                return true;
            };
            this.binding = binding;
        }

        public void bind(UsersHouseInfo usersHouseInfo)
        {
            binding.setUsersHouse(usersHouseInfo);
            binding.setClickListener(
                    (view) ->
                    {
                        HousesListFragmentDirections.ActionHousesListFragmnetToRoomFragment action =
                                HousesListFragmentDirections.actionHousesListFragmnetToRoomFragment(usersHouseInfo.getHauseId(),usersHouseInfo.getName());
                        Navigation.findNavController(view).navigate(action);
                    }

            );

            binding.getRoot().setOnCreateContextMenuListener(this);
            binding.executePendingBindings();
        }

        public void bindPicture(RequestBuilder<Drawable> drawable)
        {
            drawable.into(binding.houseImage);
            binding.executePendingBindings();
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            SubMenu subMenu = contextMenu.addSubMenu(Menu.NONE,R.string.changePictureMenuId,1,R.string.changePictureMenuTitle);
            subMenu.clearHeader();
            MenuItem withCameraMenuItem = subMenu.add(Menu.NONE,R.string.withCameraMenuId,1,R.string.withCameraMenuTitle);
            MenuItem withStorageMenuItem = subMenu.add(Menu.NONE,R.string.withStorageMenuId,2,R.string.withStorageMenuTitle);
            MenuItem chanegeNameMenuItem = contextMenu.add(Menu.NONE,R.string.changeNameMenuId,2,R.string.changeNameMenuTitle);
            withCameraMenuItem.setOnMenuItemClickListener(onMenuItemClickListener);
            withStorageMenuItem.setOnMenuItemClickListener(onMenuItemClickListener);
            chanegeNameMenuItem.setOnMenuItemClickListener(onMenuItemClickListener);
        }






    }

}
