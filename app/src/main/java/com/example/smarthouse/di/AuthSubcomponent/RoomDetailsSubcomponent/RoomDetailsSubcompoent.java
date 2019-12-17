package com.example.smarthouse.di.AuthSubcomponent.RoomDetailsSubcomponent;


import com.example.smarthouse.UI.roomFragment.RoomDetailsFragment;
import com.example.smarthouse.di.AuthSubcomponent.RoomSubcomponent.RoomViewModelModule;
import com.example.smarthouse.di.Scopes.PerFragment;

import dagger.Subcomponent;


@PerFragment
@Subcomponent
        (modules = RoomViewModelModule.class)
public interface RoomDetailsSubcompoent {
    void inject(RoomDetailsFragment roomDetailsFragment);


    @Subcomponent.Factory
    interface Factory{
        RoomDetailsSubcompoent create();
    }
}