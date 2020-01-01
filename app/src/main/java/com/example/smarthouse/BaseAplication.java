package com.example.smarthouse;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraX;
import androidx.camera.core.CameraXConfig;
import androidx.camera.core.ImageCaptureConfig;

import com.example.smarthouse.di.AppComponent;
import com.example.smarthouse.di.AuthSubcomponent.AuthSubcomponent;
import com.example.smarthouse.di.DaggerAppComponent;
import androidx.camera.camera2.Camera2Config;



public class BaseAplication extends Application implements CameraXConfig.Provider {

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

    @NonNull
    @Override
    public CameraXConfig getCameraXConfig() {
        return Camera2Config.defaultConfig();
    }
}