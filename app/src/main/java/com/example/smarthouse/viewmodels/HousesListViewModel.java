package com.example.smarthouse.viewmodels;

import androidx.databinding.ObservableField;

import com.example.smarthouse.repositorys.Repository;
import com.example.smarthouse.repositorys.SharedPreferencesRepository;
import com.example.smarthouse.data.UsersHouseInfo;
import com.example.smarthouse.utils.MakeObservable;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;






public class HousesListViewModel extends AuthViewModel {

    Repository repository;

    ObservableField<String> userQuery;
    Observable<ObservableField<String>> userQueryObservable;


    @Inject
    HousesListViewModel(Repository repository, SharedPreferencesRepository shearedPrefrencesRepository) {
        super(repository, shearedPrefrencesRepository);
        this.repository = repository;
        userQuery = new ObservableField<>();
        userQueryObservable = MakeObservable.makeObservebleForString(userQuery);
    }



    public Observable<List<UsersHouseInfo>> getUsersHousesObservable()
    {

        Observable<List<UsersHouseInfo>> newUsersHausesObservable = getUsernameObservable()
                .switchMap((username) -> repository.getUsersHouses(username).toObservable());

        return Observable
                .combineLatest(newUsersHausesObservable,userQueryObservable, (userHouseList,queryString) -> {
                    List<UsersHouseInfo> newList = new ArrayList<>();
                    if(queryString.get() != null && !queryString.get().equals("")) {
                        for (UsersHouseInfo usersHauses : userHouseList) {
                            if (usersHauses.getName().contains(queryString.get())) {
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
}
