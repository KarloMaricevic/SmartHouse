package com.example.smarthouse.adapters.RoomFragment.RoomDetails.viewHolders;

import com.example.smarthouse.data.roomData.Door;
import com.example.smarthouse.data.roomData.Light;
import com.example.smarthouse.data.roomData.Temperature;

public interface RoomAdapterCallback {
    void changeValueOf(Door door);
    void changeValueOf(Light light);
    void changeValueOf(Temperature temperature);
}
