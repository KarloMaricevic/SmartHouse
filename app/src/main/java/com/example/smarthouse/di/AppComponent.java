package com.example.smarthouse.di;

import android.app.Application;


import com.example.smarthouse.BaseAplication;
import com.example.smarthouse.di.HousesListFragment.HousesListFragmentBuildersModule;
import com.example.smarthouse.di.LogInFragment.LogInFragmentBuildersModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;


@Singleton
@Component(modules = {AndroidSupportInjectionModule.class,
                        AppModule.class,
                        LogInFragmentBuildersModule.class,
                        ViewModelFactoryModule.class,
                        HousesListFragmentBuildersModule.class
})
public interface AppComponent extends AndroidInjector<BaseAplication> {


    @Component.Builder
        interface  Builder{

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
