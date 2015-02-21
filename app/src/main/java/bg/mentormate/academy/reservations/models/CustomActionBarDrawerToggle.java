package bg.mentormate.academy.reservations.models;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import bg.mentormate.academy.reservations.R;

/**
 * Created by Maria on 2/10/2015.
 */
public class CustomActionBarDrawerToggle extends ActionBarDrawerToggle {

    ActionBarActivity activity;
    int openDrawerContentDescRes;
    int closeDrawerContentDescRes;
    public CustomActionBarDrawerToggle(ActionBarActivity activity, DrawerLayout drawerLayout, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
        super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
        this.activity = activity;
        this.openDrawerContentDescRes = openDrawerContentDescRes;
        this.closeDrawerContentDescRes = closeDrawerContentDescRes;
    }

    public CustomActionBarDrawerToggle(ActionBarActivity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
        super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        this.activity = activity;
    }

    @Override
    public void onDrawerOpened(View drawerView) {

        if (drawerView.getId() == R.id.leftListView) {
            super.onDrawerOpened(drawerView);
            activity.getSupportActionBar().setTitle("Please Select From List");
        }
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        if (drawerView.getId() == R.id.leftListView) {
            super.onDrawerClosed(drawerView);
            activity.getSupportActionBar().setTitle(activity.getResources().getString(R.string.app_name));
        }
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        if (drawerView.getId() == R.id.leftListView) {
            super.onDrawerSlide(drawerView, slideOffset);
        }
    }
}
