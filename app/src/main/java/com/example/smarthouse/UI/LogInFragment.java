package com.example.smarthouse.UI;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smarthouse.R;
import com.example.smarthouse.databinding.FragmentLogInBinding;
import com.example.smarthouse.viewmodels.LogInViewModel;
import com.example.smarthouse.viewmodels.ViewModelProviderFactory;
import com.jakewharton.rxbinding3.view.RxView;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


/**
 * A simple {@link Fragment} subclass.
 */
public class LogInFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory providerFactory;

    private LogInViewModel viewModel;

    FragmentLogInBinding binding;

    public LogInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLogInBinding.inflate(inflater,container,false);
        viewModel = ViewModelProviders.of(this,providerFactory).get(LogInViewModel.class);
        binding.setViewModel(viewModel);

        Disposable loginClickDisposable = RxView.clicks(binding.cardLayout.cirLoginButton).subscribe(
                (unit) -> viewModel.authenticate() ,
                (e) -> Log.e("LogInClickError", e.getMessage())
        );

        Disposable registerTextViewClickDisposable = RxView.clicks(binding.cardLayout.registerTextView).subscribe(
                (unit) -> Navigation.findNavController(binding.cardLayout.cirLoginButton).navigate(R.id.action_logInFragment_to_registration_graph),
                (e) -> Log.e("RegisterCickError: ", e.getMessage())
        );


        Disposable logInPositionDisposable = viewModel.getLogInPositionObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(
                (logInPosition) -> {
                    switch (logInPosition) {
                        case AUTHENTICATED_AND_SET_UP:
                            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_logInFragment_to_housesListFragmnet);
                            break;

                        case AUTHENTICATED_AND_SETTING_UP:
                            viewModel.saveCredencials(getContext(),getViewLifecycleOwner());
                            break;
                        case INVALID_AUTHENTICATION:
                            binding.cardLayout.errorTextView.setVisibility(VISIBLE);
                            binding.cardLayout.errorTextView.setText("Invalid credencials");
                            break;
                        case UNAUTHENTICATED:
                            binding.cardLayout.errorTextView.setVisibility(INVISIBLE);
                            break;
                    }
                },
                (e) -> Log.e("LogInPositionError: ",e.getMessage())
        );

        Disposable areFiledsFilledDisposabe = viewModel.getAreFieldsFilled().observeOn(AndroidSchedulers.mainThread()).subscribe(
                (isFilled) ->
                {
                    if(isFilled)
                    {
                        binding.cardLayout.cirLoginButton.setClickable(true);
                    }
                    else
                    {
                        binding.cardLayout.cirLoginButton.setClickable(false);
                    }
                }
        );

        viewModel.addDisposable(loginClickDisposable,registerTextViewClickDisposable,loginClickDisposable,areFiledsFilledDisposabe);

        return binding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.disposeViewsDisposables();

    }
}