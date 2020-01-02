package com.example.smarthouse.UI.roomFragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smarthouse.BaseAplication;
import com.example.smarthouse.UI.BaseFragment;
import com.example.smarthouse.adapters.RoomFragment.Drawer.INavigation;
import com.example.smarthouse.adapters.RoomFragment.Drawer.RoomListAdapter;
import com.example.smarthouse.adapters.RoomFragment.Drawer.RoomListAdapterDecorator;
import com.example.smarthouse.databinding.FragmentRoomDrawerBinding;
import com.example.smarthouse.viewmodels.RoomViewModel;
import com.example.smarthouse.viewmodels.ViewModelProviderFactory;
import com.jakewharton.rxbinding3.appcompat.RxSearchView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoomDrawerFragment extends BaseFragment implements INavigation {

    @Inject
    ViewModelProviderFactory mViewModelFactory;

    @Inject
    RoomListAdapter mAdapter;

    FragmentRoomDrawerBinding mBinding;

    RoomViewModel mViewModel;


    @Override
    public void onAttach(@NonNull Context context) {
        ((BaseAplication) getActivity().getApplication())
                .getmAuthCompoent()
                .getRoomDrawerSubcomponentFactory()
                .create(this)
                .inject(this);

        super.onAttach(context);
    }

    public RoomDrawerFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mBinding = FragmentRoomDrawerBinding.inflate(inflater,container,false);

        mViewModel = ViewModelProviders.of(getParentFragment(), mViewModelFactory).get(RoomViewModel.class);

        setUpRecyclerView();

        Disposable qeryTextChangeDisposable = RxSearchView.queryTextChangeEvents(mBinding.roomsSearchView)
                .subscribe(
                (searchViewQueryTextEvent) ->
                {
                    String searchText = searchViewQueryTextEvent.getQueryText().toString();
                    mViewModel.setmDrawerQueryText(searchText);

                });


        Disposable selectedRoomIdDisposable  = mViewModel
                .getmChosenRoomIdObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (roomId) ->
                        {
                            mAdapter.setSelected(roomId);
                        },
                        (e) -> {}
                );


        addDisposables(selectedRoomIdDisposable,qeryTextChangeDisposable);

        return mBinding.getRoot();
    }

    @Override
    public void onViewClick(String roomId) {
        mViewModel.setmChosenRoomId(roomId);
    }


    void setUpRecyclerView()
    {
        mBinding.navigationRecyclerView.setAdapter(mAdapter);
        mBinding.navigationRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        mBinding.navigationRecyclerView.addItemDecoration(new RoomListAdapterDecorator(10,10));
        Disposable roomListFiltredDisposable = mViewModel.roomListFilteredObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (roomList) ->
                        {
                            if(roomList.isEmpty())
                            {
                                mBinding.noRoomsTextView.setVisibility(View.VISIBLE);
                            }
                            else {
                                mBinding.noRoomsTextView.setVisibility(View.INVISIBLE);
                                mAdapter.changeData(roomList);
                            }
                        },
                        (e) -> Log.e("DrawerRecyclerError: ",e.getMessage()));
        addDisposables(roomListFiltredDisposable);
    }




}
