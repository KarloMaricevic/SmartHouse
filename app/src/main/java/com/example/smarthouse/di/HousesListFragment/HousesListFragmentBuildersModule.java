package com.example.smarthouse.di.HousesListFragment;
import com.example.smarthouse.UI.HousesListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class HousesListFragmentBuildersModule {
    @ContributesAndroidInjector
            (modules = {HousesListFragmentViewModelModule.class})

    abstract HousesListFragment contributesProfileFragment();

}
