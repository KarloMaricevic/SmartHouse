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
import com.example.smarthouse.Repository;
import com.example.smarthouse.utils.MakeObservable;
import com.example.smarthouse.workers.SaveCredencilasWorkManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.smarthouse.viewmodels.LogInViewModel.LogInPosition.AUTHENTICATED_AND_SET_UP;
import static com.example.smarthouse.viewmodels.LogInViewModel.LogInPosition.UNAUTHENTICATED;


public class LogInViewModel extends ViewModel{

    public enum LogInPosition
    {
        UNAUTHENTICATED,
        INVALID_AUTHENTICATION,
        AUTHENTICATED_AND_SETTING_UP,
        AUTHENTICATED_AND_SET_UP
    }


    protected Repository repository;

    protected ObservableField<String> username;
    protected ObservableField<String> password;
    protected ObservableField<LogInPosition> logInPosition;



    protected Observable<LogInPosition> logInPositionObservable;
    protected Observable<Boolean> areFieldsFilled;


    CompositeDisposable compositeDisposable;



    @Inject
    public LogInViewModel(Repository repository)
    {
        this.repository = repository;


        username = new ObservableField<>();
        password = new ObservableField<>();
        logInPosition = new ObservableField<>();

        compositeDisposable = new CompositeDisposable();


        Observable<ObservableField<String>> usernameFiledObserver = MakeObservable.makeObservebleForString(username);
        Observable<ObservableField<String>> passwordFiledObserver = MakeObservable.makeObservebleForString(password);


        areFieldsFilled = Observable
                .combineLatest(usernameFiledObserver,passwordFiledObserver,(obj1,obj2) -> {
                    List<ObservableField<String>> list = new ArrayList<>();
                    list.add(obj1);
                    list.add(obj2);
                    return list;})
                .subscribeOn(Schedulers.computation())
                .map((item) -> {
                    if(item.get(0).get().length() != 0 && item.get(1).get().length() != 0) {
                        return true;
                    }
                    else {
                        return false;
                    }
                })
                .distinctUntilChanged()
                .cacheWithInitialCapacity(1);

        Observable<LogInPosition> logInPositionObservable  = Observable.create((emiter) -> {
            emiter.onNext(logInPosition.get());

            final androidx.databinding.Observable.OnPropertyChangedCallback callback =  new androidx.databinding.Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(androidx.databinding.Observable sender, int propertyId) {
                    emiter.onNext(logInPosition.get());
                }
            };
            logInPosition.addOnPropertyChangedCallback(callback);

            emiter.setCancellable(() ->logInPosition.removeOnPropertyChangedCallback(callback));
        });

        this.logInPositionObservable = logInPositionObservable
                .distinctUntilChanged()
                .cacheWithInitialCapacity(1);


        username.set("darkarcher5");
        password.set("karlo271236");
        logInPosition.set(UNAUTHENTICATED);
    }


    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
        logInPosition.set(UNAUTHENTICATED);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
        logInPosition.set(UNAUTHENTICATED);
    }


    public void authenticate()
    {

        repository.getPassword(username.get()).observeOn(Schedulers.computation()).subscribe(new MaybeObserver<String>() {
            Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                this.disposable  = d;
            }

            @Override
            public void onSuccess(String dbPassword) {
                logInPosition.set(UNAUTHENTICATED);
                if(checkDbPassword(dbPassword)) {
                    logInPosition.set(LogInPosition.AUTHENTICATED_AND_SETTING_UP);
                }
                else
                {
                    logInPosition.set(LogInPosition.INVALID_AUTHENTICATION);
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
                logInPosition.set(LogInPosition.INVALID_AUTHENTICATION);
                disposable.dispose();
            }
        });
    }

    public Observable<Boolean> getAreFieldsFilled() {
        return areFieldsFilled;
    }



    private boolean checkDbPassword(String dbPassword)
    {
        if(password.get().equals(dbPassword))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public Observable<LogInPosition> getLogInPositionObservable() {
        return logInPositionObservable;
    }


    public void addDisposable(Disposable... disposables)
    {
        for(Disposable disposable : disposables) {
            compositeDisposable.add(disposable);
        }
    }

    public void disposeViewsDisposables()
    {
        compositeDisposable.clear();
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
    }



    public void saveCredencials(Context context, LifecycleOwner fragmentLifecycle)
    {

        Data inputData = new Data.Builder()
                .putString(context.getString(R.string.username),username.get())
                .putString(context.getString(R.string.password),password.get())
                .build();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(SaveCredencilasWorkManager.class)
                .setInputData(inputData)
                .build();

        WorkManager.getInstance(context).enqueue(request);

        WorkManager.getInstance(context).getWorkInfoByIdLiveData(request.getId()).observe(fragmentLifecycle,
                (workInfo) -> {
                    if(workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED)
                    {
                        logInPosition.set(AUTHENTICATED_AND_SET_UP);
                    }

                });
    }




}

