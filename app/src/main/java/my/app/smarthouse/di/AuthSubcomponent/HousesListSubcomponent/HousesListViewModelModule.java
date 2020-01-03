package my.app.smarthouse.di.AuthSubcomponent.HousesListSubcomponent;


import androidx.lifecycle.ViewModel;

import my.app.smarthouse.di.ViewModelKey;
import my.app.smarthouse.viewmodels.HousesListViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class HousesListViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(HousesListViewModel.class)
    public abstract ViewModel bindViewModel(HousesListViewModel housesListFragmentViewModel);
}
