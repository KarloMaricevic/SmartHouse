package com.example.smarthouse.di.LogInFragment;

import androidx.lifecycle.ViewModel;

import com.example.smarthouse.di.ViewModelKey;
import com.example.smarthouse.viewmodels.LogInViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

//ovo je za injektanje konkretnog ViewModela
//za svaki kontkretni radiš novi binding
@Module
public abstract class LogInFragmentViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LogInViewModel.class)

    public abstract ViewModel bindViewModel(LogInViewModel logInViewModel);
}
