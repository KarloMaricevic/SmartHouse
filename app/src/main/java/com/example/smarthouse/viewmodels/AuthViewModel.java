package com.example.smarthouse.viewmodels;

import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.smarthouse.Repository;
import com.example.smarthouse.utils.MakeObservable;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public abstract class AuthViewModel extends ViewModel {

    protected Repository repository;


    public enum AuthenticationState {
        UNAUTHENTICATED,        // Initial state, the user needs to authenticate
        AUTHENTICATED,          // The user has authenticated successfully
    }


    private ObservableField<AuthenticationState> authenticationState;
    private Observable<AuthenticationState> authenticationStateObservable;


    protected ObservableField<String> useraname;
    protected ObservableField<String> password;
    private ObservableField<String> dbPassword;

    protected Observable<ObservableField<String>> usernameObservable;

    public AuthViewModel(Repository repository) {
        this.repository = repository;

        useraname = new ObservableField<>("");
        password = new ObservableField<>("");
        dbPassword = new ObservableField<>();
        authenticationState = new ObservableField<>();

        setUpObservables();

    }

    private void setUpObservables()
    {


        authenticationStateObservable = Observable.create((emiter) -> {
            if(authenticationState.get() != null) {
                emiter.onNext(authenticationState.get());
            }

            final androidx.databinding.Observable.OnPropertyChangedCallback callback = new androidx.databinding.Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(androidx.databinding.Observable sender, int propertyId) {
                    if(authenticationState.get() != null) {
                        emiter.onNext(authenticationState.get());
                    }
                }
            };
            authenticationState.addOnPropertyChangedCallback(callback);

            emiter.setCancellable(() -> authenticationState.removeOnPropertyChangedCallback(callback));
        });




        usernameObservable = MakeObservable.makeObservebleForString(useraname).skip(1).cacheWithInitialCapacity(1);
        Observable<ObservableField<String>> passwordObservable = MakeObservable.makeObservebleForString(password).skip(1);
        Observable<ObservableField<String>> dbPasswordObservalbe = MakeObservable.makeObservebleForString(dbPassword).skip(1);



        usernameObservable
                .switchMap(item -> {
                    return repository.getUsersCredencilas(item.get()).toObservable();
                })
                .observeOn(Schedulers.computation())
                .subscribe(
                        (item) -> dbPassword.set(item),
                        (e) -> Log.e("dbError:", e.getMessage())
                );


        Observable<Map<String, String>> combined = Observable
                .combineLatest(usernameObservable, passwordObservable, dbPasswordObservalbe, (useranameObs, passwordObs, dbPasswordObs) -> {
                    HashMap<String, String> combinedMap = new HashMap<String, String>();
                    combinedMap.put("username", useranameObs.get());
                    combinedMap.put("password", passwordObs.get());
                    combinedMap.put("dbPassword", dbPasswordObs.get());
                    return combinedMap;
                });


        combined.subscribe(
                (item) -> {
                    for (String value : item.values()) {
                        if (value == null) {
                            authenticationState.set(AuthenticationState.UNAUTHENTICATED);
                            return;
                        }
                    }
                    if (item.get("password").equals(item.get("dbPassword"))) {
                        authenticationState.set(AuthenticationState.AUTHENTICATED);
                    } else {
                        authenticationState.set(AuthenticationState.UNAUTHENTICATED);
                    }
                },

                (e) -> { },
                () -> authenticationState.set(AuthenticationState.UNAUTHENTICATED)
        );

    }


    public void setUseraname(String useraname) {
        this.useraname.set(useraname);
    }

    public void setPassword(String password) {
        this.password.set(password);
    }


    public Observable<AuthenticationState> getAuthenticationStateObservable() {
        return authenticationStateObservable;
    }
}