package bg.mentormate.academy.reservations.adapters;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import bg.mentormate.academy.reservations.R;
import bg.mentormate.academy.reservations.common.GetReservations;
import bg.mentormate.academy.reservations.common.Validator;
import bg.mentormate.academy.reservations.models.Reservation;

/**
 * Created by PC on 6.2.2015 г..
 */
public class ReservationAdapter extends BaseAdapter {

    private Context context;
    private FragmentManager fragmentManager;
    private ArrayList<Reservation> reservationsArray;

    public ReservationAdapter(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;

        reservationsArray = new ArrayList<Reservation>();

        GetReservations getReservationsTask = new GetReservations();
        String result = "";
        try {
            result = getReservationsTask.execute().get();
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
                    Log.d("ATTENTION", "IMPLEMENT LOGIN HERE");
                    JSONArray reservationsJSON = null;
                    try {
                        reservationsJSON = resultJSON.getJSONArray("reservations");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (reservationsJSON != null) {
                        for (int r = 0; r < reservationsJSON.length(); r++) {
                            try {
                                JSONObject reservationJSON = reservationsJSON.getJSONObject(r);
                                Reservation reservation = new Reservation(reservationJSON.getInt("id"), reservationJSON.getInt("user_id"), reservationJSON.getString("user_first_name"), reservationJSON.getString("user_last_name"), reservationJSON.getString("user_phone"), reservationJSON.getInt("venue_id"), reservationJSON.getString("venue_name"), reservationJSON.getString("venue_phone"), reservationJSON.getString("venue_city"), reservationJSON.getString("venue_address"), reservationJSON.getString("venue_image"), reservationJSON.getInt("date_created"), reservationJSON.getInt("date_booked"), reservationJSON.getInt("people_count"), reservationJSON.getString("comment"), reservationJSON.getInt("accepted"));
                                reservationsArray.add(reservation);
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

        return reservationsArray.size();
    }

    // getItem(int) in Adapter returns Object but we can override
    // it to User thanks to Java return type covariance
    @Override
    public Reservation getItem(int position) {

        return reservationsArray.get(position);
    }

    @Override
    public long getItemId(int position) {

        return reservationsArray.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView picture;
        TextView date;
        TextView peopleCount;
        TextView venueName;
        TextView userName;
        TextView userPhone;


        if (convertView == null) {

            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.reservation_custom_row, parent, false);
            date = (TextView) convertView.findViewById(R.id.reservationDate);
            peopleCount = (TextView) convertView.findViewById(R.id.count);
            venueName = (TextView) convertView.findViewById(R.id.venueName);
            userName = (TextView) convertView.findViewById(R.id.userName);
            userPhone = (TextView) convertView.findViewById(R.id.userPhone);
            picture = (ImageView) convertView.findViewById(R.id.venueImage);

            convertView.setTag(R.id.reservationDate, date);
            convertView.setTag(R.id.count, peopleCount);
            convertView.setTag(R.id.venueName, venueName);
            convertView.setTag(R.id.userName, userName);
            convertView.setTag(R.id.userPhone, userPhone);
            convertView.setTag(R.id.venueImage, picture);
        } else {

            date = (TextView) convertView.getTag(R.id.reservationDate);
            peopleCount = (TextView) convertView.getTag(R.id.count);
            venueName = (TextView) convertView.getTag(R.id.venueName);
            userName = (TextView) convertView.getTag(R.id.userName);
            userPhone = (TextView) convertView.getTag(R.id.userPhone);
            picture = (ImageView) convertView.getTag(R.id.venueImage);
        }

        Reservation currentReservation = getItem(position);

        String venueImageValue = currentReservation.getVenue_image();
        venueImageValue = venueImageValue.trim();

        if (!Validator.isEmpty(venueImageValue)) {
            byte[] userAvatarBytes = Base64.decode(venueImageValue, Base64.URL_SAFE);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(userAvatarBytes);
            BitmapDrawable venueImageDrawable  = new BitmapDrawable(byteArrayInputStream);
            picture.setImageDrawable(venueImageDrawable);
        }
        date.setText(currentReservation.getDateBookedString());
        peopleCount.setText("For " + currentReservation.getPeopleCount());
        userName.setText(currentReservation.getUser_last_name() + " " + currentReservation.getUser_last_name());
        venueName.setText(currentReservation.getVenue_name());
        userPhone.setText(currentReservation.getVenue_phone());

        return convertView;

    }
}
