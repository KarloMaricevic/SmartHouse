package com.example.smarthouse.adapters.RoomFragment.RoomDetails;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.myexpandablerecyclerview.ExpandCollapseController;
import com.example.myexpandablerecyclerview.MultiTypeExpandableRecyclerViewAdapter;
import com.example.myexpandablerecyclerview.data.ExpandableGroup;
import com.example.myexpandablerecyclerview.data.ExpandableList;
import com.example.myexpandablerecyclerview.viewHolders.ChildViewHolder;
import com.example.smarthouse.adapters.RoomFragment.RoomDetails.viewHolders.DoorViewHolder;
import com.example.smarthouse.adapters.RoomFragment.RoomDetails.viewHolders.LightViewHolder;
import com.example.smarthouse.adapters.RoomFragment.RoomDetails.viewHolders.OptionsViewHolder;
import com.example.smarthouse.adapters.RoomFragment.RoomDetails.viewHolders.TemperatureViewHolder;
import com.example.smarthouse.data.roomData.Door;
import com.example.smarthouse.data.roomData.Light;
import com.example.smarthouse.data.roomData.Temperature;
import com.example.smarthouse.databinding.DoorItemBinding;
import com.example.smarthouse.databinding.LightItemBinding;
import com.example.smarthouse.databinding.TabItemBinding;
import com.example.smarthouse.databinding.TemperatureItemBinding;
import com.example.smarthouse.di.Scopes.PerFragment;

import java.util.List;

import javax.inject.Inject;


@PerFragment
public class MyMultiTypeAdapter  extends MultiTypeExpandableRecyclerViewAdapter<OptionsViewHolder, ChildViewHolder> {


    public static final int OPTIONS_VIEW_TYPE = 3;
    public static final int DOOR_VOEW_TYPE = 4;
    public static final  int  LIGHT_VIEW_TYPE = 5;
    public static final int TEMPERATURE_VIEW_TYPE = 6;


    @Inject
    public MyMultiTypeAdapter() {
        super();
    }


    public MyMultiTypeAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    public void changeDataSet(List<? extends ExpandableGroup> groups)
    {
        expandableList = new ExpandableList(groups);
        expandCollapseController = new ExpandCollapseController(expandableList, this);
        notifyDataSetChanged();
    }






    @Override
    public OptionsViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        TabItemBinding binding = TabItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new OptionsViewHolder(binding);
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        switch (viewType)
        {
            case DOOR_VOEW_TYPE :
                DoorItemBinding doorBinding = DoorItemBinding.inflate(LayoutInflater.from(parent.getContext()));
                return new DoorViewHolder(doorBinding);
            case LIGHT_VIEW_TYPE :
                LightItemBinding lightBinding = LightItemBinding.inflate(LayoutInflater.from(parent.getContext()));
                return new LightViewHolder(lightBinding);
            case TEMPERATURE_VIEW_TYPE :
                TemperatureItemBinding temperatureBinding = TemperatureItemBinding.inflate(LayoutInflater.from(parent.getContext()));
                return new TemperatureViewHolder(temperatureBinding);
            default:
                throw  new IllegalArgumentException("WrongType");
        }
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        int viewType = getItemViewType(flatPosition);
        switch (viewType){
            case DOOR_VOEW_TYPE:
            {
                Door door = ((Door) group.getItems().get(childIndex));
                holder.bind(door);
                break;
            }
            case LIGHT_VIEW_TYPE :
            {
                Light light = ((Light) group.getItems().get(childIndex));
                holder.bind(light);
                break;
            }
            case TEMPERATURE_VIEW_TYPE:
            {
                Temperature temperature = (Temperature) group.getItems().get(childIndex);
                holder.bind(temperature);
                break;
            }
            default:
            {
                throw new IllegalArgumentException("Wrong type");
            }

        }
    }


    @Override
    public int getChildViewType(int position, ExpandableGroup group, int childIndex) {
        if(group.getItems().get(childIndex) instanceof Door)
        {
            return DOOR_VOEW_TYPE;
        }
        else if (group.getItems().get(childIndex) instanceof Light)
        {
            return LIGHT_VIEW_TYPE;
        }
        else if(group.getItems().get(childIndex) instanceof Temperature)
        {
            return TEMPERATURE_VIEW_TYPE;
        }
        else {
            throw new IllegalArgumentException("WrongType");
        }
    }


    @Override
    public boolean isChild(int viewType) {
        return viewType == DOOR_VOEW_TYPE || viewType == LIGHT_VIEW_TYPE ||viewType == TEMPERATURE_VIEW_TYPE;
    }


    @Override
    public void onBindGroupViewHolder(OptionsViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.bind(group.getTitle(),null);
    }

    @Override
    public int getGroupViewType(int position, ExpandableGroup group) {
        return OPTIONS_VIEW_TYPE;
    }

    @Override
    public boolean isGroup(int viewType) {
        return viewType == OPTIONS_VIEW_TYPE;
    }
}
