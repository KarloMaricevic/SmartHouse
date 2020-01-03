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


    Context mAppContext;
    private SharedPreferences mPreferences;


    @Inject
    SharedPreferencesRepository(Context appContext) {
        this.mAppContext = appContext;
        mPreferences = appContext.getSharedPreferences(appContext.getString(R.string.sharedPreferencesName), Context.MODE_PRIVATE);
    }


    public Observable<String> getUseranmeObservable() {
        return Observable
                .create((emitter -> {
                    emitter.onNext(mAppContext.getString(R.string.username));

                    final SharedPreferences.OnSharedPreferenceChangeListener listener =
                            (sharedPreferences, key) -> emitter.onNext(key);

                    emitter.setCancellable(
                            () -> mPreferences.unregisterOnSharedPreferenceChangeListener(listener)
                    );

                    mPreferences.registerOnSharedPreferenceChangeListener(listener);
                }))
                .filter((key) ->key.equals(mAppContext.getString(R.string.username)))
                .map((key) -> mPreferences.getString(mAppContext.getString(R.string.username), ""))
                .subscribeOn(Schedulers.io());
    }


    public Observable<String> getPasswordObservable() {
        return Observable
                .create((emitter -> {
                    emitter.onNext(mAppContext.getString(R.string.password));

                    final SharedPreferences.OnSharedPreferenceChangeListener listener =
                            (sharedPreferences, key) -> emitter.onNext(key);

                    emitter.setCancellable(
                            () -> mPreferences.unregisterOnSharedPreferenceChangeListener(listener)
                    );

                    mPreferences.registerOnSharedPreferenceChangeListener(listener);
                }))
                .filter((key) -> key.equals(mAppContext.getString(R.string.password)))
                .map((key) -> mPreferences.getString(mAppContext.getString(R.string.password), ""))
                .subscribeOn(Schedulers.io());
    }

    public void deleteStoredKeys(){
        mPreferences.edit().clear().commit();
    }
}
