package com.example.smarthouse.adapters.RoomFragment.Drawer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthouse.data.roomData.RoomInfo;
import com.example.smarthouse.databinding.RoomDrawerItemBinding;
import com.example.smarthouse.di.Scopes.PerFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


@PerFragment
public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.RoomNameViewHolder> {

    List<RoomInfo> data;
    Integer selected;
    String selectedRoomId;

    INavigation listener;


    @Inject
    public RoomListAdapter(INavigation iNavigation) {
        data = new ArrayList<>();
        selected = null;
        this.listener = iNavigation;
    }


    public void changeData(List<RoomInfo> data)
    {
        this.data = data;
        notifyDataSetChanged();
    }

    public List<RoomInfo> getData() {
        return data;
    }



    @NonNull
    @Override
    public RoomNameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RoomDrawerItemBinding binding = RoomDrawerItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new RoomNameViewHolder(binding,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomNameViewHolder holder, int position) {
        RoomInfo roomInfo = data.get(position);
        holder.itemView.setTag(position);
        if(selectedRoomId != null)
        {
            if(selectedRoomId.equals(getData().get(position).getRoomId()))
            {
                holder.bind(roomInfo,true);
            }
            else{
                holder.bind(roomInfo,false);
            }
        }
        else{
            holder.bind(roomInfo,false);
        }
    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setSelected(String roomId)
    {
        selectedRoomId = roomId;
        notifyDataSetChanged();
    }




    public static class RoomNameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RoomDrawerItemBinding binding;
        INavigation listener;


        public RoomNameViewHolder(RoomDrawerItemBinding binding,INavigation iNavigation) {
            super(binding.getRoot());
            binding.getRoot().setOnClickListener(this);
            this.binding = binding;
            listener = iNavigation;

        }


        public void bind(RoomInfo data, Boolean isSelected)
        {
            binding.setRoomInfo(data);
            binding.setIsSelected(isSelected);
            binding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            if(listener != null) {
                listener.onViewClick(binding.getRoomInfo().getRoomId());
            }
        }





    }


}