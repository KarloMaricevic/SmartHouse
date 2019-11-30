package com.example.smarthouse.UI;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smarthouse.R;
import com.example.smarthouse.adapters.HousesListAdapter;
import com.example.smarthouse.databinding.FragmentHousesListFragmnetBinding;
import com.example.smarthouse.viewmodels.HousesListFragmentViewModel;
import com.example.smarthouse.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class HousesListFragment extends DaggerFragment {


    @Inject
    ViewModelProviderFactory providerFactory;

    private HousesListFragmentViewModel viewModel;

    private FragmentHousesListFragmnetBinding binding;

    private HousesListAdapter adapter;

    public HousesListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = ViewModelProviders.of(this,providerFactory).get(HousesListFragmentViewModel.class);
        binding = FragmentHousesListFragmnetBinding.inflate(inflater,container,false);


        //region authFragment
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("userCredencials", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(getString(R.string.username),null);
        String password = sharedPreferences.getString(getString(R.string.password),null);
        viewModel.setUseraname(username);
        viewModel.setPassword(password);


        viewModel.getAuthenticationStateObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(
                (authState) ->
                {
                    switch (authState) {

                        case UNAUTHENTICATED:
                            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_global_logInFragment);
                            break;
                    }
                },
                (e) -> Log.e("AuthObserverError:" ,e.getMessage())
        );

        //endregion

        adapter = new HousesListAdapter(getContext());
        binding.recyclerView.setAdapter(adapter);
        viewModel.getUsersHousesFlowable().observeOn(AndroidSchedulers.mainThread()).subscribe(
                (usersHousesList) ->
                {
                    adapter.setUsersHausesList(usersHousesList);
                    binding.recyclerView.getAdapter().notifyDataSetChanged();
                },

                (e) -> {}
        );

        SearchView searchView = (SearchView) binding.toolbar.getMenu().findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.setUserQuery(newText);
                return true;
            }
        });





        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }




}

