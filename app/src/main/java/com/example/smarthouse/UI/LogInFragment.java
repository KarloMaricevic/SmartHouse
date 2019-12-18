package com.example.smarthouse.UI;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.smarthouse.BaseAplication;
import com.example.smarthouse.R;
import com.example.smarthouse.databinding.FragmentLogInBinding;
import com.example.smarthouse.viewmodels.LogInViewModel;
import com.example.smarthouse.viewmodels.ViewModelProviderFactory;
import com.jakewharton.rxbinding3.view.RxView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


public class LogInFragment extends BaseFragment {

    @Inject
    ViewModelProviderFactory providerFactory;

    private LogInViewModel viewModel;

    FragmentLogInBinding binding;

    public LogInFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        ((BaseAplication) getActivity().getApplication())
                .getApplicationComponent()
                .getLogInSubcomponentFacotry()
                .create()
                .inject(this);
        super.onAttach(context);
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
                            hideKeyboard(getContext(),getActivity().getCurrentFocus());
                            Navigation.findNavController(binding.getRoot()).navigate(LogInFragmentDirections.actionLogInFragmentToHousesListFragmnet());
                            break;
                        case AUTHENTICATED_AND_SETTING_UP:
                            viewModel.saveCredencials(getContext(),getViewLifecycleOwner());
                            break;
                        case INVALID_AUTHENTICATION:
                            binding.cardLayout.errorTextView.setVisibility(VISIBLE);
                            break;
                        case UNAUTHENTICATED:
                            binding.cardLayout.errorTextView.setVisibility(INVISIBLE);
                            break;
                    }
                },
                (e) -> Log.e("LogInPositionError: ",e.getMessage())
        );

        Disposable areFiledsFilledDisposabe = viewModel.getAreFiledsFilled().observeOn(AndroidSchedulers.mainThread()).subscribe(
                (isFilled) -> {
                    if(isFilled)
                    {
                        binding.cardLayout.cirLoginButton.setClickable(true);
                    }
                    else
                    {
                        binding.cardLayout.cirLoginButton.setClickable(false);
                    }
                },
                (e) -> Log.e("areFiledsFilledError: ",e.getMessage())
        );

        addDisposables(logInPositionDisposable,loginClickDisposable,registerTextViewClickDisposable,loginClickDisposable,areFiledsFilledDisposabe);

        return binding.getRoot();
    }


    public void hideKeyboard(Context context,View focusedView)
    {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(focusedView.getWindowToken(),0);
    }
}

