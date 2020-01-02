package com.example.smarthouse.UI;

import androidx.fragment.app.Fragment;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseFragment extends Fragment {
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    protected void addDisposables(Disposable... disposables)
    {
        for(Disposable disposable : disposables)
        {
            mCompositeDisposable.add(disposable);
        }

    }


    @Override
    public void onStop() {
        mCompositeDisposable.clear();
        super.onStop();
    }

    public void clearAllDisposables()
    {
        mCompositeDisposable.clear();
    }

}
