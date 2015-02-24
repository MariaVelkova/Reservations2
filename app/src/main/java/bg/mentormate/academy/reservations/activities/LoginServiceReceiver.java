package bg.mentormate.academy.reservations.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import bg.mentormate.academy.reservations.R;
import bg.mentormate.academy.reservations.activities.admin.AdminActivity;
import bg.mentormate.academy.reservations.database.DBConstants;

/**
 * Created by Maria on 2/15/2015.
 */
public class LoginServiceReceiver extends BroadcastReceiver {

        ProgressBar progressBar;
        TextView progressStatus;
        Context context;
        public LoginServiceReceiver(Context context, ProgressBar progressBar, TextView progressStatus) {
            this.context = context;
            this.progressBar = progressBar;
            this.progressStatus = progressStatus;
        }


        @Override
        public void onReceive(Context context, Intent intent) {

            Intent redirectIntent;

            /*
             * Gets the status from the Intent's extended data, and chooses the appropriate action
             */
            int status = intent.getIntExtra(DBConstants.EXTENDED_DATA_STATUS,
                    DBConstants.STATE_ACTION_COMPLETE);
            switch (status) {
                // Logs "started" state
                case DBConstants.STATE_ACTION_STARTED:
                    progressStatus.setText(context.getResources().getString(R.string.loading));
                    if (DBConstants.LOGD) {
                        Log.d("LoginServiceReceiver", "State: STARTED");
                    }
                    break;
                case DBConstants.STATE_ACTION_LOGING:

                    progressStatus.setText(context.getResources().getString(R.string.loging));
                    if (DBConstants.LOGD) {
                        Log.d("LoginServiceReceiver", "State: LOGING");
                    }
                    break;
                case DBConstants.STATE_ACTION_LOGGED_USER:
                    redirectIntent = new Intent(context,MainActivity.class);
                    redirectIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(redirectIntent);
                    context.stopService(intent);
                    break;
                case DBConstants.STATE_ACTION_LOGGED_ADMIN:
                    redirectIntent = new Intent(context, AdminActivity.class);
                    redirectIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(redirectIntent);
                    context.stopService(intent);
                    break;

                case DBConstants.STATE_ACTION_LOGING_FAILED:

                    redirectIntent = new Intent(context,LoginActivity.class);
                    redirectIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(redirectIntent);
                    context.stopService(intent);
                    break;
//                case DBConstants.STATE_ACTION_CONNECTING_FAILED:
//
//                    progressStatus.setText(context.getResources().getString(R.string.no_network));
//                    context.stopService(intent);
//                    if (DBConstants.LOGD) {
//                        Log.d("LoginServiceReceiver", "State: CONNECTING FAILED");
//                    }
//                    break;
                default:
                    break;
            }

        }




}
