package bg.mentormate.academy.reservations.activities;

import android.accounts.NetworkErrorException;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import bg.mentormate.academy.reservations.R;
import bg.mentormate.academy.reservations.common.PostMakeReservation;
import bg.mentormate.academy.reservations.common.SessionData;
import bg.mentormate.academy.reservations.common.Validator;

/**
 * Created by PC on 11.2.2015 г..
 */
public class DialogFragmentReserve extends DialogFragment implements View.OnClickListener {

    int venueId;
    Spinner month;
    Spinner day;
    Spinner hour;
    Spinner people;
    EditText info;
    TextView summary;
    Button create;
    String summaryText;
    public static TextView reservationTime;
    public static TextView reservationDate;
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

        String[] peopleCount = new String[10];
        for (int i = 0; i < peopleCount.length ; i++) {
            peopleCount[i] = "" + (i+1);
        }
        ArrayAdapter<String> adapterPeople = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, peopleCount);

        people = (Spinner) view.findViewById(R.id.spinnerPeople);
        create = (Button) view.findViewById(R.id.createReservation);
        create.setOnClickListener(this);

        people.setAdapter(adapterPeople);

        reservationDate = (TextView) view.findViewById(R.id.reservationDate);
        Button pickDateBtn = (Button) view.findViewById(R.id.pickDateBtn);
        pickDateBtn.setOnClickListener(this);

        reservationTime = (TextView) view.findViewById(R.id.reservationTime);
        Button pickTimeBtn = (Button) view.findViewById(R.id.pickTimeBtn);
        pickTimeBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onClick(View v) {
        DialogFragment newFragment;
        switch (v.getId()) {
            case R.id.pickTimeBtn:
                newFragment = new TimePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
                break;
            case R.id.pickDateBtn:
                newFragment = new DatePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
                break;
            case R.id.createReservation:

                int userId = 0;
                userId = SessionData.getInstance().getUser().getId();
                String reservationDateString = reservationDate.getText().toString();
                String reservationTimeString = reservationTime.getText().toString();
                int peopleCount = people.getSelectedItemPosition()+1;

                ArrayList<String> errors = new ArrayList<String>();
                if (Validator.isEmpty(reservationDateString)) {
                    errors.add("Pick Date");
                }
                if (Validator.isEmpty(reservationTimeString)) {
                    errors.add("Pick Time");
                }
                if (peopleCount == 0 ) {
                    errors.add("Pick People Count");
                }

                if (errors.size() > 0) {
                    String validationString = TextUtils.join("\n", errors);
                    Toast.makeText(getActivity(), validationString, Toast.LENGTH_SHORT).show();
                } else {

                    Date date = Validator.StringDateToDate(reservationDateString + " " + reservationTimeString);
                    Calendar GMTCalendar = Calendar.getInstance();
                    GMTCalendar.setTime(date);

                    PostMakeReservation reservationTask = null;
                    try {
                        reservationTask = new PostMakeReservation(getActivity(), userId, venueId, date.getTime() / 1000, people.getSelectedItemPosition() + 1, "");
                    } catch (NetworkErrorException e) {
                        e.printStackTrace();
                    }
                    String result = "";
                    if (reservationTask != null) {
                        try {
                            result = reservationTask.execute().get();
                            int i = 0;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
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
                }



                break;
        }
    }

    public static class DatePickerFragment  extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new CustomDatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            reservationDate.setText(String.format("%4d-%02d-%02d",year,month+1,day));
        }

    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new CustomTimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
           reservationTime.setText(String.format("%02d:%02d",hourOfDay,minute));
        }
    }
}
