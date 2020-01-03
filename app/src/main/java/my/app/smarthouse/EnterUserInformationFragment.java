package my.app.smarthouse;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import my.app.smarthouse.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class EnterUserInformationFragment extends Fragment {


    public EnterUserInformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enter_user_information, container, false);
    }

}
