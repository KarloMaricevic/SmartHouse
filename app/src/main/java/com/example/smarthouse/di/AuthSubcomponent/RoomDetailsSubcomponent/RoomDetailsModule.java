package com.example.smarthouse.di.AuthSubcomponent.RoomDetailsSubcomponent;


import com.example.smarthouse.UI.roomFragment.RoomDetailsFragment;
import com.example.smarthouse.adapters.RoomFragment.RoomDetails.viewHolders.RoomAdapterCallback;
import com.example.smarthouse.di.Scopes.PerFragment;

import dagger.Binds;
import dagger.BindsInstance;
import dagger.Module;

@Module
public abstract class RoomDetailsModule {

    @Binds
    @PerFragment
    public abstract RoomAdapterCallback provideRoomAdapterCallback(RoomDetailsFragment roomDetailsFragment);

}
