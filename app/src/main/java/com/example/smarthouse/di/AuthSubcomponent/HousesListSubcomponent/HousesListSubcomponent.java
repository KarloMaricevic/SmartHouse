package com.example.smarthouse.di.AuthSubcomponent.HousesListSubcomponent;

import com.example.smarthouse.UI.HousesListFragment;
import com.example.smarthouse.di.Scopes.PerFragment;

import dagger.Subcomponent;

@PerFragment
@Subcomponent
        (modules = {
                HousesListModule.class,
                HousesListViewModelModule.class
        })
public interface HousesListSubcomponent {
    void inject(HousesListFragment housesListFragment);

    @Subcomponent.Factory
    interface Factory
    {
        HousesListSubcomponent create();
    }

}
