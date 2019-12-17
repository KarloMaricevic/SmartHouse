package com.example.smarthouse.repositorys;

import android.app.Application;

import com.example.smarthouse.data.roomData.Door;
import com.example.smarthouse.data.roomData.Light;
import com.example.smarthouse.data.roomData.Room;
import com.example.smarthouse.data.roomData.RoomInfo;
import com.example.smarthouse.data.User;
import com.example.smarthouse.data.UsersHouseInfo;
import com.example.smarthouse.data.roomData.Temperature;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
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


    public Flowable<List<UsersHouseInfo>> getUsersHouses(String username){
        Query query = FirebaseDatabase.getInstance().getReference("usersHauses/" + username);
        Flowable flowable = RxFirebaseDatabase.observeValueEvent(query,(dataSnapshot) -> {
                    List<UsersHouseInfo> usersHausesList = new ArrayList<>();

                    for(DataSnapshot userHousesDS : dataSnapshot.getChildren())
                    {
                        UsersHouseInfo usersHouseInfo = userHousesDS.getValue(UsersHouseInfo.class);
                        usersHouseInfo.setHauseId(userHousesDS.getKey());

                        usersHausesList.add(usersHouseInfo);
                    }
                    return usersHausesList;
                }
        ).subscribeOn(Schedulers.io());
        return flowable;
    }

    public Flowable<List<RoomInfo>> getHousesRooms(String houseId)
    {
        Query query = FirebaseDatabase.getInstance().getReference("houses/" +houseId +"/" + "rooms");
            return RxFirebaseDatabase.observeValueEvent(query, (dataSnapshot) -> {
                    List<RoomInfo> roomInfoList = new ArrayList<>();
                    for(DataSnapshot roomsInfoDS : dataSnapshot.getChildren())
                    {
                        RoomInfo roomInfo = roomsInfoDS.getValue(RoomInfo.class);
                        roomInfoList.add(roomInfo);
                    }
                    return roomInfoList;
            })
                    .subscribeOn(Schedulers.io());
    }

    public Flowable<Room> getRoomDetails(String roomId)
    {
        Query doorsQuery = FirebaseDatabase.getInstance().getReference("rooms/" + roomId + "/doors");
        Query lightsQuery = FirebaseDatabase.getInstance().getReference("rooms/" + roomId + "/lights");
        Query temperaturesQuery = FirebaseDatabase.getInstance().getReference("rooms/" + roomId + "/temperatures");



        Flowable<List<Door>> doorValueEvent = RxFirebaseDatabase
                .observeValueEvent(doorsQuery,(dataSnapshot) ->
                    {
                        List<Door> doorList = new ArrayList<>();
                            for (DataSnapshot doorDS : dataSnapshot.getChildren()) {
                                doorList.add(doorDS.getValue(Door.class));
                            }
                        return doorList;
                })
                .subscribeOn(Schedulers.io());

        Flowable<List<Light>> lightValueEvent = RxFirebaseDatabase.observeValueEvent(lightsQuery, (dataSnapshot) ->
                {
                    List<Light> lightList = new ArrayList<>();
                    for (DataSnapshot lightDS : dataSnapshot.getChildren()) {
                        lightList.add(lightDS.getValue(Light.class));
                    }
                    return lightList;
                })
        .subscribeOn(Schedulers.io());


        Flowable<List<Temperature>> temperatureValueEvent =
                RxFirebaseDatabase.observeValueEvent(temperaturesQuery,(dataSnapshot) ->
                {
                    List<Temperature> temperatureList = new ArrayList<>();
                    for(DataSnapshot temperatureDS : dataSnapshot.getChildren())
                    {
                        temperatureList.add(temperatureDS.getValue(Temperature.class));
                    }
                    return temperatureList;
                }).subscribeOn(Schedulers.io());


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





}