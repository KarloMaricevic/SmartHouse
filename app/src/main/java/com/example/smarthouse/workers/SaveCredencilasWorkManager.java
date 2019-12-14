package com.example.smarthouse.workers;


import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.RxWorker;
import androidx.work.WorkerParameters;

import com.example.smarthouse.R;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class SaveCredencilasWorkManager extends RxWorker {


    public SaveCredencilasWorkManager(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    @NonNull
    @Override
    public Single<Result> createWork() {

        Single<Result> single = Single.create(
                (emitter) ->
                {
                    String username  =getInputData().getString(getApplicationContext().getString(R.string.username));
                    String password = getInputData().getString(getApplicationContext().getString(R.string.password));

                    if(username == null || username.equals("")){
                        emitter.onSuccess(Result.failure());
                    }
                    else if(password == null || password.equals(""))
                    {
                        emitter.onSuccess(Result.failure());
                    }
                    try{
                        SharedPreferences userCredencials = getApplicationContext().getSharedPreferences(getApplicationContext().getString(R.string.sharedPreferencesName),Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor  = userCredencials.edit();
                        editor.putString(getApplicationContext().getString(R.string.password),password);
                        editor.putString(getApplicationContext().getString(R.string.username),username);
                        editor.commit();

                        emitter.onSuccess(Result.success());
                    }
                    catch (Exception e)
                    {
                        emitter.onSuccess(Result.retry());
                    }
                }
        );
        return single;
    }


    @NonNull
    @Override
    protected Scheduler getBackgroundScheduler() {
        return Schedulers.newThread();
    }
}