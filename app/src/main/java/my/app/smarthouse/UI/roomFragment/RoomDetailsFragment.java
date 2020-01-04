package my.app.smarthouse.UI.roomFragment;


import android.app.AlertDialog;
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

import my.app.smarthouse.BaseAplication;
import my.app.smarthouse.R;
import my.app.smarthouse.UI.BaseFragment;
import my.app.smarthouse.adapters.RoomFragment.RoomDetails.ItemDecoration;
import my.app.smarthouse.adapters.RoomFragment.RoomDetails.MyMultiTypeAdapter;
import my.app.smarthouse.adapters.RoomFragment.RoomDetails.viewHolders.RoomAdapterCallback;
import my.app.smarthouse.data.roomData.Door;
import my.app.smarthouse.data.roomData.Light;
import my.app.smarthouse.data.roomData.Temperature;
import my.app.smarthouse.databinding.DialogChangeDoorLockBinding;
import my.app.smarthouse.databinding.DialogChangeLightTurnedOnBinding;
import my.app.smarthouse.databinding.DialogChangeTemperatreBinding;
import my.app.smarthouse.databinding.FragmentRoomDetailsBinding;
import my.app.smarthouse.viewmodels.RoomViewModel;
import my.app.smarthouse.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoomDetailsFragment extends BaseFragment implements RoomAdapterCallback {


    @Inject
    ViewModelProviderFactory mViewModelFactory;

    @Inject
    MyMultiTypeAdapter mAdapter;

    FragmentRoomDetailsBinding mBinding;

    RoomViewModel mViewModel;


    DialogChangeDoorLockBinding mDoorDialogBinding;
    DialogChangeLightTurnedOnBinding mLightDialogBinding;
    DialogChangeTemperatreBinding mTeemperatureBinding;

    public RoomDetailsFragment() {
    }


    @Override
    public void onAttach(@NonNull Context context) {
        ((BaseAplication) getActivity().getApplication())
                .getmAuthCompoent()
                .getRoomDetailsSubcomponentFactory()
                .create(this)
                .inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mViewModel = ViewModelProviders.of(getParentFragment(), mViewModelFactory).get(RoomViewModel.class);

        mBinding = FragmentRoomDetailsBinding.inflate(inflater,container,false);

        ItemDecoration itemDecoration = new ItemDecoration(12,25);
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        mBinding.recyclerView.addItemDecoration(itemDecoration);
        mBinding.recyclerView.setAdapter(mAdapter);


        return mBinding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        Disposable observableDataForRoomDisposable =
                mViewModel
                        .getObservableDataForRoomDetailsAdapter()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                (item) -> {
                                    if(item.size() == 0)
                                    {
                                        mAdapter.changeDataSet(item);
                                        mBinding.errorTextView.setVisibility(View.VISIBLE);
                                    }
                                    else {
                                        mBinding.errorTextView.setVisibility(View.INVISIBLE);
                                        mAdapter.changeDataSet(item);
                                    }
                                },
                                (e) -> Log.e("RoomDetailsDataError: " , e.getMessage())
                        );

        addDisposables(observableDataForRoomDisposable);
    }

    @Override
    public void changeValueOf(Door door) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mDoorDialogBinding = DialogChangeDoorLockBinding.inflate(getActivity().getLayoutInflater());
        mDoorDialogBinding.setDoor(door);
        AlertDialog dialog = builder
                .setTitle(R.string.dialogDoorTitle)
                .setView(mDoorDialogBinding.getRoot())
                .setNegativeButton(R.string.dialogNegativeButton,(dialogInstance,which) ->{
                    mDoorDialogBinding = null;
                    return;
                })
                .setPositiveButton(R.string.dialogPositiveButton,(dialogInstace,which) -> {
                    mViewModel.changeDoorLocked(!mDoorDialogBinding.locked.isChecked(),mDoorDialogBinding.getDoor())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
                                Disposable d;
                                @Override
                                public void onSubscribe(Disposable d) {
                                    this.d = d;
                                }

                                @Override
                                public void onComplete() {
                                    d.dispose();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    d.dispose();
                                }
                            }
                    );
                    mDoorDialogBinding  = null;
                    return;
                    })
                .setCancelable(true)
                .setOnCancelListener( (dialogInstance) -> {
                    mDoorDialogBinding = null;
                })
                .create();
        dialog.show();
    }

    @Override
    public void changeValueOf(Light light) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mLightDialogBinding = DialogChangeLightTurnedOnBinding.inflate(getActivity().getLayoutInflater());
        mLightDialogBinding.setLight(light);
        AlertDialog dialog = builder
                .setTitle(R.string.dialogLightTitle)
                .setView(mLightDialogBinding.getRoot())
                .setNegativeButton(R.string.dialogNegativeButton,(dialogInstance,which) ->{
                    mLightDialogBinding = null;
                    return;
                })
                .setPositiveButton(R.string.dialogPositiveButton,(dialogInstace,which) -> {
                    mViewModel.changeLightOn(mLightDialogBinding.locked.isChecked(),mLightDialogBinding.getLight())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new CompletableObserver() {
                                Disposable d;
                                @Override
                                public void onSubscribe(Disposable d) {
                                    this.d = d;
                                }

                                @Override
                                public void onComplete() {
                                    d.dispose();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    d.dispose();
                                }
                            }
                    );
                    mLightDialogBinding  = null;
                    return;
                })
                .setCancelable(true)
                .setOnCancelListener( (dialogInstance) -> {
                    mLightDialogBinding = null;
                })
                .create();
        dialog.show();

    }

    @Override
    public void changeValueOf(Temperature temperature) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mTeemperatureBinding = DialogChangeTemperatreBinding.inflate(getActivity().getLayoutInflater());
        mTeemperatureBinding.setTemperature(temperature);
        mTeemperatureBinding.numberPicker.setMinValue(15);
        mTeemperatureBinding.numberPicker.setMaxValue(32);
        mTeemperatureBinding.numberPicker.setValue(temperature.getTargeted());
        AlertDialog dialog = builder
                .setTitle(R.string.dialogTemperatureTitle)
                .setView(mTeemperatureBinding.getRoot())
                .setNegativeButton(R.string.dialogNegativeButton,(dialogInstance,which) ->{
                    mTeemperatureBinding = null;
                    return;
                })
                .setPositiveButton(R.string.dialogPositiveButton,(dialogInstace,which) -> {
                    mViewModel.changeRoomTemeperature(mTeemperatureBinding.numberPicker.getValue(), mTeemperatureBinding.getTemperature())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new CompletableObserver() {
                                           Disposable d;

                                @Override
                                           public void onSubscribe(Disposable d) {
                                               this.d = d;
                                           }

                                           @Override
                                           public void onComplete() {
                                               d.dispose();
                                           }

                                           @Override
                                           public void onError(Throwable e) {
                                               d.dispose();
                                           }
                                       }
                            );
                })
                .setCancelable(true)
                .setOnCancelListener( (dialogInstance) -> {
                    mTeemperatureBinding = null;
                })
                .create();
        dialog.show();


    }
}
