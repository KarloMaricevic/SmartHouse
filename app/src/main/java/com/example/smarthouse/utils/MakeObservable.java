package com.example.smarthouse.utils;

import androidx.databinding.ObservableField;

import io.reactivex.Observable;

public class MakeObservable {


    public static Observable<ObservableField<String>> makeObservebleForString(ObservableField observableField)
    {
        return Observable.create(
                (emiter) ->
                {
                    emiter.onNext(observableField);


                    final androidx.databinding.Observable.OnPropertyChangedCallback callback = new androidx.databinding.Observable.OnPropertyChangedCallback() {
                        @Override
                        public void onPropertyChanged(androidx.databinding.Observable sender, int propertyId) {
                            emiter.onNext(observableField);
                        }
                    };

                    observableField.addOnPropertyChangedCallback(callback);

                    emiter.setCancellable(() -> observableField.removeOnPropertyChangedCallback(callback));
                });
    }
}
