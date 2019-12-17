package com.example.smarthouse.repositorys.utilsData;

import com.example.smarthouse.data.UsersHouseInfo;

import durdinapps.rxfirebase2.RxFirebaseChildEvent;

public class MyArray<T> {
    RxFirebaseChildEvent.EventType eventType;
    T data;

    public MyArray() {
    }


    public MyArray(RxFirebaseChildEvent.EventType eventType, T data) {
        this.eventType = eventType;
        this.data = data;
    }

    public RxFirebaseChildEvent.EventType getEventType() {
        return eventType;
    }

    public void setEventType(RxFirebaseChildEvent.EventType eventType) {
        this.eventType = eventType;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
