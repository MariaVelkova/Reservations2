package bg.mentormate.academy.reservations.activities.admin;

import android.content.Intent;
import android.content.res.Configuration;
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

import java.util.ArrayList;

import bg.mentormate.academy.reservations.R;
import bg.mentormate.academy.reservations.activities.AboutActivity;
import bg.mentormate.academy.reservations.activities.LoginActivity;
import bg.mentormate.academy.reservations.activities.PrivacyActivity;
import bg.mentormate.academy.reservations.activities.user_account.UserAccountActivity;
import bg.mentormate.academy.reservations.adapters.AdminReservationAdapter;
import bg.mentormate.academy.reservations.common.FileHelper;
import bg.mentormate.academy.reservations.common.SessionData;
import bg.mentormate.academy.reservations.models.CustomActionBarDrawerToggle;

public class AdminActivity extends ActionBarActivity {


    ArrayList<String> mLeftItems;
    ArrayAdapter mLeftAdapter;
    ListView mLeftDrawer;
    DrawerLayout mDrawerLayout;

    ActionBarDrawerToggle mDrawerToggle;

    SessionData sessionData = SessionData.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        //TextView reservationsInfo = (TextView) findViewById(R.id.reservationsInfo);
        ListView reservationsList = (ListView) findViewById(R.id.reservationsList);
        AdminReservationAdapter adapter = new AdminReservationAdapter(this, getSupportFragmentManager(),1);
        View view = View.inflate(this, R.layout.empty_list_view, null);
        reservationsList.setEmptyView(view);
        reservationsList.setAdapter(adapter);

        //checkLogin();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.myDrawer);

        mLeftItems = new ArrayList<String>();
        mLeftItems.add(0, "Accepted Reservations");
        mLeftItems.add(1, "Rejected Reservations");
        mLeftItems.add(2, "My Venues");
        mLeftItems.add(3, "My Account");
        mLeftItems.add(4, "About");
        mLeftItems.add(5, "Terms & Conditions");
        mLeftItems.add(6, "Logout");
        mLeftDrawer = (ListView) findViewById(R.id.leftListView);
        mLeftDrawer.setOnItemClickListener(new DrawerItemClickListener());


//        mRightItems = new ArrayList<String>();
//        mRightItems.add("First Right Item");
//        mRightDrawer = (ListView) findViewById(R.id.rightListView);


        Toolbar mDrawerToolbar = new Toolbar(this);
        //mDrawerToolbar.setNavigationIcon(R.drawable.ic_drawer);

        mDrawerToggle = new CustomActionBarDrawerToggle(this, mDrawerLayout, mDrawerToolbar, R.string.drawer_open, R.string.drawer_close);

        mLeftAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mLeftItems);
        mLeftDrawer.setAdapter(mLeftAdapter);

//        mRightAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mRightItems);
//        mRightDrawer.setAdapter(mRightAdapter);

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
        String s = "Debug-infos:";
        s += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        s += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
        s += "\n Device: " + android.os.Build.DEVICE;
        s += "\n Model (and Product): " + android.os.Build.MODEL + " (" + android.os.Build.PRODUCT + ")";
        Log.d("DEBUG", s);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
//            if (mDrawerLayout.isDrawerOpen(mRightDrawer)) {
//                mDrawerLayout.closeDrawer(mRightDrawer);
//            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void SelectItem(int position) {
        mDrawerLayout.closeDrawer(mLeftDrawer);
        Log.d("Position", Integer.toString(position));
        Intent intent;
        switch (position) {
            case 0:
                intent = new Intent(this, AcceptedReservations.class);
                // Launch the Activity
                this.startActivity(intent);
                break;
            case 1:
                intent = new Intent(this, RejectedReservations.class);
                // Launch the Activity
                this.startActivity(intent);
                break;
            case 2:
                intent = new Intent(this, AdminVenuesList.class);
                // Launch the Activity
                this.startActivity(intent);
                break;
            case 3:
                // Create a new Intent
                intent = new Intent(this, UserAccountActivity.class);
                // Launch the Activity
                this.startActivity(intent);
                break;
            case 4:
                // Create a new Intent
                intent = new Intent(this, AboutActivity.class);
                // Launch the Activity
                this.startActivity(intent);
                break;
            case 5:
                // Create a new Intent
                intent = new Intent(this, PrivacyActivity.class);
                // Launch the Activity
                this.startActivity(intent);
                break;
            case 6:
                FileHelper.writeFile(this, "");
                // Create a new Intent
                intent = new Intent(this, LoginActivity.class);
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
