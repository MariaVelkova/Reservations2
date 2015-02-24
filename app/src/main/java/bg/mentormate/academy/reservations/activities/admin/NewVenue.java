package bg.mentormate.academy.reservations.activities.admin;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import bg.mentormate.academy.reservations.R;
import bg.mentormate.academy.reservations.common.GetVenues;
import bg.mentormate.academy.reservations.common.PostRequestVenue;
import bg.mentormate.academy.reservations.common.SessionData;
import bg.mentormate.academy.reservations.common.Validator;
import bg.mentormate.academy.reservations.database.DBConstants;

public class NewVenue extends ActionBarActivity {
    SessionData sessionData = SessionData.getInstance();
    ImageView picChooser;
    Button createVenue;
    public static final int REQUEST_CODE = 1;
    Bitmap bitmap = null;
    EditText nameField;
    EditText cityField;
    EditText addressField;
    EditText phoneField;
    EditText latitudeField;
    EditText longitudeField;
    EditText hoursField;
    EditText capacityField;
    EditText workTimeField;
    String name;
    String city;
    String type;
    String address;
    String hours;
    String phone;
    String latitude;
    String longitude;
    String capacity;
    String workTime;
    String encodedImage;
    Spinner cities;
    Spinner types;
    int owner_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_venue);

        nameField = (EditText) findViewById(R.id.name);
        //cityField = (EditText) findViewById(R.id.city);
        addressField = (EditText) findViewById(R.id.address);
        phoneField = (EditText) findViewById(R.id.phone);
       // latitudeField = (EditText) findViewById(R.id.latitude);
       // longitudeField = (EditText) findViewById(R.id.longitude);
        hoursField = (EditText) findViewById(R.id.hours);
        capacityField = (EditText) findViewById(R.id.capacity);
        workTimeField = (EditText) findViewById(R.id.hours);
        owner_id = sessionData.getUser().getId();
        cities = (Spinner) findViewById(R.id.city);
        types = (Spinner) findViewById(R.id.type);

        ArrayAdapter<CharSequence> venueTypeAdapter;

        venueTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.venues_type_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        venueTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        types.setAdapter(venueTypeAdapter);

        ArrayAdapter<CharSequence> venueCityAdapter;
        venueCityAdapter = ArrayAdapter.createFromResource(this,
                R.array.cities_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        venueCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        cities.setAdapter(venueCityAdapter);



        picChooser = (ImageView) findViewById(R.id.venuePictureChooser);
        createVenue = (Button) findViewById(R.id.buttonCreatedVenue);
        picChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
                galleryIntent.setType("image/*");
                galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);


                Intent chooser = new Intent(Intent.ACTION_CHOOSER);
                chooser.putExtra(Intent.EXTRA_INTENT, galleryIntent);
                chooser.putExtra(Intent.EXTRA_TITLE, "Choose Action");

                Intent[] intentArray = {cameraIntent};
                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(chooser, REQUEST_CODE);
            }
        });
        createVenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameField.getText().toString();
                city = cities.getSelectedItem().toString();
                type = types.getSelectedItem().toString();
                hours = hoursField.getText().toString();
                phone = phoneField.getText().toString();
                address = addressField.getText().toString();
                capacity = capacityField.getText().toString();
                workTime = workTimeField.getText().toString();

                if (name.equals("") ||  hours.equals("") || phone.equals("")
                        || address.equals("") || capacity.equals("")
                        || workTime.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Fill Out All The Information", Toast.LENGTH_SHORT).show();
                } else {
                    if (bitmap == null) {
                        Toast.makeText(getApplicationContext(), "Please choose a picture", Toast.LENGTH_SHORT).show();
                    } else {

                        ByteArrayOutputStream streamS = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, streamS);
                        byte[] byteArray = streamS.toByteArray();
                        encodedImage = Base64.encodeToString(byteArray, Base64.URL_SAFE);

                        PostRequestVenue postRequestVenue = null;
                        try {
                            postRequestVenue = new PostRequestVenue(NewVenue.this, name, type, phone, city, address, "0", "0",
                                    workTime, capacity, Integer.toString(owner_id), encodedImage);
                            int a =2;
                        } catch (NetworkErrorException e) {
                            e.printStackTrace();
                        }
                        String result = "";
                        if (postRequestVenue != null) {
                            try {
                                result = postRequestVenue.execute().get();
                                updateVenueDB();
                                Toast.makeText(getApplicationContext(), "Venue Created Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
    }

    private void updateVenueDB() {
        GetVenues getVenuesTask = null;
        try {
            getVenuesTask = new GetVenues(this, "", "", "", 0);
        } catch (NetworkErrorException e) {
            e.printStackTrace();
        }
        String venuesResult = "";
        if (getVenuesTask != null) {
            try {
                venuesResult = getVenuesTask.execute().get();
                int fi = 0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        if (!Validator.isEmpty(venuesResult)) {
            JSONObject resultJSON = null;
            try {
                resultJSON = new JSONObject(venuesResult);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (resultJSON != null) {
                JSONArray venues = null;
                try {
                    venues = resultJSON.getJSONArray("venues");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (venues != null) {
                    for (int i = 0; i < venues.length(); i++) {

                        JSONObject venue = null;
                        try {
                            venue = venues.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (venue != null) {
                            ContentValues values = new ContentValues();
                            try {
                                values.put(DBConstants.DB_TABLE_VENUES_ID, venue.getInt(DBConstants.DB_TABLE_VENUES_ID));
                                values.put(DBConstants.DB_TABLE_VENUES_NAME, venue.getString(DBConstants.DB_TABLE_VENUES_NAME));
                                values.put(DBConstants.DB_TABLE_VENUES_TYPE, venue.getString(DBConstants.DB_TABLE_VENUES_TYPE));
                                values.put(DBConstants.DB_TABLE_VENUES_PHONE, venue.getString(DBConstants.DB_TABLE_VENUES_PHONE));
                                values.put(DBConstants.DB_TABLE_VENUES_CITY, venue.getString(DBConstants.DB_TABLE_VENUES_CITY));
                                values.put(DBConstants.DB_TABLE_VENUES_ADDRESS, venue.getString(DBConstants.DB_TABLE_VENUES_ADDRESS));
                                values.put(DBConstants.DB_TABLE_VENUES_LAT, venue.getDouble(DBConstants.DB_TABLE_VENUES_LAT));
                                values.put(DBConstants.DB_TABLE_VENUES_LON, venue.getDouble(DBConstants.DB_TABLE_VENUES_LON));
                                values.put(DBConstants.DB_TABLE_VENUES_CAPCITY, venue.getInt(DBConstants.DB_TABLE_VENUES_CAPCITY));
                                values.put(DBConstants.DB_TABLE_VENUES_WORKTIME, venue.getString(DBConstants.DB_TABLE_VENUES_WORKTIME));
                                values.put(DBConstants.DB_TABLE_VENUES_OWNER_ID, venue.getInt(DBConstants.DB_TABLE_VENUES_OWNER_ID));
                                values.put(DBConstants.DB_TABLE_VENUES_IMAGE, venue.getString(DBConstants.DB_TABLE_VENUES_IMAGE));
                                values.put(DBConstants.DB_TABLE_VENUES_CREATED, venue.getInt(DBConstants.DB_TABLE_VENUES_CREATED));
                                values.put(DBConstants.DB_TABLE_VENUES_LAST_UPDATED, venue.getInt(DBConstants.DB_TABLE_VENUES_LAST_UPDATED));
                                String venueId = Integer.toString(venue.getInt(DBConstants.DB_TABLE_VENUES_ID));
                                int row_count = getContentResolver().update(DBConstants.CONTENT_URI_VENUES, values, "id = ?", new String[]{venueId});
                                if (row_count == 0) {
                                    Uri uri = getContentResolver().insert(DBConstants.CONTENT_URI_VENUES, values);
                                }
                                int y = 0;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data.getData() != null) {
                try {
                    if (bitmap != null) {
                        bitmap.recycle();
                    }

                    InputStream stream = getContentResolver().openInputStream(data.getData());
                    bitmap = BitmapFactory.decodeStream(stream);
                    picChooser.setImageResource(R.drawable.yes);
                    stream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                bitmap = (Bitmap) data.getExtras().get("data");
                picChooser.setImageResource(R.drawable.yes);
            }

            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
