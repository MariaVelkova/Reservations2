package bg.mentormate.academy.reservations.database;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.HashMap;
import java.util.Map;
import java.util.Locale;

/**
 * Created by Student16 on 2/5/2015.
 */
public class DBConstants {


        // Constants for the content provider
        public static final String AUTHORITY                = "bg.mentormate.academy.reservations.providers.CustomContentProvider";
        //public static final String SUGGESTION_AUTHORITY     = "bg.mentormate.academy.reservations.providers.CustomSearchRecentSuggestionsProvider";
        public static final String DB_NAME                  = "Reservations.db";
        public static final int DB_VERSION                  = 6;

        // MIME types used for searching venues
        public static final String VENUES_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/vnd.paad.elemental";
        public static final String DEFINITION_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/vnd.paad.elemental";

        // The index (key) column name for use in where clauses.
        public static final String KEY_ID = "id";

        // The name and column index of each column in your database.
        // These should be descriptive.
        public static final String KEY_COLUMN_1_NAME = "name";

        // ------- define tables
        public static final String DB_TABLE_VENUES          = "venues";
        public static final String DB_TABLE_USERS           = "users";
        public static final String DB_TABLE_RESERVATIONS    = "reservations";
        public static final String DB_TABLE_CITIES          = "cities";

        public static final String[] DB_TABLES              = new String[]{DB_TABLE_VENUES, DB_TABLE_USERS, DB_TABLE_RESERVATIONS, DB_TABLE_CITIES};
        public static final String[] VENUES_TYPES           = new String[]{ "Restaurant", "Club", "Bar", "Cafe"};

        // ------- define some Uris
        public static final Uri CONTENT_URI_VENUES          = Uri.parse("content://" + AUTHORITY
                + "/" + DB_TABLE_VENUES);
        public static final Uri CONTENT_URI_USERS           = Uri.parse("content://" + AUTHORITY
                + "/" + DB_TABLE_USERS);
        public static final Uri CONTENT_URI_RESERVATIONS    = Uri.parse("content://" + AUTHORITY
                + "/" + DB_TABLE_RESERVATIONS);
        public static final Uri CONTENT_URI_CITIES          = Uri.parse("content://" + AUTHORITY
                + "/" + DB_TABLE_CITIES);

        // ------- maybe also define CONTENT_TYPE for each

        //public static final String URL                  = "content://" + AUTHORITY + "/" + DATABASE_NAME;
        //public static final Uri CONTENT_URI             = Uri.parse(URL);

        // ------- setup UriMatcher


        public static final int ALLROWS = 1;
        public static final int SINGLE_ROW = 2;
        public static final int SEARCH = 3;

        public static final int TAG_SEARCH_VENUES = 0;
        public static final int TAG_GET_VENUE = 1;
        public static final int TAG_SEARCH_SUGGEST = 2;
        public static final int TAG_REFRESH_SHORTCUT = 3;

        public static final int TAG_VENUES = 10;
        public static final int TAG_VENUE_ID = 20;
        public static final int TAG_USERS = 30;
        public static final int TAG_USERS_ID = 40;
        public static final int TAG_RESERVATIONS = 50;
        public static final int TAG_RESERVATIONS_ID = 60;
        public static final int TAG_CITIES = 70;
        public static final int TAG_CITIES_ID = 80;
        public static final UriMatcher uriMatcher;
        static {
            uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

            uriMatcher.addURI(AUTHORITY,DB_TABLE_VENUES, ALLROWS);
            uriMatcher.addURI(AUTHORITY,DB_TABLE_VENUES + "/#", SINGLE_ROW);
            uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH);
            uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH);
            uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_SHORTCUT, SEARCH);
            uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_SHORTCUT + "/*", SEARCH);
            // to get definitions...
            //uriMatcher.addURI(AUTHORITY, DB_TABLE_VENUES, TAG_SEARCH_VENUES);
            //uriMatcher.addURI(AUTHORITY, DB_TABLE_VENUES + "/#", TAG_GET_VENUE);
            // to get suggestions...
            //Log.d("SearchManager.SUGGEST_URI_PATH_QUERY", SearchManager.SUGGEST_URI_PATH_QUERY);
            //uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, TAG_SEARCH_SUGGEST);
            //uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", TAG_SEARCH_SUGGEST);

            /* The following are unused in this implementation, but if we include
             * {@link SearchManager#SUGGEST_COLUMN_SHORTCUT_ID} as a column in our suggestions table, we
             * could expect to receive refresh queries when a shortcutted suggestion is displayed in
             * Quick Search Box, in which case, the following Uris would be provided and we
             * would return a cursor with a single item representing the refreshed suggestion data.
             */

            //uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_SHORTCUT, TAG_REFRESH_SHORTCUT);
            //uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_SHORTCUT + "/*", TAG_REFRESH_SHORTCUT);


            uriMatcher.addURI(AUTHORITY, DB_TABLE_VENUES, TAG_VENUES);
            uriMatcher.addURI(AUTHORITY, DB_TABLE_VENUES + "/#", TAG_VENUE_ID);
            uriMatcher.addURI(AUTHORITY, DB_TABLE_USERS, TAG_USERS);
            uriMatcher.addURI(AUTHORITY, DB_TABLE_USERS + "/#", TAG_USERS_ID);
            uriMatcher.addURI(AUTHORITY, DB_TABLE_RESERVATIONS, TAG_RESERVATIONS);
            uriMatcher.addURI(AUTHORITY, DB_TABLE_RESERVATIONS + "/#", TAG_RESERVATIONS_ID);
            uriMatcher.addURI(AUTHORITY, DB_TABLE_CITIES, TAG_CITIES);
            uriMatcher.addURI(AUTHORITY, DB_TABLE_CITIES + "/#", TAG_CITIES_ID);
        }

        public static final String KEY_SEARCH_COLUMN = KEY_COLUMN_1_NAME;
        public static final HashMap<String, String> SEARCH_SUGGEST_PROJECTION_MAP;
        static {
            SEARCH_SUGGEST_PROJECTION_MAP = new HashMap<String, String>();
            SEARCH_SUGGEST_PROJECTION_MAP.put(
                    BaseColumns._ID, KEY_ID + " AS " + BaseColumns._ID);
            SEARCH_SUGGEST_PROJECTION_MAP.put(
                    SearchManager.SUGGEST_COLUMN_TEXT_1,
                    KEY_SEARCH_COLUMN + " AS " + SearchManager.SUGGEST_COLUMN_TEXT_1);
            SEARCH_SUGGEST_PROJECTION_MAP.put(
                    SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, KEY_ID + " AS " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
        }

        // Constants for the database
        //VENUES
        public static final String DB_TABLE_VENUES_ID               = "id";
        public static final String DB_TABLE_VENUES_NAME             = "name";
        public static final String DB_TABLE_VENUES_TYPE             = "type"; //1 - restaurant, 2 - club, 3 - bar, 4 - cafe
        public static final String DB_TABLE_VENUES_PHONE            = "phone";
        public static final String DB_TABLE_VENUES_ADDRESS          = "address";
        public static final String DB_TABLE_VENUES_CITY             = "city";
        public static final String DB_TABLE_VENUES_LAT              = "lat";
        public static final String DB_TABLE_VENUES_LON              = "lon";
        public static final String DB_TABLE_VENUES_WORKTIME         = "worktime";
        public static final String DB_TABLE_VENUES_CAPCITY          = "capacity";
        public static final String DB_TABLE_VENUES_OWNER_ID         = "owner_id";
        public static final String DB_TABLE_VENUES_IMAGE            = "image";
        public static final String DB_TABLE_VENUES_CREATED          = "created";
        public static final String DB_TABLE_VENUES_LAST_UPDATED     = "last_updated";

        public static final String CREATE_TABLE_VENUES = " CREATE TABLE " + DB_TABLE_VENUES +
                " (" + DB_TABLE_VENUES_ID + " INTEGER PRIMARY KEY, " +
                DB_TABLE_VENUES_NAME + " TEXT NOT NULL, " +
                DB_TABLE_VENUES_TYPE + " TEXT NOT NULL, " +
                DB_TABLE_VENUES_PHONE + " TEXT NULL, " +
                DB_TABLE_VENUES_ADDRESS + " TEXT NULL, " +
                DB_TABLE_VENUES_CITY + " TEXT NOT NULL, " +
                DB_TABLE_VENUES_LAT + " DOUBLE NOT NULL, " +
                DB_TABLE_VENUES_LON + " DOUBLE NOT NULL, " +
                DB_TABLE_VENUES_WORKTIME + " TEXT NULL, " +
                DB_TABLE_VENUES_CAPCITY + " INTEGER NULL, " +
                DB_TABLE_VENUES_OWNER_ID + " INTEGER NOT NULL, " +
                DB_TABLE_VENUES_IMAGE + " BLOB NULL, " +
                DB_TABLE_VENUES_CREATED + " INTEGER NOT NULL, " +
                DB_TABLE_VENUES_LAST_UPDATED + " INTEGER NOT NULL);";

        //USERS
        public static final String DB_TABLE_USERS_ID                = "id";
        public static final String DB_TABLE_USERS_TYPE              = "type"; //1 - user, 2 - venue owner
        public static final String DB_TABLE_USERS_EMAIL             = "email";
        public static final String DB_TABLE_USERS_PASS              = "password";
        public static final String DB_TABLE_USERS_FIRST_NAME        = "first_name";
        public static final String DB_TABLE_USERS_LAST_NAME         = "last_name";
        public static final String DB_TABLE_USERS_PHONE             = "phone";
        public static final String DB_TABLE_USERS_CITY              = "city";
        public static final String DB_TABLE_USERS_AVATAR            = "avatar";

        public static final String CREATE_TABLE_USERS = " CREATE TABLE " + DB_TABLE_USERS +
                " (" + DB_TABLE_USERS_ID + " INTEGER PRIMARY KEY, " +
                DB_TABLE_USERS_TYPE + " INTEGER NOT NULL, " +
                DB_TABLE_USERS_EMAIL + " TEXT NOT NULL, " +
                DB_TABLE_USERS_PASS + " TEXT NOT NULL, " +
                DB_TABLE_USERS_FIRST_NAME + " TEXT NOT NULL, " +
                DB_TABLE_USERS_LAST_NAME + " TEXT NOT NULL, " +
                DB_TABLE_USERS_PHONE + " TEXT NOT NULL, " +
                DB_TABLE_USERS_CITY + " TEXT NOT NULL, " +
                DB_TABLE_USERS_AVATAR + " BLOB NULL);";

        //RESERVATIONS
        public static final String DB_TABLE_RESERVATIONS_ID             = "id";
        public static final String DB_TABLE_RESERVATIONS_USER_ID        = "user_id";
        public static final String DB_TABLE_RESERVATIONS_VENUE_ID       = "venue_id";
        public static final String DB_TABLE_RESERVATIONS_DATE_CREATED   = "date_created";
        public static final String DB_TABLE_RESERVATIONS_DATE_BOOKED    = "date_booked";
        public static final String DB_TABLE_RESERVATIONS_PEOPLE_COUNT   = "people_count";
        public static final String DB_TABLE_RESERVATIONS_COMMENT        = "comment";
        public static final String DB_TABLE_RESERVATIONS_ACCEPTED       = "accepted";

        public static final String CREATE_TABLE_RESERVATIONS = " CREATE TABLE " + DB_TABLE_RESERVATIONS +
                " (" + DB_TABLE_RESERVATIONS_ID + " INTEGER PRIMARY KEY, " +
                DB_TABLE_RESERVATIONS_USER_ID + " INTEGER NOT NULL, " +
                DB_TABLE_RESERVATIONS_VENUE_ID + " INTEGER NOT NULL, " +
                DB_TABLE_RESERVATIONS_DATE_CREATED + " INTEGER NOT NULL, " +
                DB_TABLE_RESERVATIONS_DATE_BOOKED + " INTEGER NOT NULL, " +
                DB_TABLE_RESERVATIONS_PEOPLE_COUNT + " INTEGER NOT NULL, " +
                DB_TABLE_RESERVATIONS_COMMENT + " TEXT NULL, " +
                DB_TABLE_RESERVATIONS_ACCEPTED + " INTEGER NOT NULL);";

        //CITIES
        public static final String DB_TABLE_CITIES_ID                = "id";
        public static final String DB_TABLE_CITIES_NAME              = "name";

        public static final String CREATE_TABLE_CITIES = " CREATE TABLE " + DB_TABLE_CITIES +
                " (" + DB_TABLE_CITIES_ID + " INTEGER PRIMARY KEY, " +
                DB_TABLE_CITIES_NAME + " TEXT NOT NULL);";

        public static Map<String, String> create_tables = new HashMap<String, String>();
        public static void init() {
            create_tables.put(DB_TABLE_VENUES, CREATE_TABLE_VENUES);
            create_tables.put(DB_TABLE_USERS, CREATE_TABLE_USERS);
            create_tables.put(DB_TABLE_RESERVATIONS, CREATE_TABLE_RESERVATIONS);
            create_tables.put(DB_TABLE_CITIES, CREATE_TABLE_CITIES);

        }


    // Set to true to turn on debug logging
    public static final boolean LOGD = true;

    // Custom actions

    // Defines a custom Intent action
    public static final String BROADCAST_ACTION = "bg.mentormate.academy.reservations.BROADCAST";

    // Defines the key for the status "extra" in an Intent
    public static final String EXTENDED_DATA_STATUS = "cbg.mentormate.academy.reservations.STATUS";

    // Defines the key for the log "extra" in an Intent
    public static final String EXTENDED_STATUS_LOG = "bg.mentormate.academy.reservations.LOG";

    // Defines the key for storing fullscreen state
    public static final String EXTENDED_FULLSCREEN =
            "bg.mentormate.academy.reservations.EXTENDED_FULLSCREEN";

    /*
     * A user-agent string that's sent to the HTTP site. It includes information about the device
     * and the build that the device is running.
     */
    public static final String USER_AGENT = "Mozilla/5.0 (Linux; U; Android "
            + android.os.Build.VERSION.RELEASE + ";"
            + Locale.getDefault().toString() + "; " + android.os.Build.DEVICE
            + "/" + android.os.Build.ID + ")";

    // Status values to broadcast to the Activity

    // The download is starting
    public static final int STATE_ACTION_STARTED = 0;

    public static final int STATE_ACTION_LOGING = 5;
    public static final int STATE_ACTION_LOGGED_USER = 6;
    public static final int STATE_ACTION_LOGGED_ADMIN = 7;
    public static final int STATE_ACTION_LOGING_FAILED = -5;

    // The background thread is connecting to the RSS feed
    public static final int STATE_ACTION_CONNECTING = 1;
    public static final int STATE_ACTION_CONNECTING_FAILED = -11;

    // The background thread is parsing the RSS feed
    public static final int STATE_ACTION_PARSING = 2;

    // The background thread is writing data to the content provider
    public static final int STATE_ACTION_WRITING = 3;

    // The background thread is done
    public static final int STATE_ACTION_COMPLETE = 4;

    // The background thread is doing logging
    public static final int STATE_LOG = -1;

    public static final CharSequence BLANK = " ";

}
