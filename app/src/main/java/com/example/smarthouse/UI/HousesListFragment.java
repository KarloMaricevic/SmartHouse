package com.example.smarthouse.UI;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smarthouse.BaseAplication;
import com.example.smarthouse.R;
import com.example.smarthouse.adapters.HousesListAdapter;
import com.example.smarthouse.databinding.FragmentHousesListFragmnetBinding;
import com.example.smarthouse.viewmodels.HousesListViewModel;
import com.example.smarthouse.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


public class HousesListFragment extends Fragment {


    @Inject
    protected ViewModelProviderFactory providerFactory;

    @Inject
    protected HousesListAdapter adapter;

    private HousesListViewModel viewModel;

    private FragmentHousesListFragmnetBinding binding;


    public HousesListFragment() {
    }


    @Override
    public void onAttach(@NonNull Context context) {
        ((BaseAplication) getActivity().getApplication())
                .getAuthCompoent()
                .getHousesListSubcomponentFactory()
                .create()
                .inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel = ViewModelProviders.of(this,providerFactory).get(HousesListViewModel.class);
        binding = FragmentHousesListFragmnetBinding.inflate(inflater,container,false);


        //region AuthViewModel

        Disposable authStateDisposable = viewModel.getAuthState()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((authState) -> {
                    switch (authState) {

                        case UNAUTHENTICATED:
                            ((BaseAplication) getActivity().getApplication()).releseAuthComponent();
                            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_global_logInFragment);
                            break;
                    }
                },
                (e) -> Log.e("AuthObserverError:" ,e.getMessage()));

        //endregion

        binding.recyclerView.setAdapter(adapter);
        Disposable usersHousesDisposable = viewModel.getUsersHousesObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((usersHousesList) -> {
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


         viewModel.addViewDisposables(authStateDisposable,usersHousesDisposable);

        return binding.getRoot();
    }


    @Override
    public void onStop() {
        super.onStop();
        viewModel.clearViewDisposable();
    }
}

