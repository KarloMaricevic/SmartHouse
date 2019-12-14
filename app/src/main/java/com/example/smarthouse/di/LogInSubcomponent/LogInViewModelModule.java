package com.example.smarthouse.di.LogInSubcomponent;

import androidx.lifecycle.ViewModel;

import com.example.smarthouse.di.ViewModelKey;
import com.example.smarthouse.viewmodels.LogInViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class LogInViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LogInViewModel.class)
    public abstract ViewModel bindViewModel(LogInViewModel logInViewModel);
}
