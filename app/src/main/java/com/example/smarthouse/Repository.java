package com.example.smarthouse;

import android.app.Application;

import com.example.smarthouse.data.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import javax.inject.Inject;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
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
}