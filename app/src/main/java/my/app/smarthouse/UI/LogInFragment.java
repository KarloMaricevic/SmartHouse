package my.app.smarthouse.UI;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import my.app.smarthouse.BaseAplication;
import my.app.smarthouse.R;
import my.app.smarthouse.databinding.FragmentLogInBinding;
import my.app.smarthouse.viewmodels.LogInViewModel;
import my.app.smarthouse.viewmodels.ViewModelProviderFactory;
import com.jakewharton.rxbinding3.view.RxView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


public class LogInFragment extends BaseFragment {

    @Inject
    ViewModelProviderFactory mProviderFactory;

    private LogInViewModel mViewModel;

    FragmentLogInBinding mBinding;

    public LogInFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        ((BaseAplication) getActivity().getApplication())
                .getmApplicationComponent()
                .getLogInSubcomponentFacotry()
                .create()
                .inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentLogInBinding.inflate(inflater,container,false);
        mViewModel = ViewModelProviders.of(this, mProviderFactory).get(LogInViewModel.class);
        mBinding.setViewModel(mViewModel);
        return mBinding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        Disposable loginClickDisposable = RxView.clicks(mBinding.cardLayout.cirLoginButton).subscribe(
                (unit) -> mViewModel.authenticate() ,
                (e) -> Log.e("LogInClickError", e.getMessage())
        );

        Disposable registerTextViewClickDisposable = RxView.clicks(mBinding.cardLayout.registerTextView).subscribe(
                (unit) -> Navigation.findNavController(mBinding.cardLayout.cirLoginButton).navigate(R.id.action_logInFragment_to_registration_graph),
                (e) -> Log.e("RegisterCickError: ", e.getMessage())
        );


        Disposable logInPositionDisposable = mViewModel.getLogInPositionObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(
                (logInPosition) -> {
                    switch (logInPosition) {
                        case AUTHENTICATED_AND_SET_UP:
                            hideKeyboard(getContext(),getActivity().getCurrentFocus());
                            Navigation.findNavController(mBinding.getRoot()).navigate(LogInFragmentDirections.actionLogInFragmentToHousesListFragmnet());
                            break;
                        case AUTHENTICATED_AND_SETTING_UP:
                            mViewModel.saveCredencials(getContext(),getViewLifecycleOwner());
                            break;
                        case INVALID_AUTHENTICATION:
                            mBinding.cardLayout.errorTextView.setVisibility(VISIBLE);
                            break;
                        case UNAUTHENTICATED:
                            mBinding.cardLayout.errorTextView.setVisibility(INVISIBLE);
                            break;
                    }
                },
                (e) -> Log.e("LogInPositionError: ",e.getMessage())
        );

        Disposable areFiledsFilledDisposabe = mViewModel.getAreFiledsFilled().observeOn(AndroidSchedulers.mainThread()).subscribe(
                (isFilled) -> {
                    if(isFilled)
                    {
                        mBinding.cardLayout.cirLoginButton.setClickable(true);
                    }
                    else
                    {
                        mBinding.cardLayout.cirLoginButton.setClickable(false);
                    }
                },
                (e) -> Log.e("areFiledsFilledError: ",e.getMessage())
        );

        addDisposables(logInPositionDisposable,loginClickDisposable,registerTextViewClickDisposable,loginClickDisposable,areFiledsFilledDisposabe);

    }

    public void hideKeyboard(Context context, View focusedView)
    {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(focusedView.getWindowToken(),0);
    }
}

