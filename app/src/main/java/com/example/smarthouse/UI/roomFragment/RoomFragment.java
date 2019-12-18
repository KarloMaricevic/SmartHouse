package com.example.smarthouse.UI.roomFragment;


import android.content.Context;
import android.content.res.Configuration;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smarthouse.BaseAplication;
import com.example.smarthouse.R;
import com.example.smarthouse.UI.BaseFragment;
import com.example.smarthouse.UI.LogInFragmentDirections;
import com.example.smarthouse.databinding.FragmentRoomBinding;
import com.example.smarthouse.viewmodels.RoomViewModel;
import com.example.smarthouse.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoomFragment extends BaseFragment {

    @Inject
    ViewModelProviderFactory viewModelFactory;


    FragmentRoomBinding binding;

    RoomViewModel viewModel;


    public RoomFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        ((BaseAplication) getActivity().getApplication())
                .getAuthCompoent()
                .getRoomSubcomponentFacotry()
                .create()
                .inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = FragmentRoomBinding.inflate(inflater,container,false);

        viewModel = ViewModelProviders.of(this,viewModelFactory).get(RoomViewModel.class);

        //region AuthViewModel

        Disposable authStateDisposable = viewModel.getAuthState()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((authState) -> {
                            switch (authState) {

                                case UNAUTHENTICATED:
                                    ((BaseAplication) getActivity().getApplication()).releseAuthComponent();
                                    Navigation.findNavController(binding.getRoot()).navigate(LogInFragmentDirections.actionGlobalLogInFragment());
                                    break;
                            }
                        },
                        (e) -> Log.e("AuthObserverError:" ,e.getMessage()));

        //endregion


        viewModel.setHouseId(RoomFragmentArgs.fromBundle(getArguments()).getHouseId());

        setUpToolbar();

        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        binding.drawerLayout.openDrawer(GravityCompat.START);


        Disposable hasUserChosenRoomOnceDisposable = viewModel.getHasUserChosenRoomOnceObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (hasChosen) -> binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED),
                        (e) ->{});

        addDisposables(authStateDisposable,hasUserChosenRoomOnceDisposable);

        return binding.getRoot();
    }

    public void setUpToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.roomsToolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(),binding.drawerLayout,binding.roomsToolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
            toggle.setDrawerIndicatorEnabled(true);
            binding.drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        }

        Disposable chosenRoomidDisposable= viewModel
                .getChosenRoomIdObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ((roomInfo) -> {
                            if(roomInfo != null) {
                                binding.drawerLayout.closeDrawers();
                            }
                        }),
                        (e) -> Log.e("ToolbarTitleError: ",e.getMessage())
                );

        Disposable choosenRoomNameDisposable = viewModel
                .getChosenRoomNameObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (roomName) -> actionBar.setTitle(roomName),
                        (e) ->Log.e("TitleError: ", e.getMessage())
                );


        setBackNavigation();
        addDisposables(chosenRoomidDisposable,choosenRoomNameDisposable);



    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null)
        {
            if(savedInstanceState.getBoolean("isDrawerOpned",true))
            {
                binding.drawerLayout.openDrawer(GravityCompat.START);
            }
            else
            {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            }
            viewModel.setHouseId(savedInstanceState.getString("houseId",null));
            viewModel.setChosenRoomId(savedInstanceState.getString("roomId",null));
        }


    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isDrawerOpned",binding.drawerLayout.isDrawerOpen(GravityCompat.START));
        outState.putString("houseId",viewModel.getHouseId());
        outState.putString("roomId",viewModel.getChosenRoomId());

    }

    void setBackNavigation() {

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                viewModel.getRoomIdPositionForBackNavigation().subscribe(
                        (index) ->
                        {
                            if(index == -1)
                            {
                                Navigation.findNavController(binding.getRoot()).navigate(RoomFragmentDirections.actionRoomFragmentToHousesListFragmnet());
                            }
                            else
                            {
                                viewModel.setUpRoomIdFromBackNavigation(index);
                            }
                        }

                );
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this,callback);

    }
}


