package com.example.smarthouse.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.smarthouse.repositorys.Repository;
import com.example.smarthouse.repositorys.SharedPreferencesRepository;


import java.util.HashMap;
import io.reactivex.Observable;

public abstract class AuthViewModel extends ViewModel {


    public enum AuthenticationState {
        UNAUTHENTICATED,
        AUTHENTICATED,
    }

    private SharedPreferencesRepository mSharedPreferencesRepository;

    protected String mCurrentUsername;

    Observable<String> mUsernameObservable;
    Observable<String> mPasswordObservable;
    Observable<String> mDbPassword;
    Observable<AuthViewModel.AuthenticationState> mAuthState;

    AuthViewModel(Repository repository, SharedPreferencesRepository shearedPrefrencesRepository)
    {
        mUsernameObservable = shearedPrefrencesRepository.getUseranmeObservable();
        mPasswordObservable = shearedPrefrencesRepository.getPasswordObservable();
        mSharedPreferencesRepository = shearedPrefrencesRepository;

        mUsernameObservable.subscribe(
                (username) -> {
                    this.mCurrentUsername = username;
                },
                (e) -> {}
        );

        mDbPassword = mUsernameObservable
                .switchMap((useranme) -> repository.getUsersCredencilas(useranme).toObservable());

        mAuthState = Observable
                .combineLatest(mUsernameObservable, mPasswordObservable, mDbPassword, (username, password, dbPassword) -> {
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

    public Observable<AuthViewModel.AuthenticationState> getmAuthState() {
        return mAuthState;
    }

    public Observable<String> getmUsernameObservable() {
        return mUsernameObservable;
    }

    public SharedPreferencesRepository getSharedPreferencesRepository() {
        return mSharedPreferencesRepository;
    }
}