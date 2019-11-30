package com.example.smarthouse.di;

//App lvl aplication dependenys,ono što će zbilja biti singelton


import android.app.Application;

import com.example.smarthouse.Repository;

import javax.inject.Singleton;

//tu se metode anotiraju sa @Singleton

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    @Singleton
    Repository provideRepository(Application application)
    {
        return new Repository(application);
    }

}
