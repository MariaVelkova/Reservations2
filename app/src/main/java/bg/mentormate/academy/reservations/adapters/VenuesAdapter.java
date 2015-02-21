package bg.mentormate.academy.reservations.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import bg.mentormate.academy.reservations.R;
import bg.mentormate.academy.reservations.database.DBConstants;
import bg.mentormate.academy.reservations.models.Venue;

/**
 * Created by Maria on 2/9/2015.
 */
public class VenuesAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Venue> venues;
    public VenuesAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.venues = new ArrayList<Venue>();

        if (cursor.moveToFirst()) {
            ContentResolver contentResolver = context.getContentResolver();
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            long time = cal.getTimeInMillis();
            do {

                Venue venue = new Venue(
                        cursor.getInt(cursor.getColumnIndex(DBConstants.DB_TABLE_VENUES_ID)),
                        cursor.getString(cursor.getColumnIndex(DBConstants.DB_TABLE_VENUES_NAME)),
                        cursor.getInt(cursor.getColumnIndex(DBConstants.DB_TABLE_VENUES_TYPE)),
                        cursor.getString(cursor.getColumnIndex(DBConstants.DB_TABLE_VENUES_PHONE)),
                        cursor.getString(cursor.getColumnIndex(DBConstants.DB_TABLE_VENUES_ADDRESS)),
                        cursor.getInt(cursor.getColumnIndex(DBConstants.DB_TABLE_VENUES_CITY_ID)),
                        cursor.getDouble(cursor.getColumnIndex(DBConstants.DB_TABLE_VENUES_LAT)),
                        cursor.getDouble(cursor.getColumnIndex(DBConstants.DB_TABLE_VENUES_LON)),
                        cursor.getString(cursor.getColumnIndex(DBConstants.DB_TABLE_VENUES_WORKTIME)),
                        cursor.getInt(cursor.getColumnIndex(DBConstants.DB_TABLE_VENUES_CAPCITY)),
                        cursor.getInt(cursor.getColumnIndex(DBConstants.DB_TABLE_VENUES_OWNER_ID)),
                        cursor.getInt(cursor.getColumnIndex(DBConstants.DB_TABLE_VENUES_CREATED)),
                        cursor.getInt(cursor.getColumnIndex(DBConstants.DB_TABLE_VENUES_LAST_UPDATED))

                );
                venues.add(venue);
            } while (cursor.moveToNext());
        }
    }

    @Override
    public int getCount() {
        return venues.size();
    }

    @Override
    public Venue getItem(int position) {
        return venues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return venues.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView venueName;
        TextView venueCity;
        TextView venueAddress;
        TextView venuePhone;
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.venue_row, parent, false);
            venueName = (TextView) convertView.findViewById(R.id.venueName);
            convertView.setTag(R.id.venueName, venueName);
            venueAddress = (TextView) convertView.findViewById(R.id.venueAddress);
            convertView.setTag(R.id.venueAddress, venueAddress);
            venuePhone = (TextView) convertView.findViewById(R.id.venuePhone);
            convertView.setTag(R.id.venuePhone, venuePhone);
        } else {
            venueName = (TextView) convertView.getTag(R.id.venueName);
            venueAddress = (TextView) convertView.getTag(R.id.venueAddress);
            venuePhone = (TextView) convertView.getTag(R.id.venuePhone);
        }
        Resources resources = convertView.getResources();
        final Venue currentVenue = getItem(position);
        venueName.setText(currentVenue.getName());
        venueAddress.setText(currentVenue.getAddress());
        venuePhone.setText(currentVenue.getPhone());
        return convertView;
    }
}
