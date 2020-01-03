package my.app.smarthouse.di;

//App lvl aplication dependenys,ono što će zbilja biti singelton


import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import my.app.smarthouse.viewmodels.ViewModelProviderFactory;

import java.util.Map;

import javax.inject.Singleton;

//tu se metode anotiraju sa @Singleton

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.Multibinds;

@Module
public abstract class AppModule {

    @Multibinds
    abstract Map<Class<? extends ViewModel>, ViewModel> aMap();

    @Binds
    public abstract @Singleton
    ViewModelProvider.Factory bindViewModelFacotry(ViewModelProviderFactory viewModelFacotry);

}
