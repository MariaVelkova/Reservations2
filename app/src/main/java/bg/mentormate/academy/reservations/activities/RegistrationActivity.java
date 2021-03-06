package bg.mentormate.academy.reservations.activities;

import android.accounts.NetworkErrorException;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import bg.mentormate.academy.reservations.R;
import bg.mentormate.academy.reservations.common.FileHelper;
import bg.mentormate.academy.reservations.common.PostRequest;
import bg.mentormate.academy.reservations.common.SessionData;
import bg.mentormate.academy.reservations.common.Validator;

public class RegistrationActivity extends ActionBarActivity {

    private final String TAG = getClass().getSimpleName();
    private final Charset UTF8_CHARSET = Charset.forName("UTF-8");

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_OPEN = 2;

    ImageView avatarImage;
    EditText userFirstName;
    EditText userLastName;
    EditText userPassword;
    EditText userPassword2;
    EditText userEmail;
    EditText userPhone;
    CheckBox userType;
    Spinner userCity;

    Button registerButton;

    Bitmap avatar = null;
    byte[] avatarByteArray = null;

    SessionData sessionData = SessionData.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        avatarImage  = (ImageView)findViewById(R.id.avatarImage);
        userFirstName = (EditText) findViewById(R.id.userFirstName);
        userLastName = (EditText) findViewById(R.id.userLastName);
        userPassword = (EditText) findViewById(R.id.userPassword);
        userPassword2 = (EditText) findViewById(R.id.userPassword2);
        userEmail = (EditText) findViewById(R.id.userEmail);
        userPhone = (EditText) findViewById(R.id.userPhone);
        userType = (CheckBox) findViewById(R.id.owner);

        userCity = (Spinner) findViewById(R.id.userCity);
/*
        // Create an ArrayAdapter using the string array and a default spinner layout
        List<String> list = new ArrayList<String>();
        ArrayList<City> citiesArray = null;
        citiesArray = sessionData.getCities();
        if (citiesArray == null) {
            citiesArray = new ArrayList<City>();
            GetCities getCitiesTask = new GetCities();
            String result = "";
            try {
                result = getCitiesTask.execute().get();
                int i = 0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if (Validator.isEmpty(result)) {
                //Toast.makeText(this, "Sorry!\nSomething went wrong\nPlease check your internet connection and try again", Toast.LENGTH_SHORT).show();
            } else {
                JSONObject resultJSON = null;
                try {
                    resultJSON = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (resultJSON != null) {
                    int code = 0;
                    String message = "";
                    JSONObject data = null;
                    try {
                        code = resultJSON.getInt("code");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        message = resultJSON.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (code == 1) {
                        JSONArray citiesJSON = null;
                        try {
                            citiesJSON = resultJSON.getJSONArray("cities");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (citiesJSON != null) {
                            for (int r = 0; r < citiesJSON.length(); r++) {
                                try {
                                    JSONObject cityJSON = citiesJSON.getJSONObject(r);
                                    City city = new City(cityJSON.getInt("id"), cityJSON.getString("name"));
                                    citiesArray.add(city);
                                    list.add(city.getName());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            sessionData.setCities(citiesArray);
                        }
                    }
                }
            }
        } else {
            for (int c=0; c < citiesArray.size(); c++) {
                list.add(citiesArray.get(c).getName());
            }
        }


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userCity.setAdapter(dataAdapter);
*/
        ArrayAdapter<CharSequence> cityAdapter;
        cityAdapter = ArrayAdapter.createFromResource(this,
                R.array.cities_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        userCity.setAdapter(cityAdapter);

        registerButton = (Button) findViewById(R.id.registerButton);
    }

    public void openUploadImageDialog(View v) {
        Log.d("RegistrationActivity","openUploadImageDialog");

        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Log.d("RegistrationActivity","PackageManager.FEATURE_CAMERA");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.pick_action);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item);
            adapter.add("Image");
            adapter.add("Take Picture");
            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    switch (item) {
                        case 0:
                            selectImage();
                            break;
                        default:
                            takePicture();
                            break;
                    }
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            Log.d("RegistrationActivity","NO PackageManager.FEATURE_CAMERA");
            selectImage();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_OPEN:
                    Log.d("onActivityResult","REQUEST_IMAGE_OPEN");
                    try {
                        avatar = getBitmapFromUri(data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case REQUEST_IMAGE_CAPTURE:
                    Log.d("TAKE_PICTURE","Yep");
                    Bundle extras = data.getExtras();
                    avatar = (Bitmap) extras.get("data");
                    break;
            }
        }

        if (avatar != null) {

            avatar = Validator.rotateBitmap(avatar,90);

            avatarImage.setImageBitmap(avatar);

            ByteArrayOutputStream streamS = new ByteArrayOutputStream();
            avatar.compress(Bitmap.CompressFormat.PNG, 100, streamS);
            avatarByteArray = streamS.toByteArray();

        }
    }
/*
    public Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();

        matrix.postRotate(degrees);

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int maxTargetWidth = 120; // your arbitrary fixed limit
        int maxTargetHeight = 120;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(originalWidth/maxTargetWidth, originalHeight/maxTargetHeight);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, originalWidth/scaleFactor, originalHeight/scaleFactor, true);

        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);

        return rotatedBitmap;
    }
*/
    public void selectImage() {
        Log.d(TAG,"selectImage");

        // Generate the Intent.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_OPEN);
        }
    }

    private void takePicture() {
        Log.d(TAG,"takePicture");

        // Generate the Intent.
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }

    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public void register(View v) {

        String emptyString = new String("");
        ArrayList<String> errors = new ArrayList<String>();

        String userFirstNameValue = userFirstName.getText().toString();
        String userLastNameValue = userLastName.getText().toString();
        String userPasswordValue = userPassword.getText().toString();
        String userPassword2Value = userPassword2.getText().toString();
        String userPhoneValue = userPhone.getText().toString();
        String userEmailValue = userEmail.getText().toString();
        String userCityValue = userCity.getSelectedItem().toString();
        boolean owner = userType.isChecked();
        int userTypeValue = 1;
        if (owner) {
            userTypeValue = 2;
        }
        if (Validator.isEmpty(userEmailValue)) {
            errors.add(getResources().getString(R.string.empty_email_error));
        } else if (!Validator.validateEmailAddress(userEmailValue)) {
            errors.add(getResources().getString(R.string.invalid_email_error));
        }
        if (Validator.isEmpty(userPasswordValue)) {
            errors.add(getResources().getString(R.string.empty_pass_error));
        } else if (!userPassword2Value.equals(userPasswordValue)) {
            errors.add(getResources().getString(R.string.different_pass_error));
        }
        if (Validator.isEmpty(userFirstNameValue)) {
            errors.add(getResources().getString(R.string.empty_fname_error));
        }
        if (Validator.isEmpty(userLastNameValue)) {
            errors.add(getResources().getString(R.string.empty_lname_error));
        }
        if (Validator.isEmpty(userPhoneValue)) {
            errors.add(getResources().getString(R.string.empty_phone_error));
        } else if(!Validator.validateMobileNumber(userPhoneValue)) {
            errors.add("Invalid Phone. Phone number should be 10 digits");
        }
        if (errors.size() > 0) {
            String validationString = TextUtils.join("\n", errors);
            Toast.makeText(this, validationString, Toast.LENGTH_SHORT).show();
        } else {
            Log.d("REGISTRATION","VALID");
            userPasswordValue = Validator.md5(userPasswordValue);
            String encodedImage = null;
            if (avatarByteArray != null) {
                encodedImage = Base64.encodeToString(avatarByteArray, Base64.URL_SAFE);
            //  String yourText = new String(avatarByteArray, UTF8_CHARSET);
            }
            PostRequest registrationTask = null;
            try {
                registrationTask = new PostRequest(RegistrationActivity.this, 0, userEmailValue, userPasswordValue, Integer.toString(userTypeValue) , userFirstNameValue, userLastNameValue, userCityValue, userPhoneValue, encodedImage);
            } catch (NetworkErrorException e) {
                e.printStackTrace();
            }
            String result = "";
            if (registrationTask != null) {
                try {
                    result = registrationTask.execute().get();
                    int i = 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            if (Validator.isEmpty(result)) {
                Toast.makeText(this, "Sorry!\nSomething went wrong\nPlease check your internet connection and try again", Toast.LENGTH_SHORT).show();
            } else {
                JSONObject resultJSON = null;
                try {
                    resultJSON = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (resultJSON != null) {
                    int userId = 0;
                    int code = 0;
                    String message = "";
                    try {
                        code = resultJSON.getInt("code");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        userId = resultJSON.getInt("user_id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        message = resultJSON.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (!Validator.isEmpty(message)) {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Sorry!\nSomething went wrong\nPlease check your internet connection and try again", Toast.LENGTH_SHORT).show();
                    }
                    if (code == 1) {
                        JSONObject userJsonObject = null;
                        try {
                            userJsonObject = new JSONObject();
                            userJsonObject.put("id", userId);
                            userJsonObject.put("email", userEmailValue);
                            userJsonObject.put("password", userPasswordValue);
                            FileHelper.writeFile(this, userJsonObject.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(this, SplashScreen.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Sorry!\nSomething went wrong\nPlease check your internet connection and try again", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_blank, menu);
        return true;
    }

}
