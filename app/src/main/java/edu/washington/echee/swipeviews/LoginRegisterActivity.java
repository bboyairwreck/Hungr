package edu.washington.echee.swipeviews;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;


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

        }
    }

    public void showRegister(){
        Log.i("fragment", "loading Register fragment");
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        RegisterFragment fragment = new RegisterFragment();

        ft.replace(R.id.loginActivityRoot, fragment);   // replace instead of add
        ft.addToBackStack("Register Fragment");
        ft.commit();
    }
}
