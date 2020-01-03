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
public class ChooseUserPasswordFragment extends Fragment {


    public ChooseUserPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_user_password, container, false);
    }

}
