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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import bg.mentormate.academy.reservations.R;
import bg.mentormate.academy.reservations.common.GetReservations;
import bg.mentormate.academy.reservations.common.SessionData;
import bg.mentormate.academy.reservations.common.Validator;
import bg.mentormate.academy.reservations.http.ServerTask;
import bg.mentormate.academy.reservations.models.Reservation;

/**
 * Created by Student09 on 2/21/2015.
 */
public class AdminReservationAdapter  extends BaseAdapter {

    private Context context;
    private FragmentManager fragmentManager;
    private ArrayList<Reservation> reservationsArray;

    public AdminReservationAdapter(Context context, FragmentManager fragmentManager, int status) {
        this.context = context;
        this.fragmentManager = fragmentManager;

        reservationsArray = new ArrayList<Reservation>();

        GetReservations getReservationsTask = new GetReservations(status);
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
        SessionData sessionData = SessionData.getInstance();
        sessionData.setReservations(reservationsArray);
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
        LinearLayout background;
        ImageView picture;
        ImageView yes;
        ImageView no;
        TextView date;
        TextView peopleCount;
        TextView venueName;
        TextView userName;
        TextView userPhone;


        if (convertView == null) {

            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.admin_reservation_custom_row, parent, false);
            date = (TextView) convertView.findViewById(R.id.reservationDate);
            peopleCount = (TextView) convertView.findViewById(R.id.count);
            venueName = (TextView) convertView.findViewById(R.id.venueName);
            userName = (TextView) convertView.findViewById(R.id.userName);
            userPhone = (TextView) convertView.findViewById(R.id.userPhone);
            picture = (ImageView) convertView.findViewById(R.id.venueImage);
            yes = (ImageView) convertView.findViewById(R.id.buttonYes);
            no = (ImageView) convertView.findViewById(R.id.buttonNo);
            background = (LinearLayout) convertView.findViewById(R.id.reservationBackground);

            convertView.setTag(R.id.reservationDate, date);
            convertView.setTag(R.id.count, peopleCount);
            convertView.setTag(R.id.venueName, venueName);
            convertView.setTag(R.id.userName, userName);
            convertView.setTag(R.id.userPhone, userPhone);
            convertView.setTag(R.id.venueImage, picture);
            convertView.setTag(R.id.buttonYes, yes);
            convertView.setTag(R.id.buttonNo, no);
            convertView.setTag(R.id.reservationBackground, background);
        } else {

            date = (TextView) convertView.getTag(R.id.reservationDate);
            peopleCount = (TextView) convertView.getTag(R.id.count);
            venueName = (TextView) convertView.getTag(R.id.venueName);
            userName = (TextView) convertView.getTag(R.id.userName);
            userPhone = (TextView) convertView.getTag(R.id.userPhone);
            picture = (ImageView) convertView.getTag(R.id.venueImage);
            yes = (ImageView) convertView.getTag(R.id.buttonYes);
            no = (ImageView) convertView.getTag(R.id.buttonNo);
            background = (LinearLayout) convertView.getTag(R.id.reservationBackground);
        }

        final Reservation currentReservation = getItem(position);
        String pictureArray = currentReservation.getVenue_image();

        pictureArray = pictureArray.trim();
        if (!Validator.isEmpty(pictureArray)) {
            byte[] imgbytes = Base64.decode(pictureArray, Base64.URL_SAFE);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imgbytes);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(byteArrayInputStream);
            picture.setImageDrawable(bitmapDrawable);
        } else {
            picture.setImageDrawable(context.getResources().getDrawable(R.drawable.no_image));
        }
        date.setText(currentReservation.getDateBookedString());
        peopleCount.setText("For " + currentReservation.getPeopleCount());
        userName.setText(currentReservation.getUser_first_name() + " " + currentReservation.getUser_last_name());
        venueName.setText(currentReservation.getVenue_name());
        userPhone.setText(currentReservation.getUser_phone());
        if(currentReservation.getStatusString().equals("Rejected") ||currentReservation.getStatusString().equals("Accepted")) {
            yes.setVisibility(View.INVISIBLE);
            no.setVisibility(View.INVISIBLE);
            if (currentReservation.getStatusString().equals("Accepted")) {
                background.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));
            } else {
                background.setBackgroundColor(context.getResources().getColor(android.R.color.holo_red_light));
            }
        } else {
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String result = null;
                    String code = null;
                    String message = null;
                    String url = "http://mma-android.comxa.com/?action=approve_reservation&id=" + currentReservation.getId();
                    ServerTask approve = new ServerTask(context);
                    approve.execute(url);
                    try {
                        result = approve.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        code = jsonObject.getString("code");
                        message = jsonObject.getString("message");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (code.equals("1")) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    }
                }
            });

            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String result = null;
                    String code = null;
                    String message = null;
                    String url = "http://mma-android.comxa.com/?action=reject_reservation&id=" + currentReservation.getId();
                    ServerTask reject = new ServerTask(context);
                    reject.execute(url);
                    try {
                        result = reject.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        code = jsonObject.getString("code");
                        message = jsonObject.getString("message");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(code.equals("1")) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

        return convertView;

    }
}
