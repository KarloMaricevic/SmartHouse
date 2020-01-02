package com.example.smarthouse.viewmodels;

import androidx.lifecycle.ViewModel;

import android.content.Context;
import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LifecycleOwner;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.smarthouse.R;
import com.example.smarthouse.repositorys.Repository;
import com.example.smarthouse.utils.MakeObservable;
import com.example.smarthouse.workers.SaveCredencilasWorkManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;

import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class LogInViewModel extends ViewModel{

    public enum LogInPosition {
        UNAUTHENTICATED,
        INVALID_AUTHENTICATION,
        AUTHENTICATED_AND_SETTING_UP,
        AUTHENTICATED_AND_SET_UP
    }

    protected Repository mRepository;

    protected ObservableField<String> mUsername;
    protected ObservableField<String> mPassword;
    protected ObservableField<LogInPosition> mLogInPosition;

    Observable<String> mUsernameFiledObserver;
    Observable<String> mPasswordFiledObserver;


    @Inject
    public LogInViewModel(Repository repository) {
        super();
        this.mRepository = repository;

        mUsername = new ObservableField<>();
        mPassword = new ObservableField<>();
        mLogInPosition = new ObservableField<>();


        mUsernameFiledObserver = MakeObservable.makeObservebleForString(mUsername);
        mPasswordFiledObserver = MakeObservable.makeObservebleForString(mPassword);


        mUsername.set("darkarcher5");
        mPassword.set("karlo271236");

        mLogInPosition.set(LogInPosition.UNAUTHENTICATED);
    }

    public String getUsername() {
        return mUsername.get();
    }

    public void setUsername(String mUsername) {
        this.mUsername.set(mUsername);
        mLogInPosition.set(LogInPosition.UNAUTHENTICATED);
    }

    public String getPassword() {
        return mPassword.get();
    }

    public void setPassword(String mPassword) {
        this.mPassword.set(mPassword);
        mLogInPosition.set(LogInPosition.UNAUTHENTICATED);
    }

    public void authenticate() {

        mRepository.getPassword(mUsername.get()).observeOn(Schedulers.computation()).subscribe(new MaybeObserver<String>() {
            Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                this.disposable  = d;
            }

            @Override
            public void onSuccess(String dbPassword) {
                mLogInPosition.set(LogInPosition.UNAUTHENTICATED);
                if(checkDbPassword(dbPassword)) {
                    mLogInPosition.set(LogInPosition.AUTHENTICATED_AND_SETTING_UP);
                }
                else
                {
                    mLogInPosition.set(LogInPosition.INVALID_AUTHENTICATION);
                }
                disposable.dispose();
            }

            @Override
            public void onError(Throwable e) {
                Log.e("Error",e.getMessage());
                disposable.dispose();
            }

            @Override
            public void onComplete() {
                mLogInPosition.set(LogInPosition.INVALID_AUTHENTICATION);
                disposable.dispose();
            }
        });
    }

    private boolean checkDbPassword(String dbPassword) {
        if(mPassword.get().equals(dbPassword))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void saveCredencials(Context context, LifecycleOwner fragmentLifecycle) {

        Data inputData = new Data.Builder()
                .putString(context.getString(R.string.username), mUsername.get())
                .putString(context.getString(R.string.password), mPassword.get())
                .build();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(SaveCredencilasWorkManager.class)
                .setInputData(inputData)
                .build();

        WorkManager.getInstance(context).enqueue(request);

        WorkManager.getInstance(context).getWorkInfoByIdLiveData(request.getId()).observe(fragmentLifecycle,
                (workInfo) -> {
                    if(workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED)
                    {
                        mLogInPosition.set(LogInPosition.AUTHENTICATED_AND_SET_UP);
                    }

                });
    }

    public Observable<Boolean> getAreFiledsFilled() {
        return Observable
                .combineLatest(mUsernameFiledObserver, mPasswordFiledObserver,(obj1, obj2) -> {
                    List<String> list = new ArrayList<>();
                    list.add(obj1);
                    list.add(obj2);
                    return list;})
                .subscribeOn(Schedulers.computation())
                .map((item) -> {
                    if(item.get(0).length() != 0 && item.get(1).length() != 0) {
                        return true;
                    }
                    else {
                        return false;
                    }
                })
                .distinctUntilChanged()
                .cacheWithInitialCapacity(1);
    }

    public Observable<LogInPosition> getLogInPositionObservable() {

        ObservableOnSubscribe<LogInPosition> observableOnSubscribe = (emitter) -> {
                emitter.onNext(mLogInPosition.get());

                final androidx.databinding.Observable.OnPropertyChangedCallback callback = new androidx.databinding.Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(androidx.databinding.Observable sender, int propertyId) {
                        emitter.onNext(mLogInPosition.get());
                    }
                };
                mLogInPosition.addOnPropertyChangedCallback(callback);

                emitter.setCancellable(() -> mLogInPosition.removeOnPropertyChangedCallback(callback));
            };

        return  Observable
                    .create(observableOnSubscribe)
                    .distinctUntilChanged()
                    .cacheWithInitialCapacity(1);
    }


}



























