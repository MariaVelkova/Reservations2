package bg.mentormate.academy.reservations.activities.admin;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import bg.mentormate.academy.reservations.R;
import bg.mentormate.academy.reservations.activities.user_account.UserAccountFragment;

public class AdminHomeActivity extends ActionBarActivity implements UserAccountFragment.OnFragmentInteractionListener {

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
        Log.d("onFragmentInteraction", uri.toString());
    }

}

