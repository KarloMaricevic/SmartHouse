package com.example.smarthouse.data.roomData;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.PropertyName;

public class Temperature implements Comparable<Temperature> {
    protected String name;
    @PropertyName("temeperatureId")
    public String temperatureId;
    protected String value;
    protected String targeted;


    public Temperature() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemperatureId() {
        return temperatureId;
    }

    public void setTemperatureId(String temperatureId) {
        this.temperatureId = temperatureId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTargeted() {
        return targeted;
    }

    public void setTargeted(String targeted) {
        this.targeted = targeted;
    }

    @Override
    public int compareTo(Temperature temperature) {
        return 0;
    }


    @NonNull
    @Override
    public Temperature clone() {
        try{
            Temperature newTemperature = new Temperature();
            newTemperature.name = this.name;
            newTemperature.targeted = this.targeted;
            newTemperature.temperatureId = this.temperatureId;
            newTemperature.value = this.value;
            return newTemperature;
        }
        catch (Exception e) {
            Log.e("Clone exception: ", e.getMessage());
            return null;
        }
    }
}
