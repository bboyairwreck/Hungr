package edu.washington.echee.swipeviews;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Steven on 3/11/15.
 */

/*
To get drawer on an activity simply make that activity extend this one and add:

if (super.onNavDrawerButtonPress(item)) { //Required to activate drawer
    return true;
}

To the onOptionsItemSelected function
 */
public class HungrBaseActivity extends ActionBarActivity {
    private Context ctx;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;

    public void createNavDrawer(Context context) {
        ctx = context;
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dlDrawerLayout);
        mDrawerToggle = navToggle((Activity) context);

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        String[] stringArr = getResources().getStringArray(R.array.nav_drawer_list);
        ArrayList<String> navDrawerList = new ArrayList<String>(Arrays.asList(stringArr)); //Create nav_drawer elements list
        ListView lvNavDrawer = (ListView) findViewById(R.id.lvNavDrawer);
        lvNavDrawer.setAdapter(new NavDrawerListAdapter(context, R.layout.nav_drawer_list_layout, navDrawerList));
        lvNavDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: openHome(); //Open Home
                        break;
                    case 1: openSettings(); //Open settings
                        break;
                    case 2: openAbout(); //Open About
                }
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public boolean onNavDrawerButtonPress(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        return mDrawerToggle.onOptionsItemSelected(item);
    }

    public ActionBarDrawerToggle navToggle (Activity act) {
        return new ActionBarDrawerToggle(act, mDrawerLayout, R.drawable.ic_drawer,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(GravityCompat.START);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen); //Hides/Shows item
        return super.onPrepareOptionsMenu(menu);
    }

    private void openHome() {
        Intent swipeActivityIntent = new Intent(ctx, SwipeActivity.class);
        startActivity(swipeActivityIntent);
    }

    private void openSettings() {
        Intent swipeActivityIntent = new Intent(ctx, SettingsActivity.class);
        startActivity(swipeActivityIntent);
    }

    private void openAbout() {
        Intent swipeActivityIntent = new Intent(ctx, AboutActivity.class);
        startActivity(swipeActivityIntent);
    }
}
