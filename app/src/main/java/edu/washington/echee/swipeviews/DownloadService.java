package edu.washington.echee.swipeviews;

import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DownloadService extends IntentService {
    private DownloadManager dm;
    private long enqueue;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("DownloadService", "entered onHandleIntent()");

        String url = "http://ericchee.com/hungr/data.json";

        if (HungrApp.haveNetworkConnection(this)) {
            Log.i("DownloadService", "should be downloading here");

            dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            enqueue = dm.enqueue(request);
        } else {
            Log.i("DownloadService", "No internet connection. Did not attempt to download.");
        }
    }

    public static void startOrStopAlarm(Context context, boolean on) {
        Log.i("DownloadService", "startOrStopAlarm on = " + on);

        Intent alarmReceiverIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Constants.MY_ALARM, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        if (on) {
            int refreshInterval = 2 * 6000;   // TODO CHANGE BACK TO 60000

            Log.i("DownloadService", "setting alarm to " + refreshInterval);

            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), refreshInterval, pendingIntent);
        }
        else {
            manager.cancel(pendingIntent);
            pendingIntent.cancel();

            Log.i("DownloadService", "Stopping alarm");
        }
    }
}
