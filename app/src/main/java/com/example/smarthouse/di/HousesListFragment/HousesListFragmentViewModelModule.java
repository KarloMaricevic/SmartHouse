package com.example.smarthouse.di.HousesListFragment;

import androidx.lifecycle.ViewModel;


import com.example.smarthouse.di.ViewModelKey;
import com.example.smarthouse.viewmodels.HousesListFragmentViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class HousesListFragmentViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(HousesListFragmentViewModel.class)
    public abstract ViewModel bindViewModel(HousesListFragmentViewModel HousesListFragmentViewModel);
}
