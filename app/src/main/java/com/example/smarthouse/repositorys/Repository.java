package com.example.smarthouse.repositorys;

import android.app.Application;
import android.graphics.Bitmap;


import com.example.smarthouse.data.roomData.Door;
import com.example.smarthouse.data.roomData.Light;
import com.example.smarthouse.data.roomData.Room;
import com.example.smarthouse.data.roomData.RoomInfo;
import com.example.smarthouse.data.User;
import com.example.smarthouse.data.UsersHouseInfo;
import com.example.smarthouse.data.roomData.Temperature;
import com.example.smarthouse.repositorys.utilsData.MyArray;
import com.example.smarthouse.utils.StringHasher;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;



import java.io.ByteArrayOutputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import durdinapps.rxfirebase2.RxFirebaseChildEvent;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import durdinapps.rxfirebase2.RxFirebaseStorage;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;

import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;

import io.reactivex.functions.Cancellable;
import io.reactivex.schedulers.Schedulers;


@Singleton
public class Repository {

    @Inject
    public Repository(Application application) {
        FirebaseApp.initializeApp(application.getApplicationContext());

    }

    public Maybe<String> getPassword(@NonNull String username) {
        final Query query = FirebaseDatabase.getInstance().getReference("users/").orderByChild("username").equalTo(username);
        return RxFirebaseDatabase.observeSingleValueEvent(query,
                (dataSnapshot -> {
                    for (DataSnapshot dataSnapshotUser : dataSnapshot.getChildren()) {
                        User user = dataSnapshotUser.getValue(User.class);
                        return user.getPassword();
                    }
                    return "";
                })
        ).subscribeOn(Schedulers.io()).cache();

    }

    public Flowable<String> getUsersCredencilas(@NonNull String username) {
        final Query query = FirebaseDatabase.getInstance().getReference("users/").orderByChild("username").equalTo(username);
        return RxFirebaseDatabase.observeValueEvent(query, (dataSnapshot -> {
                    for (DataSnapshot dataSnapshotUser : dataSnapshot.getChildren()) {
                        User user = dataSnapshotUser.getValue(User.class);
                        return user.getPassword();
                    }
                    return "";
                })
        ).subscribeOn(Schedulers.io());
    }

    public Flowable getUserHouses(@NonNull String username) {
        final Query query = FirebaseDatabase.getInstance().getReference("usersHauses/" + username);

        final Flowable<List<UsersHouseInfo>> emptyListFlowable =Flowable.just(new ArrayList<>());

        final Flowable<List<UsersHouseInfo>> queryFlowable = RxFirebaseDatabase.observeChildEvent(query,UsersHouseInfo.class,BackpressureStrategy.BUFFER)
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
                        for (int i = 0; i < oldArray.size(); i++){
                            if(oldArray.get(i).getData().compereById(newArray.get(0).getData()))
                            {
                                oldArray.remove(oldArray.get(i));
                            }
                        }
                    }
                    else if(newArray.get(0).getEventType() == RxFirebaseChildEvent.EventType.CHANGED) {
                        for (int i = 0; i < oldArray.size(); i++){
                            if(oldArray.get(i).getData().compereById(newArray.get(0).getData()))
                            {
                                oldArray.set(i,newArray.get(0));
                            }
                        }
                    }
                    else{
                        for (int i = 0; i < oldArray.size(); i++){
                            if(oldArray.get(i).getData().compereById(newArray.get(0).getData()))
                            {
                                oldArray.remove(oldArray.get(i));
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

    public Flowable<List<RoomInfo>> getHousesRooms(@NonNull String houseId) {
        final Query query = FirebaseDatabase.getInstance().getReference("houses/" + houseId + "/" + "rooms");

        final Flowable<List<RoomInfo>> emptyListFlowable =Flowable.just(new ArrayList<>());

        final Flowable<List<RoomInfo>> queryFlowable = RxFirebaseDatabase.observeChildEvent(query, RoomInfo.class, BackpressureStrategy.BUFFER)
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
                            for (int i = 0; i < oldArray.size(); i++){
                                if(oldArray.get(i).getData().equals(newArray.get(0).getData()))
                                {
                                    oldArray.remove(i);
                                }
                            }
                        }
                        else if(newArray.get(0).getEventType() == RxFirebaseChildEvent.EventType.CHANGED) {
                            for (int i = 0; i < oldArray.size(); i++){
                                if(oldArray.get(i).getData().equals(newArray.get(0).getData()))
                                {
                                    oldArray.set(i,newArray.get(0));
                                }
                            }
                        }
                        else{
                            for (int i = 0; i < oldArray.size(); i++){
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

    public Flowable<Room> getRoomDetails(@NonNull String roomId) {

        final Flowable<List<Door>> doorValueEvent = getDoors(roomId);
        final Flowable<List<Light>> lightValueEvent = getLights(roomId);
        final Flowable<List<Temperature>> temperatureValueEvent = getTemperatures(roomId);

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

    private Flowable<List<Door>> getDoors(@NonNull String roomId) {
        final Query doorsQuery = FirebaseDatabase.getInstance().getReference("rooms/" + roomId + "/doors");

        final Flowable<ArrayList<Door>> emptyListFlowable =Flowable.just(new ArrayList<>());

        final Flowable<List<Door>> queryFlowable = RxFirebaseDatabase.observeChildEvent(doorsQuery,Door.class,BackpressureStrategy.BUFFER)
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
                            if(oldArray.get(i).getData().compereById(newArray.get(0).getData()))
                            {
                                oldArray.remove(i);
                            }
                        }
                    }
                    else if(newArray.get(0).getEventType() == RxFirebaseChildEvent.EventType.CHANGED) {
                        for (int i = 0; i < oldArray.size(); i++){
                            if(oldArray.get(i).getData().compereById(newArray.get(0).getData()))
                            {
                                oldArray.set(i,newArray.get(0));
                            }
                        }
                    }
                    else{
                        for (int i = 0; i < oldArray.size(); i++){
                            if(oldArray.get(i).getData().compereById(newArray.get(0).getData()))
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

    private Flowable<List<Light>> getLights(@NonNull String roomId) {
        final Query lightsQuery = FirebaseDatabase.getInstance().getReference("rooms/" + roomId + "/lights");
        final Flowable<ArrayList<Light>> emptyListFlowable =Flowable.just(new ArrayList<>());

        final Flowable<List<Light>> queryFlowable  = RxFirebaseDatabase.observeChildEvent(lightsQuery,Light.class,BackpressureStrategy.BUFFER)
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
                            if(oldArray.get(i).getData().compereById(newArray.get(0).getData()))
                            {
                                oldArray.remove(i);
                            }
                        }
                    }
                    else if(newArray.get(0).getEventType() == RxFirebaseChildEvent.EventType.CHANGED) {
                        for (int i = 0; i < oldArray.size(); i++){
                            if(oldArray.get(i).getData().compereById(newArray.get(0).getData()))
                            {
                                oldArray.set(i,newArray.get(0));
                            }
                        }
                    }
                    else{
                        for (int i = 0; i < oldArray.size(); i++){
                            if(oldArray.get(i).getData().compereById(newArray.get(0).getData()))
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

    private Flowable<List<Temperature>> getTemperatures(@NonNull String roomId) {
        final Query temperaturesQuery = FirebaseDatabase.getInstance().getReference("rooms/" + roomId + "/temperatures");

        final Flowable<ArrayList<Temperature>> emptyListFlowable =Flowable.just(new ArrayList<>());


        final Flowable<List<Temperature>> queryFlowable = RxFirebaseDatabase.observeChildEvent(temperaturesQuery,Temperature.class,BackpressureStrategy.BUFFER)
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
                            if(oldArray.get(i).getData().compereById(newArray.get(0).getData()))
                            {
                                oldArray.remove(i);
                            }
                        }
                    }
                    else if(newArray.get(0).getEventType() == RxFirebaseChildEvent.EventType.CHANGED) {
                        for (int i = 0; i < oldArray.size(); i++){
                            if(oldArray.get(i).getData().compereById(newArray.get(0).getData()))
                            {
                                oldArray.set(i,newArray.get(0));
                            }
                        }
                    }
                    else{
                        for (int i = 0; i < oldArray.size(); i++){
                            if(oldArray.get(i).getData().compereById(newArray.get(0).getData()))
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

    public Single<String> changeHousePicture(@NonNull Bitmap picture,@NonNull String username,@NonNull String houseId) {

        final String hashedUsername = StringHasher.md5(username);
        final String hashedhouseId = StringHasher.md5(houseId);


        final StorageReference storageRef = FirebaseStorage.getInstance().getReference(hashedUsername+hashedhouseId);
        return Single.create(new SingleOnSubscribe<UploadTask.TaskSnapshot>() {
                @Override
                public void subscribe(SingleEmitter<UploadTask.TaskSnapshot> emitter) throws Exception {
                ByteArrayOutputStream outputBytes = new ByteArrayOutputStream();
                picture.compress(Bitmap.CompressFormat.JPEG, 100, outputBytes);
                byte[] bytes = outputBytes.toByteArray();
                final StorageTask<UploadTask.TaskSnapshot> taskSnapshotStorageTask =
                        storageRef.putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                emitter.onSuccess(taskSnapshot);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if (!emitter.isDisposed())
                                    emitter.onError(e);
                            }
                        });

                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        taskSnapshotStorageTask.cancel();
                    }
                });
            }
                })
            .flatMap((taskSnapshot) -> {
                    return RxFirebaseStorage.getDownloadUrl(FirebaseStorage.getInstance().getReference(taskSnapshot.getMetadata().getName())).toSingle();
                })
                .flatMap((uri) -> {
                            Map<String,Object> map = new HashMap<>();
                            map.put("picture_url",uri.toString());
                            return RxFirebaseDatabase.updateChildren(FirebaseDatabase.getInstance().getReference("usersHauses/" + username + "/" + houseId + "/"),map).toSingle(() -> "Sucecess");
                        });
    }

    public Completable changeHouseName(@NonNull String newName,@NonNull String username,@NonNull String houseId) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("usersHauses/" + username + "/" + houseId + "/");
        final Map<String,Object> map = new HashMap<>();
        map.put("name",newName);
        return RxFirebaseDatabase.updateChildren(databaseReference,map).subscribeOn(Schedulers.io());
    }

    public Completable changeTargetedTemperature(@NonNull String roomId,@NonNull String temperatureId,@NonNull int targeted){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("rooms/"+ roomId + "/" + "temperatures/" + temperatureId + "/");
        final Map<String,Object> map = new HashMap<>();
        map.put("targeted",targeted);
        return RxFirebaseDatabase.updateChildren(databaseReference,map).subscribeOn(Schedulers.io());

    }

    public Completable changeLight(@NonNull String roomId,@NonNull String lightId,@NonNull boolean targeted){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("rooms/"+ roomId + "/" + "lights/" + lightId + "/");
        final Map<String,Object> map = new HashMap<>();
        map.put("targeted",targeted);
        return RxFirebaseDatabase.updateChildren(databaseReference,map).subscribeOn(Schedulers.io());
    }

    public Completable changeDoorLock(@NonNull String roomId,@NonNull String doorId,@NonNull boolean targeted) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("rooms/"+ roomId + "/" + "doors/" + doorId + "/");
        final Map<String,Object> map = new HashMap<>();
        map.put("targeted",targeted);
        return RxFirebaseDatabase.updateChildren(databaseReference,map).subscribeOn(Schedulers.io());
    }

}