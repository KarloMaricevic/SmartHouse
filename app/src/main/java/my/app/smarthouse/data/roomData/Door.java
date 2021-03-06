package my.app.smarthouse.data.roomData;

import android.util.Log;

import java.security.InvalidParameterException;

public class Door implements IdComparable,Comparable<Door> {

    protected String doorId;
    protected Boolean locked;
    protected String name;
    protected  Boolean targeted;


    public Door() {
    }


    public String getDoorId() {
        return doorId;
    }

    public void setDoorId(String doorId) {
        this.doorId = doorId;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getTargeted() {
        return targeted;
    }

    public void setTargeted(Boolean targeted) {
        this.targeted = targeted;
    }

    @Override
    public int compareTo(Door door) {
            return 0;
    }


    @Override
    public Door clone(){
        try {
            Door newDoor = new Door();
            newDoor.setTargeted(this.targeted);
            newDoor.setLocked(this.locked);
            newDoor.setName(this.name);
            newDoor.setDoorId(this.doorId);
            return newDoor;
        }
        catch (Exception e)
        {
            Log.e("Clone exception: ",e.getMessage());
            return null;
        }
    }


    @Override
    public boolean compereById(IdComparable idComparable) {
        if (!(idComparable instanceof Door)) {
            throw new InvalidParameterException("Argument is not type of Door");
        }
        if (((Door) idComparable).doorId.equals(this.doorId)){
            return true;
        }
        else {
            return false;
        }
    }
}

