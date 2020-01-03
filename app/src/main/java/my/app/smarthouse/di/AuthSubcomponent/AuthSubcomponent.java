package my.app.smarthouse.di.AuthSubcomponent;


import my.app.smarthouse.di.AuthSubcomponent.HousesListSubcomponent.HousesListSubcomponent;
import my.app.smarthouse.di.AuthSubcomponent.HousesListSubcomponent.HousesListSubcomponentFactory;
import my.app.smarthouse.di.AuthSubcomponent.RoomDetailsSubcomponent.RoomDetailsSubcompoent;
import my.app.smarthouse.di.AuthSubcomponent.RoomDetailsSubcomponent.RoomDetailsSubcomponentFactory;
import my.app.smarthouse.di.AuthSubcomponent.RoomDrawerSubcomponent.RoomDrawerSubcomponent;
import my.app.smarthouse.di.AuthSubcomponent.RoomDrawerSubcomponent.RoomDrawerSubcomponentFactory;
import my.app.smarthouse.di.AuthSubcomponent.RoomSubcomponent.RoomSubcomponent;
import my.app.smarthouse.di.AuthSubcomponent.RoomSubcomponent.RoomSubcomponentFactory;
import my.app.smarthouse.di.Scopes.PerAuthComponent;

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
