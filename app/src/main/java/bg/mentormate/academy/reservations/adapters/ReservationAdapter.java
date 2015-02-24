package bg.mentormate.academy.reservations.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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

        GetReservations getReservationsTask = new GetReservations(0);
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
        //TextView peopleCount;
        TextView venueName;
        TextView venuePhone;
        TextView venueAddress;
        TextView reservationStatus;


        if (convertView == null) {

            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.reservation_custom_row, parent, false);
            date = (TextView) convertView.findViewById(R.id.reservationDate);
            //peopleCount = (TextView) convertView.findViewById(R.id.count);
            venueName = (TextView) convertView.findViewById(R.id.venueName);
            venuePhone = (TextView) convertView.findViewById(R.id.venuePhone);
            venueAddress = (TextView) convertView.findViewById(R.id.venueAddress);
            reservationStatus = (TextView) convertView.findViewById(R.id.reservationStatus);
            picture = (ImageView) convertView.findViewById(R.id.venueImage);

            convertView.setTag(R.id.reservationDate, date);
            //convertView.setTag(R.id.count, peopleCount);
            convertView.setTag(R.id.venueName, venueName);
            convertView.setTag(R.id.venuePhone, venuePhone);
            convertView.setTag(R.id.venueAddress, venueAddress);
            convertView.setTag(R.id.reservationStatus, reservationStatus);
            convertView.setTag(R.id.venueImage, picture);
        } else {

            date = (TextView) convertView.getTag(R.id.reservationDate);
            //peopleCount = (TextView) convertView.getTag(R.id.count);
            venueName = (TextView) convertView.getTag(R.id.venueName);
            venuePhone = (TextView) convertView.getTag(R.id.venuePhone);
            venueAddress = (TextView) convertView.getTag(R.id.venueAddress);
            reservationStatus = (TextView) convertView.getTag(R.id.reservationStatus);
            picture = (ImageView) convertView.getTag(R.id.venueImage);
        }

        final Reservation currentReservation = getItem(position);

        String venueImageValue = currentReservation.getVenue_image();
        venueImageValue = venueImageValue.trim();

        if (!Validator.isEmpty(venueImageValue)) {
            byte[] userAvatarBytes = Base64.decode(venueImageValue, Base64.URL_SAFE);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(userAvatarBytes);
            BitmapDrawable venueImageDrawable  = new BitmapDrawable(byteArrayInputStream);
            picture.setImageDrawable(venueImageDrawable);
        }
        date.setText(currentReservation.getDateBookedString() + " / for " + currentReservation.getPeopleCount());
        //peopleCount.setText("For " + currentReservation.getPeopleCount());
        venuePhone.setText(currentReservation.getVenue_phone());
        venueName.setText(currentReservation.getVenue_name());
        venueAddress.setText(currentReservation.getVenue_address());
        reservationStatus.setText(currentReservation.getStatusString());

        switch (currentReservation.getAccepted()) {
            case 2: reservationStatus.setBackgroundColor(Color.parseColor("#ff2a9b3a")); break;
            case -1: reservationStatus.setBackgroundColor(Color.parseColor("#ff8d3222")); break;
        }
        venuePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String venuePhoneNumber =  currentReservation.getVenue_phone();
                if (!Validator.isEmpty(venuePhoneNumber)) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + venuePhoneNumber));
                    context.startActivity(intent);
                }
            }
        });
        return convertView;

    }
}
