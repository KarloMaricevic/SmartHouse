package com.example.smarthouse.viewmodels;

import android.graphics.Bitmap;

import androidx.databinding.ObservableField;

import com.example.smarthouse.repositorys.Repository;
import com.example.smarthouse.repositorys.SharedPreferencesRepository;
import com.example.smarthouse.data.UsersHouseInfo;
import com.example.smarthouse.utils.MakeObservable;
import com.example.smarthouse.utils.PictureResize;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;


public class HousesListViewModel extends AuthViewModel {

    Repository repository;

    ObservableField<String> userQuery;
    Observable<String> userQueryObservable;


    @Inject
    HousesListViewModel(Repository repository, SharedPreferencesRepository shearedPrefrencesRepository) {
        super(repository, shearedPrefrencesRepository);
        this.repository = repository;
        userQuery = new ObservableField<>("");
        userQueryObservable = MakeObservable.makeObservebleForString(userQuery);
    }



    public Observable<List<UsersHouseInfo>> getUsersHousesObservable()
    {

        Observable<List<UsersHouseInfo>> newUsersHausesObservable = getUsernameObservable()
                .switchMap((username) -> {
                    return repository.getUserHouses(username).toObservable();
                });

        return Observable
                .combineLatest(newUsersHausesObservable,userQueryObservable, (userHouseList,queryString) -> {
                    List<UsersHouseInfo> newList = new ArrayList<>();
                    if(queryString != null && !queryString.equals("")) {
                        for (UsersHouseInfo usersHauses : userHouseList) {
                            if (usersHauses.getName().toLowerCase().contains(queryString.toLowerCase())) {
                                newList.add(usersHauses);
                            }
                        }
                        return newList;
                    }
                    else
                    {
                        return userHouseList;
                    }
                });
    }


    public String getUserQuery() {
        return userQuery.get();
    }

    public void setUserQuery(String userQuery) {
        this.userQuery.set(userQuery);
    }


    public Single<String> saveBitmapToDatabase(Bitmap bitmap, String houseId)
    {
        Bitmap resizedBitmap = PictureResize.getResizedBitmap(bitmap, 800, 533);
        return repository.changeHousePicture(resizedBitmap,currentUsername,houseId);
    }


    public Completable changeHouseName(String newName, String houseId)
    {
       return repository.changeHouseName(newName,currentUsername,houseId);
    }
}
