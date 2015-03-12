package edu.washington.echee.swipeviews;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    private DownloadManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE); // Add more filters here that you want the receiver to listen to
        registerReceiver(receiver, filter);

        HungrApp hungrApp = (HungrApp)getApplicationContext();

        // Check internet connection
        if (hungrApp.haveNetworkConnection(this) == false) {
            if (hungrApp.isAirplaneModeOn(this)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage("Please turn Airplane Mode off.")
                        .setTitle("No access to internet");

                // Add the buttons
                builder.setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent settingsIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(settingsIntent);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                Toast.makeText(this, "No internet connection to check for updates", Toast.LENGTH_SHORT).show();
            }
        }
    }

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

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            dm = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);

            Log.i("QuizApp BroadcastReceiver", "onReceive of registered download reciever");

            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                Log.i("QuizApp BroadcastReceiver", "download complete!");
                long downloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);



                // if the downloadID exists
                if (downloadID != 0) {

                    // Check status
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadID);
                    Cursor c = dm.query(query);
                    if(c.moveToFirst()) {
                        int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        Log.d("DM Sample","Status Check: "+status);
                        switch(status) {
                            case DownloadManager.STATUS_PAUSED:
                            case DownloadManager.STATUS_PENDING:
                            case DownloadManager.STATUS_RUNNING:
                                break;
                            case DownloadManager.STATUS_SUCCESSFUL:
                                ParcelFileDescriptor file;
                                StringBuffer strContent = new StringBuffer("");

                                try {
                                    // Get file from Download Manager
                                    file = dm.openDownloadedFile(downloadID);
                                    FileInputStream fis = new FileInputStream(file.getFileDescriptor());

                                    // convert file to String
                                    String jsonString = ((HungrApp)getApplicationContext()).readJSONFile(fis);

                                    // write string to quizData.json
                                    ((HungrApp)getApplicationContext()).writeToFile(jsonString);
                                    Log.i("QuizApp JSON downloaded", jsonString.substring(0, 50) + "...}");

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case DownloadManager.STATUS_FAILED:
                                showAlert();
                                break;
                        }
                    }
                }
            }
        }
    };

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Retry to download or quit application?")
                .setTitle("Download Failed");

        // Add the buttons
        builder.setPositiveButton("Retry?", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // If clicked RETRY button, retry to download
                Log.i("HungrBaseActivity", "clicked Retry. Retrying to download");

                String url = Constants.JSON_URL;

                dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                dm.enqueue(request);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.i("HungrBaseActivity", "clicked Cancel. Not retrying to download.");
            }
        });
    }
}
