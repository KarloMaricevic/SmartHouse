package com.example.smarthouse.di.LogInSubcomponent;


import com.example.smarthouse.UI.LogInFragment;
import com.example.smarthouse.di.Scopes.PerFragment;

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
