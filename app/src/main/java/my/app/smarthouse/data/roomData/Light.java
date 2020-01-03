package my.app.smarthouse.data.roomData;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.PropertyName;

import java.security.InvalidParameterException;

public class Light implements Comparable<Light>,IdComparable {

    protected String lightId;
    protected String name;
    @PropertyName("turned on")
    public Boolean turnedOn;
    protected Boolean targeted;


    public Light() {
    }

    public String getLightId() {
        return lightId;
    }

    public void setLightId(String lightId) {
        this.lightId = lightId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getTurnedOn() {
        return turnedOn;
    }

    public void setTurnedOn(Boolean turnedOn) {
        this.turnedOn = turnedOn;
    }

    public Boolean getTargeted() {
        return targeted;
    }

    public void setTargeted(Boolean targeted) {
        this.targeted = targeted;
    }

    @Override
    public int compareTo(Light light) {
        return 0;
    }

    @NonNull
    @Override
    public Light clone() {
        try {
            Light newLight = new Light();
            newLight.setName(this.name);
            newLight.setTargeted(this.targeted);
            newLight.setTurnedOn(this.turnedOn);
            newLight.setLightId(this.lightId);
            return newLight;
        }
        catch (Exception e)
        {
            Log.e("Clone exception ",e.getMessage());
            return null;
        }
    }

    @Override
    public boolean compereById(IdComparable idComparable) {
        if (!(idComparable instanceof Light)) {
            throw new InvalidParameterException("Argument is not type of Light");
        }
        if (((Light) idComparable).lightId.equals(this.lightId)){
            return true;
        }
        else {
            return false;
        }
    }
}
