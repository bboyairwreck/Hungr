package edu.washington.echee.swipeviews;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
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

        ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left);

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
        ft.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
        ft.replace(R.id.loginActivityRoot, fragment);   // replace instead of add
        ft.addToBackStack("Register Fragment");
        ft.commit();
    }

    public void showSwipe() {
        Log.i("fragment", "loading Swipe fragment");
        Intent swipeActivityIntent = new Intent(this, SwipeActivity.class);
        startActivity(swipeActivityIntent);

        // TODO enable finish after testing so cannot return to sign-in/registration
        // finish();
    }
}
