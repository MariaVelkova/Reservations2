package bg.mentormate.academy.reservations.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import bg.mentormate.academy.reservations.R;
import bg.mentormate.academy.reservations.common.PostMakeReservation;
import bg.mentormate.academy.reservations.common.PostRequest;
import bg.mentormate.academy.reservations.common.SessionData;
import bg.mentormate.academy.reservations.common.Validator;

/**
 * Created by PC on 11.2.2015 г..
 */
public class DialogFragmentReserve extends DialogFragment {

    int venueId;
    Spinner month;
    Spinner day;
    Spinner hour;
    Spinner people;
    EditText info;
    TextView summary;
    Button create;
    String summaryText;

    /**
     * Create a new instance of FragmentBookTicketDialog, providing "movieId" and "cinemaId"
     * as arguments.
     */
    public static DialogFragmentReserve getInstance(int venueId) {
        DialogFragmentReserve f = new DialogFragmentReserve();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("venueId", venueId);
        f.setArguments(args);

        return f;
    }

    public DialogFragmentReserve() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_reservation_dialog, container, false);

        getDialog().setTitle("Make Reservation");
        venueId = getArguments().getInt("venueId", 0);
        String[] months = {"January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"};
        String[] days = new String[31];
        for (int i = 0; i < days.length ; i++) {
            days[i] = "" + (i+1);
        }
        String hours[] = new String[24];
        for (int i = 0; i < hours.length ; i++) {
            hours[i] = "" + (i+1);
        }
        String[] peopleCount = new String[10];
        for (int i = 0; i < peopleCount.length ; i++) {
            peopleCount[i] = "" + (i+1);
        }
        ArrayAdapter<String> adapterMonths = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, months);
        ArrayAdapter<String> adapterDays = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, days);
        ArrayAdapter<String> adapterHours = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, hours);
        ArrayAdapter<String> adapterPeople = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, peopleCount);

        month = (Spinner) view.findViewById(R.id.spinnerMonth);
        day = (Spinner) view.findViewById(R.id.spinnerDay);
        hour = (Spinner) view.findViewById(R.id.spinnerHour);
        people = (Spinner) view.findViewById(R.id.spinnerPeople);
        info = (EditText) view.findViewById(R.id.AdditionalInfo);
        summary = (TextView) view.findViewById(R.id.summary);
        create = (Button) view.findViewById(R.id.createReservation);

        month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateSummary();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateSummary();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateSummary();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        people.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateSummary();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        month.setAdapter(adapterMonths);
        day.setAdapter(adapterDays);
        hour.setAdapter(adapterHours);
        people.setAdapter(adapterPeople);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userId = 0;
                userId = SessionData.getInstance().getUser().getId();

                Date date = new Date();
                date.setDate(day.getSelectedItemPosition());
                date.setMonth(month.getSelectedItemPosition());
                date.setYear(2015);
                date.setHours(hour.getSelectedItemPosition());
                Calendar GMTCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                GMTCalendar.setTime(date);

                PostMakeReservation reservationTask = new PostMakeReservation(userId, venueId, GMTCalendar.getTimeInMillis(), people.getSelectedItemPosition(), info.getText().toString());
                String result = "";
                try {
                    result = reservationTask.execute().get();
                    int i = 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (Validator.isEmpty(result)) {
                    Toast.makeText(getActivity(), "Sorry!\nSomething went wrong\nPlease check your internet connection and try again", Toast.LENGTH_SHORT).show();
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

                        if (!Validator.isEmpty(message)) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Sorry!\nSomething went wrong\nPlease check your internet connection and try again", Toast.LENGTH_SHORT).show();
                        }
                        if (code == 1) {
                            dismiss();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Sorry!\nSomething went wrong\nPlease check your internet connection and try again", Toast.LENGTH_SHORT).show();
                    }

                }
                //dismiss();
            }
        });

        return view;
    }

    private void updateSummary() {
        summaryText = "Date: 2015/" + month.getSelectedItem() + "/" + day.getSelectedItem() + "/" +
               hour.getSelectedItem() + " o'clock for " + people.getSelectedItem() + " people";
        summary.setText(summaryText);
    }
}
