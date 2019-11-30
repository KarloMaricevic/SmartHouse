package com.example.smarthouse.di.LogInFragment;


import com.example.smarthouse.UI.LogInFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class LogInFragmentBuildersModule {
    @ContributesAndroidInjector ( modules = {LogInFragmentViewModelModule.class})
    abstract LogInFragment contributeLogInFragment();
}
