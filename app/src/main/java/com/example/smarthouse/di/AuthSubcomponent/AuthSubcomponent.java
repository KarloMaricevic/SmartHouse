package com.example.smarthouse.di.AuthSubcomponent;


import com.example.smarthouse.di.AuthSubcomponent.HousesListSubcomponent.HousesListSubcomponent;
import com.example.smarthouse.di.AuthSubcomponent.HousesListSubcomponent.HousesListSubcomponentFactory;
import com.example.smarthouse.di.Scopes.PerAuthComponent;

import dagger.Subcomponent;


@PerAuthComponent
@Subcomponent
        (modules = HousesListSubcomponentFactory.class)
public interface AuthSubcomponent {

    HousesListSubcomponent.Factory getHousesListSubcomponentFactory();

    @Subcomponent.Factory
    interface Factory
    {
        AuthSubcomponent create();
    }






}
