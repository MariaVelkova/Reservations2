package bg.mentormate.academy.reservations.activities.admin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import bg.mentormate.academy.reservations.R;
import bg.mentormate.academy.reservations.common.PostRequestVenue;

public class NewVenue extends ActionBarActivity {

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
    String address;
    String hours;
    String phone;
    String latitude;
    String longitude;
    String capacity;
    String workTime;
    String encodedImage;
    int owner_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_venue);

        nameField = (EditText) findViewById(R.id.name);
        cityField = (EditText) findViewById(R.id.city);
        addressField = (EditText) findViewById(R.id.address);
        phoneField = (EditText) findViewById(R.id.phone);
        latitudeField = (EditText) findViewById(R.id.latitude);
        longitudeField = (EditText) findViewById(R.id.longitude);
        hoursField = (EditText) findViewById(R.id.hours);
        capacityField = (EditText) findViewById(R.id.capacity);
        workTimeField = (EditText) findViewById(R.id.hours);
        owner_id = getIntent().getIntExtra("owner_id", 0);


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
                city = cityField.getText().toString();
                hours = hoursField.getText().toString();
                phone = phoneField.getText().toString();
                address = addressField.getText().toString();
                latitude = latitudeField.getText().toString();
                longitude = longitudeField.getText().toString();
                capacity = capacityField.getText().toString();
                workTime = workTimeField.getText().toString();

                if (name.equals("") || city.equals("") || hours.equals("") || phone.equals("")
                        || address.equals("") || latitude.equals("") || longitude.equals("") || capacity.equals("")
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

                        PostRequestVenue postRequestVenue = new PostRequestVenue(name, "1", phone, city, address, latitude, longitude,
                                workTime, capacity, (owner_id + " "), encodedImage);
                        postRequestVenue.execute();
                    }
                }
            }
        });
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
