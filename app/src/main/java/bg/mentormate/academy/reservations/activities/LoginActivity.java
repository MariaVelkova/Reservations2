package bg.mentormate.academy.reservations.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
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
import bg.mentormate.academy.reservations.common.PostRequest;
import bg.mentormate.academy.reservations.common.SessionData;
import bg.mentormate.academy.reservations.common.Validator;
import bg.mentormate.academy.reservations.models.City;
import bg.mentormate.academy.reservations.models.User;

public class LoginActivity extends ActionBarActivity {

    String userEmailValue = "";
    String userPassValue = "";
    long currentGMTTime = Validator.getCurrentGMTTime();
    JSONObject userJsonObject = null;
    SessionData sessionData = SessionData.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String fileContent = FileHelper.readFile(this);
        Log.d("USER",fileContent);
        if (!Validator.isEmpty(fileContent)) {

            try {
                userJsonObject = new JSONObject(fileContent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (userJsonObject != null) {
                try {
                    userEmailValue = userJsonObject.getString("email");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    userPassValue = userJsonObject.getString("password");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                login(false);
            }
        }

        setContentView(R.layout.activity_login);

        String userEmailValue = "";
        Intent callerIntent = getIntent();
        if (callerIntent != null) {
            userEmailValue = callerIntent.getStringExtra("userEmail");
        }
        EditText userEmail = (EditText) findViewById(R.id.userEmail);
        userEmail.setText(userEmailValue);
    }


    public void userLogin(View v) {
        EditText userEmail = (EditText) findViewById(R.id.userEmail);
        EditText userPass = (EditText) findViewById(R.id.userPass);
        userEmailValue = userEmail.getText().toString();
        userPassValue = userPass.getText().toString();

        login(true);
    }

    public void userRegistration(View v) {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
    }

    public void login(Boolean buttonPreesed) {
        String emptyString = new String("");
        ArrayList<String> errors = new ArrayList<String>();

        if (emptyString.equals(userEmailValue)) {
            errors.add("Empty Email");
        } else if (!Validator.validateEmailAddress(userEmailValue)) {
            errors.add("Invalid Email");
        }
        if (emptyString.equals(userPassValue)) {
            errors.add("Empty Password");
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
            PostLogin loginTask = new PostLogin(userEmailValue, userPassValue);
            String result = "";
            try {
                result = loginTask.execute().get();
                int i = 0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (Validator.isEmpty(result)) {
                if (buttonPreesed == true) {
                    Toast.makeText(this, "Sorry!\nSomething went wrong\nPlease check your internet connection and try again", Toast.LENGTH_SHORT).show();
                }
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
                        try {
                            user = resultJSON.getJSONObject("user");
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
                                User userObj = new User( userId, userType, userEmailValue, user.getString("password"),  user.getString("first_name"), user.getString("last_name"), user.getString("phone"),  user.getString("city"), user.getString("password")) ;
                                sessionData.setUser(userObj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            goToHomePage(userId, userType);
                        }

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
                        //dismiss();

                    }

                    if (buttonPreesed == true) {
                        if (!Validator.isEmpty(message)) {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Sorry!\nSomething went wrong\nPlease check your internet connection and try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    if (buttonPreesed == true) {
                        Toast.makeText(this, "Sorry!\nSomething went wrong\nPlease check your internet connection and try again", Toast.LENGTH_SHORT).show();
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

        Log.d("ATTENTION", "IMPLEMENT LOGIN HERE");
        if (userType == 1) {
            intent = new Intent(this,MainActivity.class);
        } else {
            intent = new Intent(this, AdminActivity.class);
        }
        startActivity(intent);
    }

}
