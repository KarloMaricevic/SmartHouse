package com.example.smarthouse.data.roomData;

import java.util.List;

public class Room {
    protected String houseID;
    protected List<Door> doorList;
    protected List<Temperature> temperatureList;
    protected List<Light> lightsList;

    public Room() {
    }

    public String getHouseID() {
        return houseID;
    }

    public Room setHouseID(String houseID) {
        this.houseID = houseID;
        return this;
    }


    public List<Door> getDoorList() {
        return doorList;
    }

    public Room setDoorList(List<Door> doorList) {
        this.doorList = doorList;
        return this;
    }

    public List<Temperature> getTemperatureList() {
        return temperatureList;
    }

    public Room setTemperatureList(List<Temperature> temperatureList) {
        this.temperatureList = temperatureList;
        return this;
    }

    public List<Light> getLightsList() {
        return lightsList;
    }

    public Room setLightsList(List<Light> lightsList) {
        this.lightsList = lightsList;
        return this;
    }





}
