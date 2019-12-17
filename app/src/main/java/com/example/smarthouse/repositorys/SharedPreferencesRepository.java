package com.example.smarthouse.repositorys;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.smarthouse.R;
import com.example.smarthouse.di.Scopes.PerAuthComponent;

import javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;


@PerAuthComponent
public class SharedPreferencesRepository {


    Context appContext;
    private SharedPreferences preferences;


    @Inject
    SharedPreferencesRepository(Context appContext) {
        this.appContext = appContext;
        preferences = appContext.getSharedPreferences(appContext.getString(R.string.sharedPreferencesName), Context.MODE_PRIVATE);
    }


    public Observable<String> getUseranmeObservable() {
        return Observable
                .create((emitter -> {
                    emitter.onNext(appContext.getString(R.string.username));

                    final SharedPreferences.OnSharedPreferenceChangeListener listener =
                            (sharedPreferences, key) -> emitter.onNext(key);

                    emitter.setCancellable(
                            () -> preferences.unregisterOnSharedPreferenceChangeListener(listener)
                    );

                    preferences.registerOnSharedPreferenceChangeListener(listener);
                }))
                .filter((key) ->key.equals(appContext.getString(R.string.username)))
                .map((key) -> preferences.getString(appContext.getString(R.string.username), ""))
                .subscribeOn(Schedulers.io());
    }


    public Observable<String> getPasswordObservable() {
        return Observable
                .create((emitter -> {
                    emitter.onNext(appContext.getString(R.string.password));

                    final SharedPreferences.OnSharedPreferenceChangeListener listener =
                            (sharedPreferences, key) -> emitter.onNext(key);

                    emitter.setCancellable(
                            () -> preferences.unregisterOnSharedPreferenceChangeListener(listener)
                    );

                    preferences.registerOnSharedPreferenceChangeListener(listener);
                }))
                .filter((key) -> key.equals(appContext.getString(R.string.password)))
                .map((key) -> preferences.getString(appContext.getString(R.string.password), ""))
                .subscribeOn(Schedulers.io());

    }
}
