package bg.mentormate.academy.reservations.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;

import bg.mentormate.academy.reservations.R;
import bg.mentormate.academy.reservations.common.SessionData;
import bg.mentormate.academy.reservations.common.Validator;
import bg.mentormate.academy.reservations.database.DBConstants;
import bg.mentormate.academy.reservations.models.Venue;

public class VenueDetailActivity extends ActionBarActivity implements View.OnClickListener {

    Venue venue = null;
    DialogFragment reservationDialog = null;
    SessionData sessionData = SessionData.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_detail);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }


        Intent callerIntent = getIntent();
        if (callerIntent != null) {
            Uri uri = callerIntent.getData();
            if (uri != null) {
                Cursor cursor = managedQuery(uri, null, null, null, null);

                if (cursor != null) {

                    cursor.moveToFirst();

                    try {
                        venue = new Venue(
                                cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.DB_TABLE_VENUES_ID)),
                                cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.DB_TABLE_VENUES_NAME)),
                                cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.DB_TABLE_VENUES_TYPE)),
                                cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.DB_TABLE_VENUES_PHONE)),
                                cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.DB_TABLE_VENUES_ADDRESS)),
                                cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.DB_TABLE_VENUES_CITY)),
                                cursor.getDouble(cursor.getColumnIndexOrThrow(DBConstants.DB_TABLE_VENUES_LAT)),
                                cursor.getDouble(cursor.getColumnIndexOrThrow(DBConstants.DB_TABLE_VENUES_LON)),
                                cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.DB_TABLE_VENUES_WORKTIME)),
                                cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.DB_TABLE_VENUES_CAPCITY)),
                                cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.DB_TABLE_VENUES_OWNER_ID)),
                                cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.DB_TABLE_VENUES_IMAGE)));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }

            /****
            String venueString = callerIntent.getStringExtra("venue");
            JSONObject venueJSON = null;
            try {
                venueJSON = new JSONObject(venueString);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (venueJSON != null) {
                try {
                    venue = new Venue(venueJSON.getInt("id"), venueJSON.getString("name"), venueJSON.getString("type"), venueJSON.getString("phone"), venueJSON.getString("address"), venueJSON.getString("city"), venueJSON.getDouble("lat"), venueJSON.getDouble("lon"), venueJSON.getString("worktime"), venueJSON.getInt("capacity"), venueJSON.getInt("owner_id"), venueJSON.getString("image"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
             */
        }

        if (venue != null) {
            getSupportActionBar().setTitle(venue.getName());
            final int venueId = venue.getId();
            sessionData.setCurerntVenueId(venueId);
            reservationDialog = DialogFragmentReserve.getInstance(venueId);
            final String venuePhoneNumber = venue.getPhone();
            ImageView venueImage = (ImageView) findViewById(R.id.venueImage);
            TextView venueName = (TextView) findViewById(R.id.venueName);
            TextView venueAddress = (TextView) findViewById(R.id.venueAddress);
            TextView venuePhone = (TextView) findViewById(R.id.venuePhone);
            TextView venueWorkTime = (TextView) findViewById(R.id.venueWorktime);
            TextView venueCapacity = (TextView) findViewById(R.id.venueCapacity);
            TextView venueType = (TextView) findViewById(R.id.venueType);

            String pictureArray = venue.getImage();
            if (!Validator.isEmpty(pictureArray)) {
                byte[] imgbytes = Base64.decode(pictureArray, Base64.URL_SAFE);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imgbytes);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(byteArrayInputStream);
                venueImage.setImageDrawable(bitmapDrawable);
            }
            venueName.setText(venue.getName());
            venueAddress.setText(venue.getCity() + ", " + venue.getAddress());
            venuePhone.setText(venuePhoneNumber);
            venuePhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Validator.isEmpty(venuePhoneNumber)) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + venuePhoneNumber));
                        startActivity(intent);
                    }
                }
            });
            venueType.setText(venue.getType());
            venueWorkTime.setText(venue.getWorktime());
            venueCapacity.setText(String.format(getResources().getString(R.string.venue_capacity), venue.getCapacity()));


            Button reservationBtn = (Button) findViewById(R.id.reservationBtn);

            if (sessionData.getUser().getType() == 2) {
                reservationBtn.setVisibility(View.GONE);
            } else {
                reservationBtn.setOnClickListener(this);
            }
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_blank, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reservationBtn:
                reservationDialog.show(getSupportFragmentManager(), "DialogFragmentReserve");
        }
    }

}
