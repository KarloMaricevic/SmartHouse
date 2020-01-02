package com.example.smarthouse;

import android.app.Application;


import com.example.smarthouse.di.AppComponent;
import com.example.smarthouse.di.AuthSubcomponent.AuthSubcomponent;
import com.example.smarthouse.di.DaggerAppComponent;



public class BaseAplication extends Application {

    AppComponent mApplicationComponent;
    AuthSubcomponent mAuthCompoent;


    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent = DaggerAppComponent.factory().create(this,getApplicationContext());
    }

    public AppComponent getmApplicationComponent() {
        return mApplicationComponent;
    }

    public AuthSubcomponent getmAuthCompoent() {
        if (mAuthCompoent == null) {
            mAuthCompoent = mApplicationComponent.getAuthComponentFactory().create();
        }
        return mAuthCompoent;
    }

    public void releseAuthComponent()
    {
        mAuthCompoent = null;
    }


}