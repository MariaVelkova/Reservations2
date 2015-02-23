package bg.mentormate.academy.reservations.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;

import bg.mentormate.academy.reservations.R;
import bg.mentormate.academy.reservations.common.SessionData;
import bg.mentormate.academy.reservations.common.Validator;
import bg.mentormate.academy.reservations.models.Venue;

public class VenueDetailActivity extends ActionBarActivity implements View.OnClickListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    Venue venue = null;
    DialogFragment reservationDialog = null;
    SessionData sessionData = SessionData.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_detail);
        setUpMapIfNeeded();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        Intent callerIntent = getIntent();
        if (callerIntent != null) {
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
        }

        if (venue != null) {
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
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link com.google.android.gms.maps.SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        if (venue != null) {
            Log.d("setUpMap", venue.getName());
            LatLng venuePosition = new LatLng(venue.getLat(), venue.getLon());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(venuePosition, 5));
            mMap.addMarker(new MarkerOptions().position(venuePosition).title(venue.getName()));
        }
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
