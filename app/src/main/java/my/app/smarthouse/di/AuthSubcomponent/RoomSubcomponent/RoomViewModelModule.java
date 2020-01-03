package my.app.smarthouse.di.AuthSubcomponent.RoomSubcomponent;

import androidx.lifecycle.ViewModel;

import my.app.smarthouse.di.ViewModelKey;
import my.app.smarthouse.viewmodels.RoomViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;


@Module
public abstract class RoomViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(RoomViewModel.class)
    public abstract ViewModel bindViewModel(RoomViewModel roomViewModel);
}
