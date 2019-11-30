package com.example.smarthouse;

import android.app.Application;

import com.example.smarthouse.data.User;
import com.example.smarthouse.data.UsersHouseInfo;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;

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
                        usersHausesList.add(userHousesDS.getValue(UsersHouseInfo.class));
                    }
                    return usersHausesList;
                }
        ).subscribeOn(Schedulers.io());

        return flowable;
    }

}