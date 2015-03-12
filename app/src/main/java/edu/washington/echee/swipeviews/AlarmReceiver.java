package edu.washington.echee.swipeviews;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    private DownloadManager dm;
    private long enqueue;

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("AlarmReceiver", "Alarm has looped. entered onReceive().");

        showToast(context);

        Intent downloadServiceIntent = new Intent(context, DownloadService.class);
        context.startService(downloadServiceIntent);
    }

    private void showToast(Context context){
        String url = Constants.JSON_URL;

        String toastMessage = "Hungry is checking updates";
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
    }
}
