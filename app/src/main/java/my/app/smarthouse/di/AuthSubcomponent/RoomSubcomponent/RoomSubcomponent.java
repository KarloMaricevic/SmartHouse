package my.app.smarthouse.di.AuthSubcomponent.RoomSubcomponent;

import my.app.smarthouse.UI.roomFragment.RoomFragment;

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
