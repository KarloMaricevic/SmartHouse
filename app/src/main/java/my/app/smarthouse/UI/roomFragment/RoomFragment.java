package my.app.smarthouse.UI.roomFragment;


import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import my.app.smarthouse.BaseAplication;
import my.app.smarthouse.R;
import my.app.smarthouse.UI.BaseFragment;
import my.app.smarthouse.UI.LogInFragmentDirections;
import my.app.smarthouse.databinding.FragmentRoomBinding;
import my.app.smarthouse.viewmodels.RoomViewModel;
import my.app.smarthouse.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoomFragment extends BaseFragment {

    @Inject
    ViewModelProviderFactory mViewModelFactory;


    FragmentRoomBinding mBinding;

    RoomViewModel mViewModel;

    ActionBar mActionBar;


    public RoomFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        ((BaseAplication) getActivity().getApplication())
                .getmAuthCompoent()
                .getRoomSubcomponentFacotry()
                .create()
                .inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mBinding = FragmentRoomBinding.inflate(inflater,container,false);

        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(RoomViewModel.class);



        mViewModel.setmHouseId(RoomFragmentArgs.fromBundle(getArguments()).getHouseId());

        setUpToolbar();

        mBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        mBinding.drawerLayout.openDrawer(GravityCompat.START);

        return mBinding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        //region AuthViewModel

        Disposable authStateDisposable = mViewModel.getmAuthState()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((authState) -> {
                            switch (authState) {

                                case UNAUTHENTICATED:
                                    ((BaseAplication) getActivity().getApplication()).releseAuthComponent();
                                    Navigation.findNavController(mBinding.getRoot()).navigate(LogInFragmentDirections.actionGlobalLogInFragment());
                                    break;
                            }
                        },
                        (e) -> Log.e("AuthObserverError:" ,e.getMessage()));

        //endregion


        Disposable hasUserChosenRoomOnceDisposable = mViewModel.getHasUserChosenRoomOnceObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (hasChosen) -> mBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED),
                        (e) ->{});


        Disposable chosenRoomidDisposable= mViewModel
                .getmChosenRoomIdObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ((roomInfo) -> {
                            if(roomInfo != null) {
                                mBinding.drawerLayout.closeDrawers();
                            }
                        }),
                        (e) -> Log.e("ToolbarTitleError: ",e.getMessage())
                );

        Disposable choosenRoomNameDisposable = mViewModel
                .getChosenRoomNameObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (roomName) ->  mActionBar.setTitle(roomName),
                        (e) ->Log.e("TitleError: ", e.getMessage())
                );



        addDisposables(authStateDisposable,hasUserChosenRoomOnceDisposable,chosenRoomidDisposable,choosenRoomNameDisposable);

    }

    public void setUpToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mBinding.roomsToolbar);
        mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(mActionBar != null)
        {
            mActionBar.setTitle("");
            mActionBar.setDisplayHomeAsUpEnabled(true);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), mBinding.drawerLayout, mBinding.roomsToolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
            toggle.setDrawerIndicatorEnabled(true);
            mBinding.drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        }



        setBackNavigation();

    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null)
        {
            if(savedInstanceState.getBoolean("isDrawerOpned",true))
            {
                mBinding.drawerLayout.openDrawer(GravityCompat.START);
            }
            else
            {
                mBinding.drawerLayout.closeDrawer(GravityCompat.START);
            }
            mViewModel.setmHouseId(savedInstanceState.getString("houseId",null));
            mViewModel.setmChosenRoomId(savedInstanceState.getString("roomId",null));
        }


    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isDrawerOpned", mBinding.drawerLayout.isDrawerOpen(GravityCompat.START));
        outState.putString("houseId", mViewModel.getmHouseId());
        outState.putString("roomId", mViewModel.getmChosenRoomId());

    }

    void setBackNavigation() {

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                mViewModel.getRoomIdPositionForBackNavigation().subscribe(
                        (index) ->
                        {
                            if(index == -1)
                            {
                                Navigation.findNavController(mBinding.getRoot()).navigate(RoomFragmentDirections.actionRoomFragmentToHousesListFragmnet());
                            }
                            else
                            {
                                mViewModel.setUpRoomIdFromBackNavigation(index);
                            }
                        }

                );
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this,callback);

    }


}


