package bg.mentormate.academy.reservations.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

import bg.mentormate.academy.reservations.R;
import bg.mentormate.academy.reservations.activities.user_account.UserAccountActivity;
import bg.mentormate.academy.reservations.adapters.VenuesAdapter;
import bg.mentormate.academy.reservations.common.FileHelper;
import bg.mentormate.academy.reservations.common.SessionData;
import bg.mentormate.academy.reservations.common.Validator;
import bg.mentormate.academy.reservations.database.DBConstants;
import bg.mentormate.academy.reservations.models.CustomActionBarDrawerToggle;
import bg.mentormate.academy.reservations.models.User;

public class MainActivity extends ActionBarActivity {

    final String LOGIN_PREFS = "bg.mentormate.academy.reservations.activities.MainActivity.loginPrefs";

    //The columns we'll include in the dictionary table
    //public static final String KEY_VENUE = SearchManager.SUGGEST_COLUMN_TEXT_1;
    //public static final String KEY_ADDRESS = SearchManager.SUGGEST_COLUMN_TEXT_2;

    ArrayList<String> mLeftItems;
    ArrayAdapter mLeftAdapter;
    ListView mLeftDrawer;
    ArrayList<String> mRightItems;
    ArrayAdapter mRightAdapter;
    ListView mRightDrawer;
    DrawerLayout mDrawerLayout;

    ActionBarDrawerToggle mDrawerToggle;
    SessionData sessionData = SessionData.getInstance();
    User user = sessionData.getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //checkLogin();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.myDrawer);

        mLeftItems = new ArrayList<String>();
        mLeftItems.add(0,"My Reservations");
        mLeftItems.add(1,"My Account");
        mLeftItems.add(2,"About");
        mLeftItems.add(3,"Terms & Conditions");
        mLeftItems.add(4,"Logout");
        mLeftDrawer = (ListView) findViewById(R.id.leftListView);
        mLeftDrawer.setOnItemClickListener(new DrawerItemClickListener());


        mRightItems = new ArrayList<String>();
        mRightItems.add("First Right Item");
        mRightDrawer = (ListView) findViewById(R.id.rightListView);


        Toolbar mDrawerToolbar = new Toolbar(this);
        //mDrawerToolbar.setNavigationIcon(R.drawable.ic_drawer);

        mDrawerToggle = new CustomActionBarDrawerToggle(this, mDrawerLayout,mDrawerToolbar,R.string.drawer_open, R.string.drawer_close);

        mLeftAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,mLeftItems);
        mLeftDrawer.setAdapter(mLeftAdapter);

        mRightAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,mRightItems);
        mRightDrawer.setAdapter(mRightAdapter);

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
        String s="Debug-infos:";
        s += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        s += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
        s += "\n Device: " + android.os.Build.DEVICE;
        s += "\n Model (and Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")";
        Log.d("DEBUG",s);
        //listView = (ListView) findViewById(R.id.listView);
/*
        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        int resourceId = R.drawable.dro;
        Drawable drawableFromRes = getResources().getDrawable(resourceId);
        imageView.setBackground(drawableFromRes);

        String resourceName = "dro";
        String resourceFormat = ".png";
        String resource = resourceName + resourceFormat;
        try {
            Drawable drawableFromPath = Drawable.createFromStream(getAssets().open(resource), resource);
            imageView.setBackground(drawableFromPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
        handleIntent(getIntent());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        mDrawerToggle.onConfigurationChanged(configuration);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // Because this activity has set launchMode="singleTop", the system calls this method
        // to deliver the intent if this activity is currently the foreground activity when
        // invoked again (when the user executes a search from this activity, we don't create
        // a new instance of this activity, so the system delivers the search intent here)
        handleIntent(intent);
    }



    private void handleIntent(Intent intent) {
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // handles a click on a search suggestion; launches activity to show venue
            Log.d("d",intent.getDataString());
            Uri detailUri = intent.getData();
            String id = detailUri.getLastPathSegment();
            Log.d("ID",id);
            //SuggestionsInfo.
            Intent venueIntent = new Intent(this, VenueDetailActivity.class);
            venueIntent.setData(intent.getData());
            startActivity(venueIntent);
            finish();
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // handles a search query
            String query = intent.getStringExtra(SearchManager.QUERY);
            showResults(query, "", user.getCity());
            /*
            resultValues = getResults(query);
            String[] resultValuesArray = new String[resultValues.size()];
            resultValuesArray = resultValues.toArray(resultValuesArray);
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, resultValuesArray);
            */
        } else {
            showResults("","",user.getCity());
        }
        /*
        listView.setAdapter(adapter);
        */
    }

    /**
     * Searches the dictionary and displays results for the given query.
     * @param query The search query
     */

    private void showResults(String query, String venueType, String venueCity) {
        ListView mListView = (ListView) findViewById(R.id.list);

        Log.d("QUERY IS ", query);
        Log.d("MainActivity", "showResults");

            VenuesAdapter adapter = new VenuesAdapter(this, query, venueType, venueCity, 0);

            mListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            // Define the on-click listener for the list items
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Build the Intent used to open VenueDetailActivity with a specific word Uri
                    Intent wordIntent = new Intent(getApplicationContext(), VenueDetailActivity.class);
                    Uri data = Uri.withAppendedPath(DBConstants.CONTENT_URI_VENUES,
                            String.valueOf(id));
                    Log.d("ID1",String.valueOf(id));
                    Log.d("ID",String.valueOf(parent.getSelectedItemId()));
                    Log.d("URI", data.toString());
                    wordIntent.setData(data);
                    startActivity(wordIntent);
                }
            });

        // Display the number of results
        int count = adapter.getCount();
        String countString;
        if (Validator.isEmpty(query) && Validator.isEmpty(venueCity) && Validator.isEmpty(venueType)  ) {
            countString = getResources().getQuantityString(R.plurals.venues_list,
                    count,new Object[]{count});
        } else {
            countString = getResources().getQuantityString(R.plurals.search_results,
                    count, new Object[]{count, query});
        }
        TextView mTextView = (TextView) findViewById(R.id.text);
        mTextView.setText(countString);
        Log.d("showResults", countString);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            MenuItem menuItem = menu.findItem(R.id.action_search);
            SearchView searchView = (SearchView) menuItem.getActionView();
            if (searchView != null) {
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                searchView.setIconifiedByDefault(false);
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            if (mDrawerLayout.isDrawerOpen(mRightDrawer)) {
                mDrawerLayout.closeDrawer(mRightDrawer);
            }
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_search:
                onSearchRequested();
                return true;
            case R.id.action_filter:
                if (mDrawerLayout.isDrawerOpen(mRightDrawer)) {
                    mDrawerLayout.closeDrawer(mRightDrawer);
                } else {
                    mDrawerLayout.closeDrawer(mLeftDrawer);
                    mDrawerLayout.openDrawer(mRightDrawer);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void SelectItem(int possition) {
        mDrawerLayout.closeDrawer(mLeftDrawer);
        Log.d("Possition",Integer.toString(possition));
        Intent intent;
        switch (possition) {
            case 0:
                intent = new Intent(this,ReservationsActivity.class);
                // Launch the Activity
                this.startActivity(intent);
                break;
            case 1:
                // Create a new Intent
                intent = new Intent(this,UserAccountActivity.class);
                // Launch the Activity
                this.startActivity(intent);
                break;
            case 2:
                // Create a new Intent
                intent = new Intent(this,AboutActivity.class);
                // Launch the Activity
                this.startActivity(intent);
                break;
            case 3:
                // Create a new Intent
                intent = new Intent(this,PrivacyActivity.class);
                // Launch the Activity
                this.startActivity(intent);
                break;
            case 4:
                FileHelper.writeFile(this, "");
                // Create a new Intent
                intent = new Intent(this,LoginActivity.class);
                // Launch the Activity
                this.startActivity(intent);
                break;
        }
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            SelectItem(position);

        }
    }
}
