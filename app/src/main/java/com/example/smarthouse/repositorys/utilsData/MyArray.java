package com.example.smarthouse.repositorys.utilsData;

import durdinapps.rxfirebase2.RxFirebaseChildEvent;

public class MyArray<T> {
    RxFirebaseChildEvent.EventType mEventType;
    T mData;

    public MyArray() {
    }


    public MyArray(RxFirebaseChildEvent.EventType eventType, T data) {
        this.mEventType = eventType;
        this.mData = data;
    }

    public RxFirebaseChildEvent.EventType getEventType() {
        return mEventType;
    }

    public void setEventType(RxFirebaseChildEvent.EventType mEventType) {
        this.mEventType = mEventType;
    }

    public T getData() {
        return mData;
    }

    public void setData(T mData) {
        this.mData = mData;
    }
}
