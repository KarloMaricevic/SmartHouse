package com.example.smarthouse.viewmodels;

import androidx.databinding.ObservableField;

import com.example.smarthouse.Repository;
import com.example.smarthouse.data.UsersHouseInfo;
import com.example.smarthouse.utils.MakeObservable;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class HousesListFragmentViewModel extends AuthViewModel {

    ObservableField<String> userQuery;
    Observable<ObservableField<String>> userQueryObservable;

    @Inject
    public HousesListFragmentViewModel(Repository repository) {
        super(repository);
        userQuery = new ObservableField<>();
        userQueryObservable = MakeObservable.makeObservebleForString(userQuery);
    }


    public Observable<List<UsersHouseInfo>> getUsersHousesFlowable()
    {

        Observable<List<UsersHouseInfo>> usersHausesObservable = usernameObservable.concatMap(
                (item) ->
                {
                    return repository.getUsersHouses(item.get()).toObservable();
                }
        );

        return Observable.combineLatest(usersHausesObservable,userQueryObservable,
                (userHouseList,queryString) ->
                {
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
                }

        );
    }


    public String getUserQuery() {
        return userQuery.get();
    }

    public void setUserQuery(String userQuery) {
        this.userQuery.set(userQuery);
    }
}



