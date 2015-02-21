package bg.mentormate.academy.reservations.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
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

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import bg.mentormate.academy.reservations.R;
import bg.mentormate.academy.reservations.common.GetReservations;
import bg.mentormate.academy.reservations.common.Validator;
import bg.mentormate.academy.reservations.models.Reservation;

/**
 * Created by Student09 on 2/21/2015.
 */
public class AdminReservationAdapter  extends BaseAdapter {

    private Context context;
    private FragmentManager fragmentManager;
    private ArrayList<Reservation> reservationsArray;

    public AdminReservationAdapter(Context context, FragmentManager fragmentManager) {
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
                                Reservation reservation = new Reservation(reservationJSON.getInt("id"), reservationJSON.getInt("user_id"), reservationJSON.getInt("venue_id"), reservationJSON.getString("venue_name"), reservationJSON.getString("venue_phone"), reservationJSON.getString("venue_city"), reservationJSON.getString("venue_address"), reservationJSON.getInt("date_created"), reservationJSON.getInt("date_booked"), reservationJSON.getInt("people_count"), reservationJSON.getString("comment"), reservationJSON.getInt("accepted"));
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
        ImageView yes;
        ImageView no;
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
//            yes = (ImageView) convertView.findViewById(R.id.buttonYes);
//            no = (ImageView) convertView.findViewById(R.id.buttonNo);

            convertView.setTag(R.id.reservationDate, date);
            convertView.setTag(R.id.count, peopleCount);
            convertView.setTag(R.id.venueName, venueName);
            convertView.setTag(R.id.userName, userName);
            convertView.setTag(R.id.userPhone, userPhone);
            convertView.setTag(R.id.venueImage, picture);
//            convertView.setTag(R.id.buttonYes, yes);
//            convertView.setTag(R.id.buttonNo, no);
        } else {

            date = (TextView) convertView.getTag(R.id.reservationDate);
            peopleCount = (TextView) convertView.getTag(R.id.count);
            venueName = (TextView) convertView.getTag(R.id.venueName);
            userName = (TextView) convertView.getTag(R.id.userName);
            userPhone = (TextView) convertView.getTag(R.id.userPhone);
            picture = (ImageView) convertView.getTag(R.id.venueImage);
//            yes = (ImageView) convertView.getTag(R.id.buttonYes);
//            no = (ImageView) convertView.getTag(R.id.buttonNo);
        }

        Reservation currentReservation = getItem(position);

//        picture.setImageResource(currentReservation.getVenue().getPicture());
//        date.setText(currentReservation.getDate());
//        peopleCount.setText("For " + currentReservation.getForHowMany());
//        userName.setText(currentReservation.getUser().getFirstName() + " " + currentReservation.getUser().getLastName());
//        venueName.setText(currentReservation.getVenue().getName());
        userPhone.setText("0878571190");
//        yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /*DialogFragment dialog = new CustomDialogFragment();
//                Bundle args = new Bundle();
//                args.putString("message", "Accept Reservation?");
//                args.putString("positive", "Yes");
//                args.putString("negative", "No");
//                dialog.setArguments(args);
//                dialog.setTargetFragment(Constants.REQUEST_CODE_APPROVE);
//                dialog.show(fragmentManager, "TAG");
//                */
//                Toast.makeText(context, "Reservation Approved", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               /* DialogFragment dialog = new CustomDialogFragment();
//                Bundle args = new Bundle();
//                args.putString("message", "Reject Reservation?");
//                args.putString("positive", "Yes");
//                args.putString("negative", "No");
//                dialog.setArguments(args);
//               // dialog.setTargetFragment(fragmentManager.getFragment(bundle,"key"), Constants.REQUEST_CODE_REJECT);
//                dialog.show(fragmentManager, "TAG");
//                */
//                Toast.makeText(context, "Reservation Denied", Toast.LENGTH_SHORT).show();
//            }
//        });

        return convertView;

    }
}
