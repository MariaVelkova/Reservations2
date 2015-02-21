package bg.mentormate.academy.reservations.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import bg.mentormate.academy.reservations.R;
import bg.mentormate.academy.reservations.common.GetReservations;
import bg.mentormate.academy.reservations.common.GetVenues;
import bg.mentormate.academy.reservations.common.Validator;
import bg.mentormate.academy.reservations.database.DBConstants;
import bg.mentormate.academy.reservations.models.Reservation;
import bg.mentormate.academy.reservations.models.Venue;

/**
 * Created by Maria on 2/9/2015.
 */
public class VenuesAdapter extends BaseAdapter {
    private Context context;
    String query;
    String venueType;
    String venueCity;
    int owner_id;
    private ArrayList<Venue> venuesArray;
    public VenuesAdapter(Context context, String query, String venueType, String venueCity, int owner_id) {
        this.context = context;
        this.query = query;
        this.venueType = venueType;
        this.venueCity = venueCity;
        this.owner_id = owner_id;

        this.venuesArray = new ArrayList<Venue>();

        //this.fragmentManager = fragmentManager;

        GetVenues getVenuesTask = new GetVenues(query,venueType,venueCity,owner_id);
        String result = "";
        try {
            result = getVenuesTask.execute().get();
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
                    JSONArray venuesJSON = null;
                    try {
                        venuesJSON = resultJSON.getJSONArray("venues");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (venuesJSON != null) {
                        for (int r = 0; r < venuesJSON.length(); r++) {
                            try {
                                JSONObject venueJSON = venuesJSON.getJSONObject(r);
                                Venue venue = new Venue(venueJSON.getInt("id"), venueJSON.getString("name"), venueJSON.getString("type"), venueJSON.getString("phone"), venueJSON.getString("address"), venueJSON.getString("city"), venueJSON.getDouble("lat"), venueJSON.getDouble("lon"), venueJSON.getString("worktime"), venueJSON.getInt("capacity"), venueJSON.getInt("owner_id"));
                                venuesArray.add(venue);
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
    public int getCount() {
        return venuesArray.size();
    }

    @Override
    public Venue getItem(int position) {
        return venuesArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return venuesArray.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView venueName;
        TextView venueCity;
        TextView venueAddress;
        TextView venuePhone;
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.venue_row, parent, false);
            venueName = (TextView) convertView.findViewById(R.id.venueName);
            convertView.setTag(R.id.venueName, venueName);
            venueAddress = (TextView) convertView.findViewById(R.id.venueAddress);
            convertView.setTag(R.id.venueAddress, venueAddress);
            venuePhone = (TextView) convertView.findViewById(R.id.venuePhone);
            convertView.setTag(R.id.venuePhone, venuePhone);
        } else {
            venueName = (TextView) convertView.getTag(R.id.venueName);
            venueAddress = (TextView) convertView.getTag(R.id.venueAddress);
            venuePhone = (TextView) convertView.getTag(R.id.venuePhone);
        }



        Resources resources = convertView.getResources();
        final Venue currentVenue = getItem(position);
        venueName.setText(currentVenue.getName());
        venueAddress.setText(currentVenue.getAddress());
        venuePhone.setText(currentVenue.getPhone());
        return convertView;
    }
}