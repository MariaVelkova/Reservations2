package bg.mentormate.academy.reservations.activities.admin;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import bg.mentormate.academy.reservations.R;
import bg.mentormate.academy.reservations.adapters.AdminReservationAdapter;

public class RejectedReservations extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AdminReservationAdapter adapter = new AdminReservationAdapter(this, getSupportFragmentManager(),-1);
        if(adapter.getCount() == 0) {
            setContentView(R.layout.empty_list_view);
            TextView emptyListText = (TextView) findViewById(R.id.emptyListText);
            emptyListText.setText("This list is still empty");
        } else {
            setContentView(R.layout.activity_accepted_reservations);
            ListView reservationsList = (ListView) findViewById(R.id.listViewAccepted);
            reservationsList.setAdapter(adapter);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rejected_reservations, menu);
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
        }  else if (id == R.id.refresh) {
            finish();
            startActivity(getIntent());
        }

        return super.onOptionsItemSelected(item);
    }
}
