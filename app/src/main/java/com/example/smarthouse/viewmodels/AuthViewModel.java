package com.example.smarthouse.viewmodels;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.example.smarthouse.repositorys.Repository;
import com.example.smarthouse.repositorys.SharedPreferencesRepository;


import java.util.HashMap;
import io.reactivex.Observable;

public abstract class AuthViewModel extends BaseViewModel {


    public enum AuthenticationState {
        UNAUTHENTICATED,
        AUTHENTICATED,
    }


    Observable<String> usernameObservable;
    Observable<String> passwordObservable;
    Observable<String> dbPassword;
    Observable<AuthViewModel.AuthenticationState> authState;

    AuthViewModel(Repository repository, SharedPreferencesRepository shearedPrefrencesRepository)
    {
        usernameObservable = shearedPrefrencesRepository.getUseranmeObservable();
        passwordObservable = shearedPrefrencesRepository.getPasswordObservable();

        dbPassword = usernameObservable
                .switchMap((useranme) -> repository.getUsersCredencilas(useranme).toObservable());

        authState = Observable
                .combineLatest(usernameObservable,passwordObservable,dbPassword, (username,password,dbPassword) -> {
                    HashMap<String, String> combinedMap = new HashMap<String, String>();
                    combinedMap.put("username", username);
                    combinedMap.put("password", password);
                    combinedMap.put("dbPassword", dbPassword);
                    return combinedMap; })
                .map((hashMap) -> {
                            for(String value : hashMap.values())
                            {
                                if(value.equals(""))
                                {
                                    return AuthViewModel.AuthenticationState.UNAUTHENTICATED;
                                }

                            }
                            if (hashMap.get("password").equals(hashMap.get("dbPassword")))
                            {
                                return AuthViewModel.AuthenticationState.AUTHENTICATED;
                            }
                            else
                            {
                                return AuthViewModel.AuthenticationState.UNAUTHENTICATED;
                            }
                        });
    }

    public Observable<AuthViewModel.AuthenticationState> getAuthState() {
        return authState;
    }

    public Observable<String> getUsernameObservable() {
        return usernameObservable;
    }
}