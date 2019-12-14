package com.example.smarthouse.viewmodels;

import androidx.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseViewModel extends ViewModel {

    private CompositeDisposable compositeViewModelDisposable;
    private CompositeDisposable compositeViewDisposable;

    BaseViewModel()
    {
        compositeViewModelDisposable = new CompositeDisposable();
        compositeViewDisposable = new CompositeDisposable();
    }


    protected CompositeDisposable getCompositeViewModelDisposable() {
        return compositeViewModelDisposable;
    }

    public CompositeDisposable getCompositeViewDisposable() {
        return compositeViewDisposable;
    }



    public void addViewDisposables(Disposable... disposables)
    {
        for(Disposable disposable : disposables)
        {
            compositeViewDisposable.add(disposable);
        }
    }

    protected void addViewModelDisposables(Disposable... disposables)
    {
        for(Disposable disposable : disposables)
        {
            compositeViewModelDisposable.add(disposable);
        }
    }

    public void clearDisposables()
    {
        compositeViewModelDisposable.clear();
        compositeViewDisposable.clear();
    }


    public void disposeDisposables()
    {
        compositeViewModelDisposable.dispose();
        compositeViewDisposable.dispose();
    }


    protected void clearViewModelDisposable()
    {
        compositeViewModelDisposable.clear();
    }

    public void clearViewDisposable()
    {
        compositeViewDisposable.clear();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposeDisposables();
    }
}
