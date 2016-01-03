package edu.washington.echee.swipeviews;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class LoginRegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        LoginFragment loginFragment = new LoginFragment();

        ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left);

        ft.add(R.id.loginActivityRoot, loginFragment, Constants.LOGIN_FRAGMENT_TAG);
        ft.commit();

        Button btnSkipLogIn = (Button) findViewById(R.id.btnSkipLogIn);
        btnSkipLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSwipe();
            }
        });

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

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Check if alarm has already been created
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        boolean alarmUP = (PendingIntent.getBroadcast(this, Constants.MY_ALARM, alarmIntent,
                PendingIntent.FLAG_NO_CREATE) != null);

        if (alarmUP) {
            DownloadService.startOrStopAlarm(this, false);
        }
    }
}
