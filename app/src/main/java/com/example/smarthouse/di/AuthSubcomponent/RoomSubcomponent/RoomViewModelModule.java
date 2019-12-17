package com.example.smarthouse.di.AuthSubcomponent.RoomSubcomponent;

import androidx.lifecycle.ViewModel;

import com.example.smarthouse.di.ViewModelKey;
import com.example.smarthouse.viewmodels.RoomViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;


@Module
public abstract class RoomViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(RoomViewModel.class)
    public abstract ViewModel bindViewModel(RoomViewModel roomViewModel);
}
