package com.example.smarthouse.data.roomData;

import androidx.annotation.NonNull;

public class RoomInfo {
    protected String roomId;
    protected String name;


    public RoomInfo() {
    }


    public RoomInfo(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    protected RoomInfo clone() {
        RoomInfo newRoomInfo = new RoomInfo();
        newRoomInfo.roomId = this.roomId;
        newRoomInfo.name = this.name;
        return newRoomInfo;
    }
}