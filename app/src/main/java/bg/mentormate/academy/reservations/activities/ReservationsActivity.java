package bg.mentormate.academy.reservations.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import bg.mentormate.academy.reservations.R;
import bg.mentormate.academy.reservations.adapters.ReservationAdapter;
import bg.mentormate.academy.reservations.adapters.VenuesAdapter;
import bg.mentormate.academy.reservations.common.GetReservations;
import bg.mentormate.academy.reservations.common.PostRequest;
import bg.mentormate.academy.reservations.common.Validator;
import bg.mentormate.academy.reservations.database.DBConstants;
import bg.mentormate.academy.reservations.models.Reservation;

public class ReservationsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_reservations);

        //TextView reservationsInfo = (TextView) findViewById(R.id.reservationsInfo);
        ListView reservationsList = (ListView) findViewById(R.id.reservationsList);
        ReservationAdapter adapter = new ReservationAdapter(this, getSupportFragmentManager());
        reservationsList.setAdapter(adapter);
    }

}
