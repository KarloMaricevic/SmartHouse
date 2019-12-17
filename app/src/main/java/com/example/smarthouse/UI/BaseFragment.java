package com.example.smarthouse.UI;

import androidx.fragment.app.Fragment;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseFragment extends Fragment {
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected void addDisposables(Disposable... disposables)
    {
        for(Disposable disposable : disposables)
        {
            compositeDisposable.add(disposable);
        }

    }


    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
