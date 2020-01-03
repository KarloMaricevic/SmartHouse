package my.app.smarthouse.di.AuthSubcomponent.RoomDetailsSubcomponent;


import my.app.smarthouse.UI.roomFragment.RoomDetailsFragment;
import my.app.smarthouse.adapters.RoomFragment.RoomDetails.viewHolders.RoomAdapterCallback;
import my.app.smarthouse.di.Scopes.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RoomDetailsModule {

    @Binds
    @PerFragment
    public abstract RoomAdapterCallback provideRoomAdapterCallback(RoomDetailsFragment roomDetailsFragment);

}
