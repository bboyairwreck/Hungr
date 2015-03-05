package edu.washington.echee.swipeviews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class LoginFragment extends Fragment {
    private Activity hostActivity;  // TODO Change Activity Type

    public LoginFragment() {}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.hostActivity = (Activity) activity; // TODO Change Activity Type
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button btnLogin = (Button) view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO change this variable name and hostActivity
                Intent swipeActivityIntent = new Intent(hostActivity, SwipeActivity.class);
                startActivity(swipeActivityIntent);
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
