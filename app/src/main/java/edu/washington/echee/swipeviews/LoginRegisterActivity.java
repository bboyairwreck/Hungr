package edu.washington.echee.swipeviews;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;


public class LoginRegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        LoginFragment loginFragment = new LoginFragment();

        ft.add(R.id.loginActivityRoot, loginFragment);
        ft.commit();


        if (savedInstanceState == null) {
//            FragmentManager fm = getFragmentManager();
//            fm.beginTransaction()
//                    .add(R.id.container, new PlaceholderFragment())
//                    .commit();
        }
    }
}
