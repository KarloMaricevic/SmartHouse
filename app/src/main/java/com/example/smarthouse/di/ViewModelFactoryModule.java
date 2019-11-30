package com.example.smarthouse.di;


import androidx.lifecycle.ViewModelProvider;

import com.example.smarthouse.viewmodels.ViewModelProviderFactory;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.BindsInstance;
import dagger.Module;

//ovo je dependeny za VIewModelProviderFactory

@Module
public abstract class ViewModelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory viewModelProviderFactory);

}
