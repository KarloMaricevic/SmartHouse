package my.app.smarthouse.utils;

import androidx.databinding.ObservableField;

import io.reactivex.Observable;

public class MakeObservable {

    public static Observable<String> makeObservebleForString(ObservableField<String> observableField)
    {
        return Observable.create(
                (emiter) ->
                {
                    if(observableField.get() != null){
                        emiter.onNext(observableField.get());
                    }
                    final androidx.databinding.Observable.OnPropertyChangedCallback callback = new androidx.databinding.Observable.OnPropertyChangedCallback() {
                        @Override
                        public void onPropertyChanged(androidx.databinding.Observable sender, int propertyId) {
                            emiter.onNext(observableField.get());
                        }
                    };

                    observableField.addOnPropertyChangedCallback(callback);

                    emiter.setCancellable(() -> observableField.removeOnPropertyChangedCallback(callback));
                });
    }
}
