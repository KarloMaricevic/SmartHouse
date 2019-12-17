package com.example.smarthouse.UI.roomFragment;


import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smarthouse.BaseAplication;
import com.example.smarthouse.R;
import com.example.smarthouse.UI.BaseFragment;
import com.example.smarthouse.adapters.RoomFragment.RoomDetails.ItemDecoration;
import com.example.smarthouse.adapters.RoomFragment.RoomDetails.MyMultiTypeAdapter;
import com.example.smarthouse.databinding.FragmentRoomDetailsBinding;
import com.example.smarthouse.viewmodels.RoomViewModel;
import com.example.smarthouse.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoomDetailsFragment extends BaseFragment {


    @Inject
    ViewModelProviderFactory viewModelFactory;

    @Inject
    MyMultiTypeAdapter adapter;

    FragmentRoomDetailsBinding binding;

    RoomViewModel viewModel;

    public RoomDetailsFragment() {
    }


    @Override
    public void onAttach(@NonNull Context context) {
        ((BaseAplication) getActivity().getApplication())
                .getAuthCompoent()
                .getRoomDetailsSubcomponentFactory()
                .create()
                .inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel = ViewModelProviders.of(getParentFragment(),viewModelFactory).get(RoomViewModel.class);

        binding = FragmentRoomDetailsBinding.inflate(inflater,container,false);

        ItemDecoration itemDecoration = new ItemDecoration(12,25);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        binding.recyclerView.addItemDecoration(itemDecoration);
        binding.recyclerView.setAdapter(adapter);

        Disposable observableDataForRoomDisposable =
            viewModel
                .getObservableDataForRoomDetailsAdapter()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (item) -> {
                            if(item.size() == 0)
                            {
                                adapter.changeDataSet(item);
                                binding.errorTextView.setVisibility(View.VISIBLE);
                            }
                            else {
                                binding.errorTextView.setVisibility(View.INVISIBLE);
                                adapter.changeDataSet(item);
                            }
                        },
                        (e) -> Log.e("RoomDetailsDataError: " , e.getMessage())
                );

        addDisposables(observableDataForRoomDisposable);

        return binding.getRoot();
    }
}
