package com.example.smarthouse.di.AuthSubcomponent.RoomSubcomponent;

import com.example.smarthouse.UI.roomFragment.RoomFragment;
import com.example.smarthouse.viewmodels.RoomViewModel;

import javax.inject.Inject;

import dagger.Subcomponent;

@Subcomponent
        (modules = RoomViewModelModule.class)
public interface RoomSubcomponent {

     void  inject(RoomFragment roomFragment);


     @Subcomponent.Factory
    interface Factory{
         RoomSubcomponent create();
     }
}
