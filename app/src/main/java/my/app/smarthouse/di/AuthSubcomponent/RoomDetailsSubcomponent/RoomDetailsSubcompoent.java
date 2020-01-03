package my.app.smarthouse.di.AuthSubcomponent.RoomDetailsSubcomponent;


import my.app.smarthouse.UI.roomFragment.RoomDetailsFragment;
import my.app.smarthouse.di.AuthSubcomponent.RoomSubcomponent.RoomViewModelModule;
import my.app.smarthouse.di.Scopes.PerFragment;

import dagger.BindsInstance;
import dagger.Subcomponent;


@PerFragment
@Subcomponent
        (modules = {RoomViewModelModule.class,
                RoomDetailsModule.class})
public interface RoomDetailsSubcompoent {
    void inject(RoomDetailsFragment roomDetailsFragment);


    @Subcomponent.Factory
    interface Factory{
        RoomDetailsSubcompoent create(@BindsInstance RoomDetailsFragment roomDetailsFragment);
    }
}
