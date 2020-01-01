package com.example.smarthouse.UI;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.smarthouse.BaseAplication;
import com.example.smarthouse.R;
import com.example.smarthouse.adapters.housesListAdapter.HousesListAdapter;
import com.example.smarthouse.adapters.housesListAdapter.IOption;
import com.example.smarthouse.databinding.DialogChangeNameLayoutBinding;
import com.example.smarthouse.databinding.FragmentHousesListFragmnetBinding;
import com.example.smarthouse.repositorys.utilsData.MyArray;
import com.example.smarthouse.viewmodels.HousesListViewModel;
import com.example.smarthouse.viewmodels.ViewModelProviderFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class HousesListFragment extends BaseFragment implements IOption {



    private final int REED_STORAGE_PERMISSION_CODE = 10;
    private final int PICK_PICTURE_CODE= 11;

    private final int TAKE_PICTURE_CODE = 14;
    private final int CAMERA_PERMISSION_CODE = 15;



    @Inject
    protected ViewModelProviderFactory providerFactory;

    @Inject
    protected HousesListAdapter adapter;

    private HousesListViewModel viewModel;

    private FragmentHousesListFragmnetBinding binding;
    private DialogChangeNameLayoutBinding mDialogBinding;

    String mHouseId;
    String pictureFilePath;



    public HousesListFragment() {
    }


    @Override
    public void onAttach(@NonNull Context context) {
        ((BaseAplication) getActivity().getApplication())
                .getAuthCompoent()
                .getHousesListSubcomponentFactory()
                .create(this::onMenuItemClicked)
                .inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel = ViewModelProviders.of(this,providerFactory).get(HousesListViewModel.class);
        binding = FragmentHousesListFragmnetBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }




    @Override
    public void onResume() {
        super.onResume();
        makeAndConnectObservers();
    }

    @Override
    public void onMenuItemClicked(int menuItemId,String houseId) {
        mHouseId = houseId;
        switch (menuItemId) {
            case R.string.withCameraMenuId:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(hasPermissions(Manifest.permission.CAMERA)){
                        dispatchTakePictureIntent();
                    }
                    else{
                        requestPermissions(new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_CODE);
                    }
                }
                else {
                    dispatchTakePictureIntent();
                }
                break;
            case R.string.withStorageMenuId:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (hasPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        dispatchPickPictureIntent();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REED_STORAGE_PERMISSION_CODE);
                    }
                }
                else {
                    dispatchPickPictureIntent();
                }
                break;
            case  R.string.changeNameMenuId :
                createDialog();
                break;


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REED_STORAGE_PERMISSION_CODE :
                if (PackageManager.PERMISSION_GRANTED == (grantResults.length == 0 ? null : grantResults[0])) {
                    Toast.makeText(getContext(), "Permission request granted", Toast.LENGTH_LONG).show();
                    dispatchPickPictureIntent();
                }
                else {
                    Toast.makeText(getContext(),"Permission request denied", Toast.LENGTH_LONG).show();
                    mHouseId = null;
                }
                break;
            case  CAMERA_PERMISSION_CODE:
                if(PackageManager.PERMISSION_GRANTED == (grantResults.length == 0 ? null : grantResults[0]))
                {
                    Toast.makeText(getContext(), "Permission request granted", Toast.LENGTH_LONG).show();
                    dispatchTakePictureIntent();
                }
                else {
                    Toast.makeText(getContext(),"Permission request denied", Toast.LENGTH_LONG).show();
                    mHouseId = null;
                }
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == TAKE_PICTURE_CODE && resultCode == Activity.RESULT_OK ) {
            if(mHouseId == null || mHouseId.equals("") || pictureFilePath.equals("") || pictureFilePath == null) {
                Toast.makeText(getActivity().getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG);
            }
            else {
                Bitmap takenPicture = BitmapFactory.decodeFile(pictureFilePath);
                File file = new File(pictureFilePath);
                file.deleteOnExit();
                viewModel.saveBitmapToDatabase(takenPicture, mHouseId).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<String>() {
                    Disposable d;
                    @Override
                    public void onSubscribe(Disposable d) {
                        this.d = d;
                    }

                    @Override
                    public void onSuccess(String s) {
                        d.dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        d.dispose();
                    }
                });
            }
        }

        else if(requestCode == PICK_PICTURE_CODE && resultCode == Activity.RESULT_OK){
            if(data == null || mHouseId == null || mHouseId.equals(""))
            {
                Toast.makeText(getActivity().getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG);
            }
            else
            {
                try {
                    Uri pictureUri = data.getData();
                    if(hasPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Bitmap selectedImage = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),pictureUri);
                        viewModel.saveBitmapToDatabase(selectedImage, mHouseId).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<String>() {
                            Disposable d;
                            @Override
                            public void onSubscribe(Disposable d) {
                                this.d = d;
                            }

                            @Override
                            public void onSuccess(String s) {
                                d.dispose();
                            }

                            @Override
                            public void onError(Throwable e) {
                                d.dispose();
                            }
                        });
                    }
                    else{
                        Toast.makeText(getContext(),"Permission to read data was revoked",Toast.LENGTH_SHORT);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        mHouseId = null;
        pictureFilePath = null;
    }

    public boolean hasPermissions(String permission) {
        if(ContextCompat.checkSelfPermission(getContext(),permission) == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    public void makeAndConnectObservers(){
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

        binding.recyclerView.setAdapter(adapter);
        Disposable usersHousesDisposable =
                viewModel.getUsersHousesObservable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((usersHousesList) -> {
                                    adapter.setUsersHausesList(usersHousesList);
                                    binding.recyclerView.getAdapter().notifyDataSetChanged();
                                },
                                (e) -> Log.e("usersHousesError: ",e.getMessage())
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


        addDisposables(authStateDisposable,usersHousesDisposable);
    }


    public void dispatchPickPictureIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                .setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"Select picture"),PICK_PICTURE_CODE);
    }


    public void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try{
            photoFile = crateImageFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if(photoFile != null){
            Uri photoUri = FileProvider.getUriForFile(getContext(),"com.example.android.fileprovider",photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
            startActivityForResult(takePictureIntent,TAKE_PICTURE_CODE);
        }
    }


    private File crateImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
        pictureFilePath = image.getAbsolutePath();
        return image;
    }

    public void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mDialogBinding = DialogChangeNameLayoutBinding.inflate(getActivity().getLayoutInflater());
        AlertDialog dialog = builder
                .setTitle("New name")
                .setView(mDialogBinding.getRoot())
                .setNegativeButton("Cancle",(dialogInstance,id) ->{
                    mDialogBinding = null;
                    mHouseId = null;
                })
                .setPositiveButton("Confirm",(dialogInstace,id) -> {
                    viewModel.changeHouseName(mDialogBinding.houseName.getText().toString(),mHouseId).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
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
                    });
                    mDialogBinding = null;
                    mHouseId = null;
                })
                .setCancelable(true)
                .setOnCancelListener((dialogInstance) -> {
                    mDialogBinding = null;
                    mHouseId = null;
                    })
                .create();
        dialog.show();
    }



}

