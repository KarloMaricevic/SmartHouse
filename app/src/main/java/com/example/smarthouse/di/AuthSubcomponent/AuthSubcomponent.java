package com.example.smarthouse.di.AuthSubcomponent;


import com.example.smarthouse.di.AuthSubcomponent.HousesListSubcomponent.HousesListSubcomponent;
import com.example.smarthouse.di.AuthSubcomponent.HousesListSubcomponent.HousesListSubcomponentFactory;
import com.example.smarthouse.di.AuthSubcomponent.RoomDetailsSubcomponent.RoomDetailsSubcompoent;
import com.example.smarthouse.di.AuthSubcomponent.RoomDetailsSubcomponent.RoomDetailsSubcomponentFactory;
import com.example.smarthouse.di.AuthSubcomponent.RoomDrawerSubcomponent.RoomDrawerSubcomponent;
import com.example.smarthouse.di.AuthSubcomponent.RoomDrawerSubcomponent.RoomDrawerSubcomponentFactory;
import com.example.smarthouse.di.AuthSubcomponent.RoomSubcomponent.RoomSubcomponent;
import com.example.smarthouse.di.AuthSubcomponent.RoomSubcomponent.RoomSubcomponentFactory;
import com.example.smarthouse.di.Scopes.PerAuthComponent;

import dagger.Subcomponent;


@PerAuthComponent
@Subcomponent
        (modules = {
                HousesListSubcomponentFactory.class,
                RoomSubcomponentFactory.class,
                RoomDrawerSubcomponentFactory.class,
                RoomDetailsSubcomponentFactory.class
            }
        )
public interface AuthSubcomponent {
    HousesListSubcomponent.Factory getHousesListSubcomponentFactory();
    RoomSubcomponent.Factory getRoomSubcomponentFacotry();
    RoomDrawerSubcomponent.Factory getRoomDrawerSubcomponentFactory();
    RoomDetailsSubcompoent.Factory getRoomDetailsSubcomponentFactory();

    @Subcomponent.Factory
    interface Factory
    {
        AuthSubcomponent create();
    }






}
