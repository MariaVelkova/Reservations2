package bg.mentormate.academy.reservations.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

//import com.example.pc.its.CustomDialogFragment;
import bg.mentormate.academy.reservations.R;


public class ReservationDetails extends ActionBarActivity {
    TextView isApprovedText;
    TextView reservationInfo;
    TextView venueName;
    ImageView picture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_details);
        isApprovedText = (TextView) findViewById(R.id.approved);
        reservationInfo = (TextView) findViewById(R.id.reservationInfo);
        venueName = (TextView) findViewById(R.id.venueName);
        picture = (ImageView) findViewById(R.id.venuePicture);

        Intent intent = getIntent();
        String date = intent.getStringExtra("date");
        int peopleCount = intent.getIntExtra("peopleCount", 0);
        String user = intent.getStringExtra("user");
        String name = intent.getStringExtra("venueName");
        String city = intent.getStringExtra("city");
        int picInt = intent.getIntExtra("venuePicture", 0);
        boolean approved = intent.getBooleanExtra("approved", false);

        if (approved) {
            isApprovedText.setText("Yes");
        }
        reservationInfo.setText("Reservation for " + peopleCount + " by user " + user + " for " + date );
        venueName.setText(name + " restaurant in " + city);
        picture.setImageResource(picInt);


    }
//    public void cancel(View v) {
//        DialogFragment dialog = new CustomDialogFragment();
//        Bundle args = new Bundle();
//        args.putString("message", "Are you sure you want to cancel?");
//        args.putString("positive", "Yes");
//        args.putString("negative", "No");
//        dialog.setArguments(args);
//        dialog.show(getSupportFragmentManager(), "TAG");
//    }
}
