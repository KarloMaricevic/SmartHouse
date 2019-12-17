package com.example.smarthouse.data;

public class UsersHouseInfo {
    String name;
    String hauseId;
    String picture_url;
    String picture_updated;


    public UsersHouseInfo() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHauseId() {
        return hauseId;
    }

    public void setHauseId(String hauseId) {
        this.hauseId = hauseId;
    }


    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public String getPicture_updated() {
        return picture_updated;
    }

    public void setPicture_updated(String picture_updated) {
        this.picture_updated = picture_updated;
    }
}
