package my.app.smarthouse.di.AuthSubcomponent.HousesListSubcomponent;

import my.app.smarthouse.UI.HousesListFragment;
import my.app.smarthouse.adapters.housesListAdapter.IOption;
import my.app.smarthouse.di.Scopes.PerFragment;

import dagger.BindsInstance;
import dagger.Subcomponent;

@PerFragment
@Subcomponent
        (modules = {
                HousesListModule.class,
                HousesListViewModelModule.class
        })
public interface HousesListSubcomponent {
    void inject(HousesListFragment housesListFragment);

    @Subcomponent.Factory
    interface Factory
    {
        HousesListSubcomponent create(@BindsInstance IOption iOption);
    }

}
