package com.example.smarthouse;

import android.app.Application;

import com.example.smarthouse.di.AppComponent;
import com.example.smarthouse.di.AuthSubcomponent.AuthSubcomponent;
import com.example.smarthouse.di.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class BaseAplication extends Application {

    AppComponent applicationComponent;
    AuthSubcomponent authCompoent;


    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerAppComponent.factory().create(this,getApplicationContext());
    }

    public AppComponent getApplicationComponent() {
        return applicationComponent;
    }

    public AuthSubcomponent getAuthCompoent() {
        if (authCompoent == null) {
            authCompoent = applicationComponent.getAuthComponentFactory().create();
        }
        return authCompoent;
    }

    public void releseAuthComponent()
    {
        authCompoent = null;
    }
}