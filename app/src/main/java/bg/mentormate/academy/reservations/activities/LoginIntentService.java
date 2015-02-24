package bg.mentormate.academy.reservations.activities;

import android.accounts.NetworkErrorException;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import bg.mentormate.academy.reservations.common.FileHelper;
import bg.mentormate.academy.reservations.common.GetVenues;
import bg.mentormate.academy.reservations.common.PostLogin;
import bg.mentormate.academy.reservations.common.SessionData;
import bg.mentormate.academy.reservations.common.Validator;
import bg.mentormate.academy.reservations.database.DBConstants;
import bg.mentormate.academy.reservations.models.City;
import bg.mentormate.academy.reservations.models.User;


/**
 * Created by Maria on 2/15/2015.
 */
public class LoginIntentService extends IntentService {

        // Used to write to the system log from this class.
        public static final String LOG_TAG = "LoginIntentService";
        public SessionData sessionData = SessionData.getInstance();

        // Defines and instantiates an object for handling status updates.
        private BroadcastNotifier mBroadcaster = new BroadcastNotifier(this);

        public LoginIntentService() {
            super("LoginIntentService");
        }

        @Override
        protected void onHandleIntent(Intent intent) {

            Log.d("LoginIntentService", "Updating the data");
            mBroadcaster.broadcastIntentWithState(DBConstants.STATE_ACTION_STARTED);
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
            mBroadcaster.broadcastIntentWithState(DBConstants.STATE_ACTION_LOGING);
            String userEmailValue = "";
            String userPassValue = "";
            JSONObject userJsonObject = null;

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
                    //login(false);
                }
            }
            int uu = 5;
            if (!Validator.isEmpty(userEmailValue) && Validator.validateEmailAddress(userEmailValue) && !Validator.isEmpty(userPassValue)) {
                PostLogin loginTask = null;
                try {
                    loginTask = new PostLogin(this, userEmailValue, userPassValue);
                } catch (NetworkErrorException e) {
                    e.printStackTrace();
                }
                String result = "";
                if (loginTask != null) {
                    try {
                        result = loginTask.execute().get();
                        int i = 0;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                if (!Validator.isEmpty(result)) {
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
//                        JSONArray cities = null;
                        int userId = 0;
                        int userType = 0;
                        try {
                            code = resultJSON.getInt("code");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (code == 1) {
//                            try {
//                                cities = resultJSON.getJSONArray("cities");
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                            if (cities != null) {
//                                ArrayList<City> citiesList = new ArrayList<City>();
//                                City cityObj;
//                                for (int j = 0; j < cities.length(); j++) {
//                                    JSONObject cityJsonObj = null;
//                                    try {
//                                        cityJsonObj = cities.getJSONObject(j);
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                    if (cityJsonObj != null) {
//                                        try {
//                                            cityObj = new City(cityJsonObj.getInt("id"), cityJsonObj.getString("name"));
//                                            citiesList.add(cityObj);
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        };
//                                    }
//                                }
//                                sessionData.setCities(citiesList);
//                            }

                            try {
                                user = resultJSON.getJSONObject("user");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (user != null) {
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
                                    String u = user.toString();
                                    FileHelper.writeFile(this, user.toString());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                //Intent redirectIntent;

                                if (userType == 1) {
                                    mBroadcaster.broadcastIntentWithState(DBConstants.STATE_ACTION_LOGGED_USER);
                                    return;
                                    //redirectIntent = new Intent(this,MainActivity.class);
                                } else {
                                    mBroadcaster.broadcastIntentWithState(DBConstants.STATE_ACTION_LOGGED_ADMIN);
                                    return;
                                    //redirectIntent = new Intent(this, AdminActivity.class);
                                }
                                //startActivity(redirectIntent);
                            }
                        }
                    }
                }
            }
            mBroadcaster.broadcastIntentWithState(DBConstants.STATE_ACTION_LOGING_FAILED);
            Log.d("LoginIntentService", "Data successfully updated!");
        }

}
                /*
                    JSONObject reservations = data.getJSONObject("reservations");
                    Iterator<String> reservationsKeys = reservations.keys();

                    while(reservationsKeys.hasNext()) {
                        String reservationId = reservationsKeys.next();
                        JSONObject reservation = reservations.getJSONArray(reservationId).getJSONObject(0);
                        Log.d("RES ID",reservationId);
                        Log.d("RES",reservation.toString());
                        ContentValues values = new ContentValues();
                        values.put(DBConstants.DB_TABLE_RESERVATIONS_ID, reservationId);
                        values.put(DBConstants.DB_TABLE_RESERVATIONS_USER_ID, reservation.getInt(DBConstants.DB_TABLE_RESERVATIONS_USER_ID));
                        values.put(DBConstants.DB_TABLE_RESERVATIONS_VENUE_ID, reservation.getInt(DBConstants.DB_TABLE_RESERVATIONS_VENUE_ID));
                        values.put(DBConstants.DB_TABLE_RESERVATIONS_DATE_BOOKED, reservation.getInt(DBConstants.DB_TABLE_RESERVATIONS_DATE_BOOKED));
                        values.put(DBConstants.DB_TABLE_RESERVATIONS_DATE_CREATED, reservation.getInt(DBConstants.DB_TABLE_RESERVATIONS_DATE_CREATED));
                        values.put(DBConstants.DB_TABLE_RESERVATIONS_PEOPLE_COUNT, reservation.getInt(DBConstants.DB_TABLE_RESERVATIONS_PEOPLE_COUNT));
                        values.put(DBConstants.DB_TABLE_RESERVATIONS_COMMENT, reservation.getString(DBConstants.DB_TABLE_RESERVATIONS_COMMENT));
                        values.put(DBConstants.DB_TABLE_RESERVATIONS_ACCEPTED, reservation.getInt(DBConstants.DB_TABLE_RESERVATIONS_ACCEPTED));
                        Uri uri = getContentResolver().insert(DBConstants.CONTENT_URI_RESERVATIONS, values);
                    }
                    */


