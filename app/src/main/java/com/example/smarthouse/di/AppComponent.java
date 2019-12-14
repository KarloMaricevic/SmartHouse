package com.example.smarthouse.di;

import android.app.Application;
import android.content.Context;

import com.example.smarthouse.di.AuthSubcomponent.AuthSubcomponent;
import com.example.smarthouse.di.AuthSubcomponent.AuthSubcomponentFactory;
import com.example.smarthouse.di.LogInSubcomponent.LogInSubcomponent;
import com.example.smarthouse.di.LogInSubcomponent.LogInSubcomponentFactory;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;


@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        AuthSubcomponentFactory.class,
        LogInSubcomponentFactory.class
})
public interface AppComponent{

        AuthSubcomponent.Factory getAuthComponentFactory();
        LogInSubcomponent.Factory getLogInSubcomponentFacotry();

        @Component.Factory
        interface Factory {
            AppComponent create(@BindsInstance Application application, @BindsInstance Context appContex);
        }

    }
