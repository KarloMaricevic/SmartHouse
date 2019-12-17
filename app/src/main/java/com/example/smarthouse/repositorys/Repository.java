package com.example.smarthouse.repositorys;

import android.app.Application;
import android.util.Log;

import com.example.smarthouse.data.roomData.Door;
import com.example.smarthouse.data.roomData.Light;
import com.example.smarthouse.data.roomData.Room;
import com.example.smarthouse.data.roomData.RoomInfo;
import com.example.smarthouse.data.User;
import com.example.smarthouse.data.UsersHouseInfo;
import com.example.smarthouse.data.roomData.Temperature;
import com.example.smarthouse.repositorys.utilsData.MyArray;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.intellij.lang.annotations.Flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import durdinapps.rxfirebase2.RxFirebaseChildEvent;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;


@Singleton
public class Repository {

    @Inject
    public Repository(Application application) {
        FirebaseApp.initializeApp(application.getApplicationContext());

    }

    public Maybe<String> getPassword(String username) {
        Query query = FirebaseDatabase.getInstance().getReference("users/").orderByChild("username").equalTo(username);
        Maybe<String> queryObservable = RxFirebaseDatabase.observeSingleValueEvent(query,
                (dataSnapshot -> {
                    for (DataSnapshot dataSnapshotUser : dataSnapshot.getChildren()) {
                        User user = dataSnapshotUser.getValue(User.class);
                        return user.getPassword();
                    }
                    return null;
                })
        ).subscribeOn(Schedulers.io()).cache();
        return queryObservable;
    }

    public Flowable<String> getUsersCredencilas(String username) {
        Query query = FirebaseDatabase.getInstance().getReference("users/").orderByChild("username").equalTo(username);
        Flowable<String> flowable = RxFirebaseDatabase.observeValueEvent(query, (dataSnapshot -> {
                    for (DataSnapshot dataSnapshotUser : dataSnapshot.getChildren()) {
                        User user = dataSnapshotUser.getValue(User.class);
                        return user.getPassword();
                    }
                    return "";
                })
        ).subscribeOn(Schedulers.io());
        return flowable;
    }

    public Flowable getUserHouses(String username) {
        Query query = FirebaseDatabase.getInstance().getReference("usersHauses/" + username);

        Flowable<List<UsersHouseInfo>> emptyListFlowable =Flowable.just(new ArrayList<UsersHouseInfo>());

        Flowable<List<UsersHouseInfo>> queryFlowable = RxFirebaseDatabase.observeChildEvent(query,UsersHouseInfo.class,BackpressureStrategy.BUFFER)
                .map(dataSnapshotRxFirebaseChildEvent -> {
                    UsersHouseInfo usersHouseInfo = dataSnapshotRxFirebaseChildEvent.getValue();
                    usersHouseInfo.setHauseId(dataSnapshotRxFirebaseChildEvent.getKey());

                    return new MyArray<>(dataSnapshotRxFirebaseChildEvent.getEventType(),usersHouseInfo);
                })
                .map((myArray) -> {
                            List<MyArray<UsersHouseInfo>> arrayList = new ArrayList<>();
                            arrayList.add(myArray);
                            return arrayList;
                        })
                .scan((oldArray, newArray) -> {
                    if(newArray.get(0).getEventType() == RxFirebaseChildEvent.EventType.ADDED)
                    {
                        oldArray.add(newArray.get(0));
                    }
                    else if(newArray.get(0).getEventType() == RxFirebaseChildEvent.EventType.REMOVED) {
                        for (Integer i = 0; i < oldArray.size(); i++){
                            if(oldArray.get(i).getData().equals(newArray.get(0).getData()))
                            {
                                oldArray.remove(i);
                            }
                        }
                    }
                    else if(newArray.get(0).getEventType() == RxFirebaseChildEvent.EventType.CHANGED) {
                        for (Integer i = 0; i < oldArray.size(); i++){
                            if(oldArray.get(i).getData().equals(newArray.get(0).getData()))
                            {
                                oldArray.set(i,newArray.get(0));
                            }
                        }
                    }
                    else{
                        for (Integer i = 0; i < oldArray.size(); i++){
                            if(oldArray.get(i).getData().equals(newArray.get(0).getData()))
                            {
                                oldArray.remove(i);
                            }
                        }
                    }
                    return oldArray;
                })
                .map((childList) -> {
                            List<UsersHouseInfo> usersHouseInfoList = new ArrayList<>();
                            for(MyArray<UsersHouseInfo> myArray : childList)
                            {
                                usersHouseInfoList.add(myArray.getData());
                            }
                            return usersHouseInfoList;
                        })
                .subscribeOn(Schedulers.io());

        return Flowable.merge(emptyListFlowable,queryFlowable)
                .throttleLast(500,TimeUnit.MILLISECONDS);


    }

    public Flowable<List<RoomInfo>> getHousesRooms(String houseId) {
        Query query = FirebaseDatabase.getInstance().getReference("houses/" + houseId + "/" + "rooms");

        Flowable<List<RoomInfo>> emptyListFlowable =Flowable.just(new ArrayList<RoomInfo>());

        Flowable<List<RoomInfo>> queryFlowable = RxFirebaseDatabase.observeChildEvent(query, RoomInfo.class, BackpressureStrategy.BUFFER)
            .map(dataSnapshotRxFirebaseChildEvent -> {
                RoomInfo roomInfo = dataSnapshotRxFirebaseChildEvent.getValue();

                return new MyArray<>(dataSnapshotRxFirebaseChildEvent.getEventType(),roomInfo);
            })
                    .map((myArray) -> {
                        List<MyArray<RoomInfo>> arrayList = new ArrayList<>();
                        arrayList.add(myArray);
                        return arrayList;
                    })
                    .scan((oldArray, newArray) -> {
                        if(newArray.get(0).getEventType() == RxFirebaseChildEvent.EventType.ADDED)
                        {
                            oldArray.add(newArray.get(0));
                        }
                        else if(newArray.get(0).getEventType() == RxFirebaseChildEvent.EventType.REMOVED) {
                            for (Integer i = 0; i < oldArray.size(); i++){
                                if(oldArray.get(i).getData().equals(newArray.get(0).getData()))
                                {
                                    oldArray.remove(i);
                                }
                            }
                        }
                        else if(newArray.get(0).getEventType() == RxFirebaseChildEvent.EventType.CHANGED) {
                            for (Integer i = 0; i < oldArray.size(); i++){
                                if(oldArray.get(i).getData().equals(newArray.get(0).getData()))
                                {
                                    oldArray.set(i,newArray.get(0));
                                }
                            }
                        }
                        else{
                            for (Integer i = 0; i < oldArray.size(); i++){
                                if(oldArray.get(i).getData().equals(newArray.get(0).getData()))
                                {
                                    oldArray.remove(i);
                                }
                            }
                        }
                        return oldArray;
                    })
                    .map((childList) -> {
                        List<RoomInfo> usersHouseInfoList = new ArrayList<>();
                        for(MyArray<RoomInfo> myArray : childList)
                        {
                            usersHouseInfoList.add(myArray.getData());
                        }
                        return usersHouseInfoList;
                    })
                    .subscribeOn(Schedulers.io());

        return Flowable.merge(emptyListFlowable,queryFlowable).throttleLast(500,TimeUnit.MILLISECONDS);
        }

    public Flowable<Room> getRoomDetails(String roomId) {

        Flowable<List<Door>> doorValueEvent = getDoors(roomId);
        Flowable<List<Light>> lightValueEvent = getLights(roomId);
        Flowable<List<Temperature>> temperatureValueEvent = getTemperatures(roomId);

        return Flowable.combineLatest(doorValueEvent,lightValueEvent,temperatureValueEvent,
                (doorList,lightList,temperatureList) ->
                {
                    Room room = new Room();
                    room.setDoorList(doorList);
                    room.setLightsList(lightList);
                    room.setTemperatureList(temperatureList);
                    return room;
                });
    }

    Flowable<List<Door>> getDoors(String roomId) {
        Query doorsQuery = FirebaseDatabase.getInstance().getReference("rooms/" + roomId + "/doors");

        Flowable<ArrayList<Door>> emptyListFlowable =Flowable.just(new ArrayList<Door>());

        Flowable<List<Door>> queryFlowable = RxFirebaseDatabase.observeChildEvent(doorsQuery,Door.class,BackpressureStrategy.BUFFER)
                .map(dataSnapshotRxFirebaseChildEvent -> {
                    Door door = dataSnapshotRxFirebaseChildEvent.getValue();

                    return new MyArray<>(dataSnapshotRxFirebaseChildEvent.getEventType(),door);
                })
                .map((myArray) -> {
                    List<MyArray<Door>> arrayList = new ArrayList<>();
                    arrayList.add(myArray);
                    return arrayList;
                })
                .scan((oldArray, newArray) -> {
                    if(newArray.get(0).getEventType() == RxFirebaseChildEvent.EventType.ADDED)
                    {
                        oldArray.add(newArray.get(0));
                    }
                    else if(newArray.get(0).getEventType() == RxFirebaseChildEvent.EventType.REMOVED) {
                        for (int i = 0; i < oldArray.size(); i++){
                            if(oldArray.get(i).getData().getDoorId() == newArray.get(0).getData().getDoorId())
                            {
                                oldArray.remove(i);
                            }
                        }
                    }
                    else if(newArray.get(0).getEventType() == RxFirebaseChildEvent.EventType.CHANGED) {
                        for (int i = 0; i < oldArray.size(); i++){
                            if(oldArray.get(i).getData().getDoorId() == newArray.get(0).getData().getDoorId())
                            {
                                oldArray.set(i,newArray.get(0));
                            }
                        }
                    }
                    else{
                        for (int i = 0; i < oldArray.size(); i++){
                            if(oldArray.get(i).getData().getDoorId() == newArray.get(0).getData().getDoorId())
                            {
                                oldArray.remove(i);
                            }
                        }
                    }
                    return oldArray;
                })
                .map((childList) -> {
                    List<Door> doorList = new ArrayList<>();
                    for(MyArray<Door> myArray : childList)
                    {
                        doorList.add(myArray.getData());
                    }
                    return doorList;
                })
                .subscribeOn(Schedulers.io());

        return Flowable.merge(emptyListFlowable,queryFlowable)
                .throttleLast(500,TimeUnit.MILLISECONDS);
    }

    Flowable<List<Light>> getLights(String roomId) {
        Query lightsQuery = FirebaseDatabase.getInstance().getReference("rooms/" + roomId + "/lights");
        Flowable<ArrayList<Light>> emptyListFlowable =Flowable.just(new ArrayList<Light>());

        Flowable<List<Light>> queryFlowable  = RxFirebaseDatabase.observeChildEvent(lightsQuery,Light.class,BackpressureStrategy.BUFFER)
                .map(dataSnapshotRxFirebaseChildEvent -> {
                    Light light = dataSnapshotRxFirebaseChildEvent.getValue();

                    return new MyArray<>(dataSnapshotRxFirebaseChildEvent.getEventType(),light);
                })
                .map((myArray) -> {
                    List<MyArray<Light>> arrayList = new ArrayList<>();
                    arrayList.add(myArray);
                    return arrayList;
                })
                .scan((oldArray, newArray) -> {
                    if(newArray.get(0).getEventType() == RxFirebaseChildEvent.EventType.ADDED)
                    {
                        oldArray.add(newArray.get(0));
                    }
                    else if(newArray.get(0).getEventType() == RxFirebaseChildEvent.EventType.REMOVED) {
                        for (int i = 0; i < oldArray.size(); i++){
                            if(oldArray.get(i).getData().getLightId() ==  newArray.get(0).getData().getLightId())
                            {
                                oldArray.remove(i);
                            }
                        }
                    }
                    else if(newArray.get(0).getEventType() == RxFirebaseChildEvent.EventType.CHANGED) {
                        for (int i = 0; i < oldArray.size(); i++){
                            if(oldArray.get(i).getData().getLightId() ==  newArray.get(0).getData().getLightId())
                            {
                                oldArray.set(i,newArray.get(0));
                            }
                        }
                    }
                    else{
                        for (int i = 0; i < oldArray.size(); i++){
                            if(oldArray.get(i).getData().getLightId() ==  newArray.get(0).getData().getLightId())
                            {
                                oldArray.remove(i);
                            }
                        }
                    }
                    return oldArray;
                })
                .map((childList) -> {
                    List<Light> doorList = new ArrayList<>();
                    for(MyArray<Light> myArray : childList)
                    {
                        doorList.add(myArray.getData());
                    }
                    return doorList;
                })
                .subscribeOn(Schedulers.io());

        return Flowable.merge(emptyListFlowable,queryFlowable)
                .throttleLast(500,TimeUnit.MILLISECONDS);

    }

    Flowable<List<Temperature>> getTemperatures(String roomId) {
        Query temperaturesQuery = FirebaseDatabase.getInstance().getReference("rooms/" + roomId + "/temperatures");

        Flowable<ArrayList<Temperature>> emptyListFlowable =Flowable.just(new ArrayList<Temperature>());


        Flowable<List<Temperature>> queryFlowable = RxFirebaseDatabase.observeChildEvent(temperaturesQuery,Temperature.class,BackpressureStrategy.BUFFER)
                .map(dataSnapshotRxFirebaseChildEvent -> {
                    Temperature temperature = dataSnapshotRxFirebaseChildEvent.getValue();

                    return new MyArray<>(dataSnapshotRxFirebaseChildEvent.getEventType(),temperature);
                })
                .map((myArray) -> {
                    List<MyArray<Temperature>> arrayList = new ArrayList<>();
                    arrayList.add(myArray);
                    return arrayList;
                })
                .scan((oldArray, newArray) -> {
                    if(newArray.get(0).getEventType() == RxFirebaseChildEvent.EventType.ADDED)
                    {
                        oldArray.add(newArray.get(0));
                    }
                    else if(newArray.get(0).getEventType() == RxFirebaseChildEvent.EventType.REMOVED) {
                        for (int i = 0; i < oldArray.size(); i++){
                            if(oldArray.get(i).getData().temperatureId == newArray.get(0).getData().temperatureId)
                            {
                                oldArray.remove(i);
                            }
                        }
                    }
                    else if(newArray.get(0).getEventType() == RxFirebaseChildEvent.EventType.CHANGED) {
                        for (int i = 0; i < oldArray.size(); i++){
                            if(oldArray.get(i).getData().temperatureId == newArray.get(0).getData().temperatureId)
                            {
                                oldArray.set(i,newArray.get(0));
                            }
                        }
                    }
                    else{
                        for (int i = 0; i < oldArray.size(); i++){
                            if(oldArray.get(i).getData().temperatureId == newArray.get(0).getData().temperatureId)
                            {
                                oldArray.remove(i);
                            }
                        }
                    }
                    return oldArray;
                })
                .map((childList) -> {
                    List<Temperature> doorList = new ArrayList<>();
                    for(MyArray<Temperature> myArray : childList)
                    {
                        doorList.add(myArray.getData());
                    }
                    return doorList;
                })
                .subscribeOn(Schedulers.io());

        return Flowable.merge(emptyListFlowable,queryFlowable)
                .throttleLast(500,TimeUnit.MILLISECONDS);
    }

}