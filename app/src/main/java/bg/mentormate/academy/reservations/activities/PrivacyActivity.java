package bg.mentormate.academy.reservations.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.content.res.Resources;

import bg.mentormate.academy.reservations.R;

public class PrivacyActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        TextView privacyPolicyTextView = (TextView) findViewById(R.id.privacyPolicyTextView);
        Resources res = getResources();
        String company = res.getString(R.string.privacy_policy_company);
        privacyPolicyTextView.setText(
                String.format(
                        res.getString(R.string.privacy_policy),
                        res.getString(R.string.privacy_policy_last_update),
                        res.getString(R.string.privacy_policy_service),
                        company,
                        company,
                        company,
                        company,
                        res.getString(R.string.privacy_policy_country),
                        res.getString(R.string.privacy_policy_period)));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_blank, menu);
        return true;
    }

}
