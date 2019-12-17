package com.example.smarthouse.UI.roomFragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smarthouse.BaseAplication;
import com.example.smarthouse.R;
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
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoomDrawerFragment extends BaseFragment implements INavigation {

    @Inject
    ViewModelProviderFactory viewModelFactory;

    @Inject
    RoomListAdapter adapter;

    FragmentRoomDrawerBinding binding;

    RoomViewModel viewModel;


    @Override
    public void onAttach(@NonNull Context context) {
        ((BaseAplication) getActivity().getApplication())
                .getAuthCompoent()
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


        binding = FragmentRoomDrawerBinding.inflate(inflater,container,false);

        viewModel = ViewModelProviders.of(getParentFragment(),viewModelFactory).get(RoomViewModel.class);

        setUpRecyclerView();

        Disposable qeryTextChangeDisposable = RxSearchView.queryTextChangeEvents(binding.roomsSearchView)
                .subscribe(
                (searchViewQueryTextEvent) ->
                {
                    String searchText = searchViewQueryTextEvent.getQueryText().toString();
                    viewModel.setDrawerQueryText(searchText);

                });

        addDisposables(qeryTextChangeDisposable);

        return binding.getRoot();
    }

    @Override
    public void onViewClick(String roomId) {
        viewModel.setChosenRoomId(roomId);
        adapter.setSelected(roomId);
    }


    void setUpRecyclerView()
    {
        binding.navigationRecyclerView.setAdapter(adapter);
        binding.navigationRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        binding.navigationRecyclerView.addItemDecoration(new RoomListAdapterDecorator(10,10));
        Disposable roomListFiltredDisposable = viewModel.roomListFilteredObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (roomList) ->
                        {
                            if(roomList.isEmpty())
                            {
                                binding.noRoomsTextView.setVisibility(View.VISIBLE);
                            }
                            else {
                                binding.noRoomsTextView.setVisibility(View.INVISIBLE);
                                adapter.changeData(roomList);
                            }
                        },
                        (e) -> Log.e("DrawerRecyclerError: ",e.getMessage()));
        addDisposables(roomListFiltredDisposable);
    }
}
