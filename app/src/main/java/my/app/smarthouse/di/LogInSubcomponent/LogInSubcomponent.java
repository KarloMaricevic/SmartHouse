package my.app.smarthouse.di.LogInSubcomponent;


import my.app.smarthouse.UI.LogInFragment;
import my.app.smarthouse.di.Scopes.PerFragment;

import dagger.Subcomponent;

@PerFragment
@Subcomponent
        (modules = {
                LogInModule.class,
                LogInViewModelModule.class

        })
public interface LogInSubcomponent {
    void inject(LogInFragment logInFragment);

    @Subcomponent.Factory
    interface Factory
    {
        LogInSubcomponent create();
    }

}
