package my.app.smarthouse.di.LogInSubcomponent;

import androidx.lifecycle.ViewModel;

import my.app.smarthouse.di.ViewModelKey;
import my.app.smarthouse.viewmodels.LogInViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class LogInViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LogInViewModel.class)
    public abstract ViewModel bindViewModel(LogInViewModel logInViewModel);
}
