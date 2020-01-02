package com.example.smarthouse.data.roomData;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.PropertyName;

import java.security.InvalidParameterException;

public class Temperature implements Comparable<Temperature>,IdComparable {
    protected String name;
    @PropertyName("temeperatureId")
    public String temperatureId;
    protected Integer value;
    protected Integer targeted;


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

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getTargeted() {
        return targeted;
    }

    public void setTargeted(Integer targeted) {
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

    @Override
    public boolean compereById(IdComparable idComparable) {
        if (!(idComparable instanceof Temperature)) {
            throw new InvalidParameterException("Argument is not type of Door");
        }
        if (((Temperature) idComparable).temperatureId.equals(this.temperatureId)){
            return true;
        }
        else {
            return false;
        }
    }


}
