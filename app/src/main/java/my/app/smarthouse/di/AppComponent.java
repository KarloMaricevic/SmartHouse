package my.app.smarthouse.di;

import android.app.Application;
import android.content.Context;

import my.app.smarthouse.di.AuthSubcomponent.AuthSubcomponent;
import my.app.smarthouse.di.AuthSubcomponent.AuthSubcomponentFactory;
import my.app.smarthouse.di.LogInSubcomponent.LogInSubcomponent;
import my.app.smarthouse.di.LogInSubcomponent.LogInSubcomponentFactory;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;


@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        AuthSubcomponentFactory.class,
        LogInSubcomponentFactory.class
})
public interface AppComponent{

        AuthSubcomponent.Factory getAuthComponentFactory();
        LogInSubcomponent.Factory getLogInSubcomponentFacotry();

        @Component.Factory
        interface Factory {
            AppComponent create(@BindsInstance Application application, @BindsInstance Context appContex);
        }

    }
