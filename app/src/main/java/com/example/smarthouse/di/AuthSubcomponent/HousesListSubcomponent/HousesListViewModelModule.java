package com.example.smarthouse.di.AuthSubcomponent.HousesListSubcomponent;


import androidx.lifecycle.ViewModel;

import com.example.smarthouse.di.ViewModelKey;
import com.example.smarthouse.viewmodels.HousesListViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class HousesListViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(HousesListViewModel.class)
    public abstract ViewModel bindViewModel(HousesListViewModel housesListFragmentViewModel);
}
