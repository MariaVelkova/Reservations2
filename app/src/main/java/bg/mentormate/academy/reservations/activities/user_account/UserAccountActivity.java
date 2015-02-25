package bg.mentormate.academy.reservations.activities.user_account;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;

import bg.mentormate.academy.reservations.R;
import bg.mentormate.academy.reservations.activities.MainActivity;
import bg.mentormate.academy.reservations.activities.admin.AdminActivity;
import bg.mentormate.academy.reservations.common.SessionData;

public class UserAccountActivity extends ActionBarActivity implements UserAccountFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        // Create new fragment and transaction
        Fragment userAccountFragment = new UserAccountFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.container, userAccountFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d("onFragmentInteraction",uri.toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent;
                if (SessionData.getInstance().getUser().getType() == 1) {
                    upIntent = new Intent(this,MainActivity.class);
                } else {
                    upIntent = new Intent(this,AdminActivity.class);
                }
                startActivity(upIntent);
                //NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
