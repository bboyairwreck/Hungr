package edu.washington.echee.swipeviews;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class RegisterFragment extends Fragment {
    private static final String ARG_PARAM1 = "username";
    private static final String ARG_PARAM2 = "password";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Activity hostActivity;


    public RegisterFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        return view;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.hostActivity = (Activity) activity; // TODO Change Activity Type

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
