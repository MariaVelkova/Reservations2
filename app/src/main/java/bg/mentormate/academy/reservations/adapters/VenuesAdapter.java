package bg.mentormate.academy.reservations.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import bg.mentormate.academy.reservations.R;
import bg.mentormate.academy.reservations.common.SessionData;
import bg.mentormate.academy.reservations.common.Validator;
import bg.mentormate.academy.reservations.database.DBConstants;
import bg.mentormate.academy.reservations.models.Venue;

/**
 * Created by Maria on 2/9/2015.
 */
public class VenuesAdapter extends BaseAdapter {
    private Context context;
    String query;
    String venueType;
    String venueCity;
    int owner_id;
    private ArrayList<Venue> venuesArray;

    SessionData sessionData = SessionData.getInstance();

    public VenuesAdapter(Context context, String query, String venueType, String venueCity, int owner_id) {
        this.context = context;
        this.query = query;
        this.venueType = venueType;
        this.venueCity = venueCity;
        this.owner_id = owner_id;

        this.venuesArray = new ArrayList<Venue>();



        Cursor cursor;
        ArrayList<String> selection_args = new ArrayList<String>();
        ArrayList<String> args = new ArrayList<String>();

        if (!Validator.isEmpty(query)) {
            selection_args.add(DBConstants.DB_TABLE_VENUES_NAME + " LIKE ?");
            args.add("%" + query + "%");
        }
        if (!Validator.isEmpty(venueCity)) {
            selection_args.add(DBConstants.DB_TABLE_VENUES_CITY + " = ?");
            args.add(venueCity);
        }
        if (!Validator.isEmpty(venueType)) {
            selection_args.add(DBConstants.DB_TABLE_VENUES_TYPE + " = ?");
            args.add(venueType);
        }
        if (owner_id > 0) {
            selection_args.add(DBConstants.DB_TABLE_VENUES_OWNER_ID + " = ?");
            args.add(Integer.toString(owner_id));
        }
        String selectionString = null;
        if (selection_args.size() > 0) {
            selectionString = TextUtils.join(" AND ", selection_args);
        }
        cursor = context.getContentResolver().query(DBConstants.CONTENT_URI_VENUES, null, selectionString, args.toArray(new String[args.size()]), DBConstants.DB_TABLE_VENUES_NAME);

        if (cursor.moveToFirst()) {
            do {

                Venue venue = new Venue(
                        cursor.getInt(cursor.getColumnIndex(DBConstants.DB_TABLE_VENUES_ID)),
                        cursor.getString(cursor.getColumnIndex(DBConstants.DB_TABLE_VENUES_NAME)),
                        cursor.getString(cursor.getColumnIndex(DBConstants.DB_TABLE_VENUES_TYPE)),
                        cursor.getString(cursor.getColumnIndex(DBConstants.DB_TABLE_VENUES_PHONE)),
                        cursor.getString(cursor.getColumnIndex(DBConstants.DB_TABLE_VENUES_ADDRESS)),
                        cursor.getString(cursor.getColumnIndex(DBConstants.DB_TABLE_VENUES_CITY)),
                        cursor.getDouble(cursor.getColumnIndex(DBConstants.DB_TABLE_VENUES_LAT)),
                        cursor.getDouble(cursor.getColumnIndex(DBConstants.DB_TABLE_VENUES_LON)),
                        cursor.getString(cursor.getColumnIndex(DBConstants.DB_TABLE_VENUES_WORKTIME)),
                        cursor.getInt(cursor.getColumnIndex(DBConstants.DB_TABLE_VENUES_CAPCITY)),
                        cursor.getInt(cursor.getColumnIndex(DBConstants.DB_TABLE_VENUES_OWNER_ID)),
                        cursor.getString(cursor.getColumnIndex(DBConstants.DB_TABLE_VENUES_IMAGE))

                );
                venuesArray.add(venue);
            } while (cursor.moveToNext());
        }




        /*
        ArrayList<Venue> sessionVenues = sessionData.getVenues();
        Log.d("CurrentGMTTime", Long.toString(Validator.getCurrentGMTTime()));
        Log.d("VenuesUpdatedAt", Long.toString(sessionData.getVenuesUpdatedAt()));
        Log.d("CurrentGMTTime - VenuesUpdatedAt", Long.toString(Validator.getCurrentGMTTime() - sessionData.getVenuesUpdatedAt()));
        if (sessionVenues == null || Validator.getCurrentGMTTime() - sessionData.getVenuesUpdatedAt() > 2*60*1000) { //2 minutes

            //this.fragmentManager = fragmentManager;

            GetVenues getVenuesTask = new GetVenues(query, venueType, venueCity, owner_id);
            String result = "";
            try {
                result = getVenuesTask.execute().get();
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
                        JSONArray venuesJSON = null;
                        try {
                            venuesJSON = resultJSON.getJSONArray("venues");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (venuesJSON != null) {
                            for (int r = 0; r < venuesJSON.length(); r++) {
                                try {
                                    JSONObject venueJSON = venuesJSON.getJSONObject(r);
                                    Venue venue = new Venue(venueJSON.getInt("id"), venueJSON.getString("name"), venueJSON.getString("type"), venueJSON.getString("phone"), venueJSON.getString("address"), venueJSON.getString("city"), venueJSON.getDouble("lat"), venueJSON.getDouble("lon"), venueJSON.getString("worktime"), venueJSON.getInt("capacity"), venueJSON.getInt("owner_id"), venueJSON.getString("image"));
                                    venuesArray.add(venue);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            sessionData.setVenues(venuesArray);
                        }
                    }
                }
            }
        } else {
            venuesArray = sessionVenues;
        }
        */

    }

    @Override
    public int getCount() {
        return venuesArray.size();
    }

    @Override
    public Venue getItem(int position) {
        return venuesArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return venuesArray.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView venueName;
        ImageView venueImage;
        TextView venueAddress;
        TextView venuePhone;
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.venue_row, parent, false);

            venueName = (TextView) convertView.findViewById(R.id.venueName);
            convertView.setTag(R.id.venueName, venueName);

            venueImage = (ImageView) convertView.findViewById(R.id.venueImage);
            convertView.setTag(R.id.venueImage, venueImage);

            venueAddress = (TextView) convertView.findViewById(R.id.venueAddress);
            convertView.setTag(R.id.venueAddress, venueAddress);

            venuePhone = (TextView) convertView.findViewById(R.id.venuePhone);
            convertView.setTag(R.id.venuePhone, venuePhone);
        } else {
            venueImage = (ImageView) convertView.getTag(R.id.venueImage);
            venueName = (TextView) convertView.getTag(R.id.venueName);
            venueAddress = (TextView) convertView.getTag(R.id.venueAddress);
            venuePhone = (TextView) convertView.getTag(R.id.venuePhone);
        }



        Resources resources = convertView.getResources();
        final Venue currentVenue = getItem(position);
        String pictureArray = currentVenue.getImage();

            byte[] imgbytes = Base64.decode(pictureArray, Base64.URL_SAFE);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imgbytes);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(byteArrayInputStream);
            venueImage.setImageDrawable(bitmapDrawable);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgbytes, 0, imgbytes.length);
        Bitmap bmp  =bitmapDrawable.getBitmap();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap ds = BitmapFactory.decodeByteArray(imgbytes, 0, imgbytes.length, options);
        venueName.setText(currentVenue.getName());
        venueAddress.setText(currentVenue.getAddress());
        venuePhone.setText(currentVenue.getPhone());
        return convertView;
    }
}
