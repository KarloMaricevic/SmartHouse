package my.app.smarthouse.adapters.RoomFragment.RoomDetails.viewHolders;

import my.app.smarthouse.data.roomData.Door;
import my.app.smarthouse.data.roomData.Light;
import my.app.smarthouse.data.roomData.Temperature;

public interface RoomAdapterCallback {
    void changeValueOf(Door door);
    void changeValueOf(Light light);
    void changeValueOf(Temperature temperature);
}
