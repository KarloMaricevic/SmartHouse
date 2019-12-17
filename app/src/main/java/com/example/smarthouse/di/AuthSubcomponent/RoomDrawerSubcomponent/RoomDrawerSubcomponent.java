package com.example.smarthouse.di.AuthSubcomponent.RoomDrawerSubcomponent;


import com.example.smarthouse.UI.roomFragment.RoomDrawerFragment;
import com.example.smarthouse.adapters.RoomFragment.Drawer.INavigation;
import com.example.smarthouse.di.AuthSubcomponent.RoomSubcomponent.RoomViewModelModule;
import com.example.smarthouse.di.Scopes.PerFragment;
import com.example.smarthouse.viewmodels.RoomViewModel;

import dagger.BindsInstance;
import dagger.Subcomponent;

@PerFragment
@Subcomponent
        (modules = RoomViewModelModule.class)
public interface RoomDrawerSubcomponent {

    void inject(RoomDrawerFragment roomDrawerFragment);

    @Subcomponent.Factory
    interface Factory {
        RoomDrawerSubcomponent create(@BindsInstance INavigation iNavigation);
    }

}
