package bg.mentormate.academy.reservations.activities;

import android.accounts.NetworkErrorException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import bg.mentormate.academy.reservations.R;
import bg.mentormate.academy.reservations.activities.admin.AdminActivity;
import bg.mentormate.academy.reservations.common.FileHelper;
import bg.mentormate.academy.reservations.common.PostLogin;
import bg.mentormate.academy.reservations.common.SessionData;
import bg.mentormate.academy.reservations.common.Validator;
import bg.mentormate.academy.reservations.models.User;

public class LoginActivity extends ActionBarActivity {

    String userEmailValue = "";
    String userPassValue = "";

    EditText userEmail;
    EditText userPass;

    long currentGMTTime = Validator.getCurrentGMTTime();
    JSONObject userJsonObject = null;
    SessionData sessionData = SessionData.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmail = (EditText) findViewById(R.id.userEmail);
        userPass = (EditText) findViewById(R.id.userPass);

        Intent callerIntent = getIntent();
        if (callerIntent != null) {
            userEmailValue = callerIntent.getStringExtra("userEmail");
        }

        userEmail.setText(userEmailValue);
    }



    public void userLogin(View v) {

        userEmailValue = userEmail.getText().toString();
        userPassValue = userPass.getText().toString();

        login(true);
    }

    public void userRegistration(View v) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void login(Boolean buttonPreesed) {
        ArrayList<String> errors = new ArrayList<String>();

        if (Validator.isEmpty(userEmailValue)) {
            errors.add(getResources().getString(R.string.empty_email_error));
        } else if (!Validator.validateEmailAddress(userEmailValue)) {
            errors.add(getResources().getString(R.string.invalid_email_error));
        }
        if (Validator.isEmpty(userPassValue)) {
            errors.add(getResources().getString(R.string.empty_pass_error));
        }

        if (errors.size() > 0) {
            String validationString = TextUtils.join("\n", errors);
            if (buttonPreesed == true) {
                Toast.makeText(this, validationString, Toast.LENGTH_SHORT).show();
            }
        } else {
            if (buttonPreesed) {
                userPassValue = Validator.md5(userPassValue);
            }
            PostLogin loginTask = null;
            try {
                loginTask = new PostLogin(this,userEmailValue, userPassValue);
            } catch (NetworkErrorException e) {
                Toast.makeText(this, getString(R.string.no_network), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            String result = "";
            if (loginTask != null) {
                try {
                    result = loginTask.execute().get();
                    Log.d("Post Login", result);
                    int i = 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            if (Validator.isEmpty(result)) {
//                if (buttonPreesed == true) {
//                    Toast.makeText(this, "Sorry!\nSomething went wrong\nPlease check your internet connection and try again", Toast.LENGTH_SHORT).show();
//                }
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
                    JSONObject user = null;
                    JSONArray cities = null;
                    int userId = 0;
                    int userType = 0;
                    try {
                        code = resultJSON.getInt("code");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (code != 1) {
                        try {
                            message = resultJSON.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
/*
                        try {
                            cities = resultJSON.getJSONArray("cities");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (cities != null) {
                            ArrayList<City> citiesList = new ArrayList<City>();
                            City cityObj;
                            for (int j = 0; j < cities.length(); j++) {
                                JSONObject cityJsonObj = null;
                                try {
                                    cityJsonObj = cities.getJSONObject(j);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (cityJsonObj != null) {
                                    try {
                                        cityObj = new City(cityJsonObj.getInt("id"), cityJsonObj.getString("name"));
                                        citiesList.add(cityObj);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    };
                                }
                            }
                            sessionData.setCities(citiesList);
                        }
*/
                        try {
                            user = resultJSON.getJSONObject("user");
                            int g = 0;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (user == null) {
                            message = "Invalid Email and/or Password";
                        } else {
                            try {
                                userId = user.getInt("id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                userType = user.getInt("type");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                User userObj = new User( userId, userType, userEmailValue, user.getString("password"),  user.getString("first_name"), user.getString("last_name"), user.getString("phone"),  user.getString("city"), user.getString("avatar")) ;
                                sessionData.setUser(userObj);
                                FileHelper.writeFile(this, user.toString());

                                goToHomePage(userId, userType);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    if (buttonPreesed == true) {
                        if (!Validator.isEmpty(message)) {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(this, "Sorry!\nSomething went wrong\nPlease check your internet connection and try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    if (buttonPreesed == true) {
                        //Toast.makeText(this, "Sorry!\nSomething went wrong\nPlease check your internet connection and try again", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }
    }

    public void goToHomePage(int userId, int userType) {
        Intent intent;

        JSONObject userJsonObject = null;
        try {
            userJsonObject = new JSONObject();
            userJsonObject.put("id", userId);
            userJsonObject.put("email", userEmailValue);
            userJsonObject.put("password", userPassValue);
            FileHelper.writeFile(this, userJsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (userType == 1) {
            intent = new Intent(this,MainActivity.class);
        } else {
            intent = new Intent(this, AdminActivity.class);
        }
        startActivity(intent);
        finish();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_blank, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
