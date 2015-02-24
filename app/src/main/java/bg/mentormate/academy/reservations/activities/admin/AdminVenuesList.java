package bg.mentormate.academy.reservations.activities.admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import bg.mentormate.academy.reservations.R;
import bg.mentormate.academy.reservations.activities.VenueDetailActivity;
import bg.mentormate.academy.reservations.adapters.VenuesAdapter;
import bg.mentormate.academy.reservations.common.SessionData;
import bg.mentormate.academy.reservations.database.DBConstants;

public class AdminVenuesList extends ActionBarActivity {
    ListView list;
    Button addVenue;
    SessionData sessionData = SessionData.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Refreshed", "Yes");
        super.onCreate(savedInstanceState);
        VenuesAdapter venuesAdapter = new VenuesAdapter(this, "","","", sessionData.getUser().getId());

        if(venuesAdapter.getCount() == 0) {
            setContentView(R.layout.admin_empty_venues_list);
        } else {
            setContentView(R.layout.admin_venues_list);
            list = (ListView) findViewById(R.id.listView2);
            list.setAdapter(venuesAdapter);

            // Define the on-click listener for the list items
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Build the Intent used to open VenueDetailActivity with a specific word Uri
                    Intent intent = new Intent(getApplicationContext(), VenueDetailActivity.class);
//                    Venue venue = (Venue) parent.getAdapter().getItem(position);
//                    String venueString = venue.toString();
//                    intent.putExtra("venue", venueString);
                    Uri data = Uri.withAppendedPath(DBConstants.CONTENT_URI_VENUES,
                            String.valueOf(id));
                    Log.d("ID1",String.valueOf(id));
                    Log.d("ID",String.valueOf(parent.getSelectedItemId()));
                    Log.d("URI", data.toString());
                    intent.setData(data);
                    startActivity(intent);
                }
            });
        }
        addVenue = (Button) findViewById(R.id.addVenueButton);
        addVenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminVenuesList.this, NewVenue.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin_venues_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.refresh) {
            finish();
            startActivity(getIntent());
        }

        return super.onOptionsItemSelected(item);
    }
}
