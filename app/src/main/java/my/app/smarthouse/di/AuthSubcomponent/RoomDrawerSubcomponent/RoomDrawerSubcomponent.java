package my.app.smarthouse.di.AuthSubcomponent.RoomDrawerSubcomponent;


import my.app.smarthouse.UI.roomFragment.RoomDrawerFragment;
import my.app.smarthouse.adapters.RoomFragment.Drawer.INavigation;
import my.app.smarthouse.di.AuthSubcomponent.RoomSubcomponent.RoomViewModelModule;
import my.app.smarthouse.di.Scopes.PerFragment;

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
