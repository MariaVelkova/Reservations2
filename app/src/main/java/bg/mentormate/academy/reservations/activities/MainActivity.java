package bg.mentormate.academy.reservations.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import bg.mentormate.academy.reservations.R;
import bg.mentormate.academy.reservations.activities.user_account.UserAccountActivity;
import bg.mentormate.academy.reservations.adapters.VenuesAdapter;
import bg.mentormate.academy.reservations.common.FileHelper;
import bg.mentormate.academy.reservations.common.GetCities;
import bg.mentormate.academy.reservations.common.SessionData;
import bg.mentormate.academy.reservations.common.Validator;
import bg.mentormate.academy.reservations.database.DBConstants;
import bg.mentormate.academy.reservations.models.City;
import bg.mentormate.academy.reservations.models.CustomActionBarDrawerToggle;
import bg.mentormate.academy.reservations.models.User;
import bg.mentormate.academy.reservations.models.Venue;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    final String LOGIN_PREFS = "bg.mentormate.academy.reservations.activities.MainActivity.loginPrefs";

    //The columns we'll include in the dictionary table
    //public static final String KEY_VENUE = SearchManager.SUGGEST_COLUMN_TEXT_1;
    //public static final String KEY_ADDRESS = SearchManager.SUGGEST_COLUMN_TEXT_2;

    //http://it-ride.blogspot.com/2010/10/android-implementing-notification.html
    ArrayList<String> mLeftItems;
    ArrayAdapter mLeftAdapter;
    ListView mLeftDrawer;
    Spinner venueCitySpinner;
    ArrayAdapter<String> venueCityAdapter;
    Spinner venueTypeSpinner;
    ArrayAdapter<CharSequence> venueTypeAdapter;
    Button venueFilterBtn;
    Button venueClearFilterBtn;
//    ArrayList<String> mRightItems;
//    ArrayAdapter mRightAdapter;
    LinearLayout mRightDrawer;
    DrawerLayout mDrawerLayout;

    ActionBarDrawerToggle mDrawerToggle;
    SessionData sessionData = SessionData.getInstance();
    User user = sessionData.getUser();

    String query = "";
    String venueCity = "";
    String venueType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (user != null) {
            venueCity = user.getCity();
        }
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


//        mRightItems = new ArrayList<String>();
//        mRightItems.add("First Right Item");
        mRightDrawer = (LinearLayout) findViewById(R.id.rightListView);
        venueCitySpinner = (Spinner) findViewById(R.id.venueCity);
        // Create an ArrayAdapter using the string array and a default spinner layout
        List<String> list = new ArrayList<String>();
        ArrayList<City> citiesArray = null;
        citiesArray = sessionData.getCities();
        if (citiesArray == null) {
            citiesArray = new ArrayList<City>();
            GetCities getCitiesTask = new GetCities();
            String result = "";
            try {
                result = getCitiesTask.execute().get();
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
                        JSONArray citiesJSON = null;
                        try {
                            citiesJSON = resultJSON.getJSONArray("cities");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (citiesJSON != null) {
                            for (int r = 0; r < citiesJSON.length(); r++) {
                                try {
                                    JSONObject cityJSON = citiesJSON.getJSONObject(r);
                                    City city = new City(cityJSON.getInt("id"), cityJSON.getString("name"));
                                    citiesArray.add(city);
                                    list.add(city.getName());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            sessionData.setCities(citiesArray);
                        }
                    }
                }
            }
        } else {
            for (int c=0; c < citiesArray.size(); c++) {
                list.add(citiesArray.get(c).getName());
            }
        }
        venueCityAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        venueCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        venueCitySpinner.setAdapter(venueCityAdapter);
        if (!Validator.isEmpty(user.getCity())) {
            int spinnerPostion = venueCityAdapter.getPosition(user.getCity());
            venueCitySpinner.setSelection(spinnerPostion);
            spinnerPostion = 0;
        }

        venueTypeSpinner = (Spinner) findViewById(R.id.venueType);
        // Create an ArrayAdapter using the string array and a default spinner layout
        venueTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.venue_types_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        venueTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        venueTypeSpinner.setAdapter(venueTypeAdapter);

        venueFilterBtn = (Button) findViewById(R.id.venueFilter);
        venueFilterBtn.setOnClickListener(this);
        venueClearFilterBtn = (Button) findViewById(R.id.venueClearFilter);
        venueClearFilterBtn.setOnClickListener(this);

        Toolbar mDrawerToolbar = new Toolbar(this);
        //mDrawerToolbar.setNavigationIcon(R.drawable.ic_drawer);

        mDrawerToggle = new CustomActionBarDrawerToggle(this, mDrawerLayout,mDrawerToolbar,R.string.drawer_open, R.string.drawer_close);

        mLeftAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,mLeftItems);
        mLeftDrawer.setAdapter(mLeftAdapter);

//        mRightAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,mRightItems);
//        mRightDrawer.setAdapter(mRightAdapter);

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
            query = intent.getStringExtra(SearchManager.QUERY);
            showResults(query, venueType, venueCity);
            /*
            resultValues = getResults(query);
            String[] resultValuesArray = new String[resultValues.size()];
            resultValuesArray = resultValues.toArray(resultValuesArray);
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, resultValuesArray);
            */
        } else {
            query = "";
            showResults("",venueType,venueCity);
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
                Intent intent = new Intent(getApplicationContext(), VenueDetailActivity.class);
                Uri data = Uri.withAppendedPath(DBConstants.CONTENT_URI_VENUES,
                        String.valueOf(id));
                Log.d("ID1",String.valueOf(id));
                Log.d("ID",String.valueOf(parent.getSelectedItemId()));
                Log.d("URI", data.toString());
                intent.setData(data);
//                Venue venue = (Venue) parent.getAdapter().getItem(position);
//                String venueString = venue.toString();
//                intent.putExtra("venue", venueString);
                startActivity(intent);
            }
        });

        // Display the number of results
        int count = adapter.getCount();
        String countString;
        if (Validator.isEmpty(query) && Validator.isEmpty(venueCity) && Validator.isEmpty(venueType)  ) {
            countString = getResources().getQuantityString(R.plurals.venues_list,
                    count,new Object[]{count}) + "; City: " + user.getCity() + "; Type: All";
        } else {
            if (!Validator.isEmpty(query)) {
                countString = getResources().getQuantityString(R.plurals.search_results,
                        count, new Object[]{count, query});
            } else {
                countString = getResources().getQuantityString(R.plurals.venues_list,
                        count, new Object[]{count, query});
            }
            if (!Validator.isEmpty(venueCity)) {
                countString = countString + "; City: " + venueCity;
            } else {
                countString = countString + "; City: " + user.getCity();
            }
            if (!Validator.isEmpty(venueType)) {
                countString = countString + "; Type: " + venueType;
            } else {
                countString = countString + "; Type: All";
            }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.venueFilter:
                mDrawerLayout.closeDrawer(mRightDrawer);
                venueCity = venueCitySpinner.getSelectedItem().toString();
                venueType = venueTypeSpinner.getSelectedItem().toString();
                if(new String("All").equals(venueType)) {
                    venueType = "";
                }
                showResults(query, venueType, venueCity);
                break;
            case R.id.venueClearFilter:
                mDrawerLayout.closeDrawer(mRightDrawer);
                query = "";
                venueCity = user.getCity();
                int citySpinnerPostion = venueCityAdapter.getPosition(venueCity);
                venueCitySpinner.setSelection(citySpinnerPostion);
                citySpinnerPostion = 0;
                venueType = "";
                int typeSpinnerPostion = venueTypeAdapter.getPosition("All");
                venueTypeSpinner.setSelection(typeSpinnerPostion);
                typeSpinnerPostion = 0;
                showResults(query, venueType, venueCity);
                break;
        }
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,  long id) {
            SelectItem(position);
        }
    }
}
