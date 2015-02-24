package bg.mentormate.academy.reservations.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import bg.mentormate.academy.reservations.R;
import bg.mentormate.academy.reservations.database.DBConstants;

public class SplashScreen extends Activity {

    ProgressBar progressBar;
    TextView progressStatus;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    // An instance of the status broadcast receiver
    LoginServiceReceiver mLoginServiceReceiver;

    // Sets a tag to use in logging
    private static final String CLASS_TAG = "SplashScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressStatus = (TextView) findViewById(R.id.progressStatus);
    }

    @Override
    public void onResume() {
        super.onResume();

        startService();

        /*
         * Creates an intent filter for LoginServiceReceiver that intercepts broadcast Intents
         */

        // The filter's action is BROADCAST_ACTION
        IntentFilter statusIntentFilter = new IntentFilter(
                DBConstants.BROADCAST_ACTION);

        // Sets the filter's category to DEFAULT
        statusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);

        // Instantiates a new LoginServiceReceiver
        mLoginServiceReceiver = new LoginServiceReceiver(this, progressBar, progressStatus);

        // Registers the LoginServiceReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mLoginServiceReceiver,
                statusIntentFilter);
    }

    // Method to start the service
    public void startService() {
        startService(new Intent(this, LoginIntentService.class));
    }

    // Method to stop the service
    public void stopService() {
        stopService(new Intent(this, LoginIntentService.class));
    }

    public void broadcastIntent()
    {
        Intent intent = new Intent();
        intent.setAction("bg.mentormate.academy.reservations.CUSTOM_INTENT");
        sendBroadcast(intent);
    }

    /*
     * This callback is invoked when the system is about to destroy the Activity.
     */
    @Override
    public void onDestroy() {

        // If the LoginServiceReceiver still exists, unregister it and set it to null
        if (mLoginServiceReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mLoginServiceReceiver);
            mLoginServiceReceiver = null;
        }
        stopService();
        // Must always call the super method at the end.
        super.onDestroy();
    }

    /*
     * This callback is invoked when the system is stopping the Activity. It stops
     * background threads.
     */
    @Override
    protected void onStop() {

        // Cancel all the running threads managed by the PhotoManager
        super.onStop();
    }

}


