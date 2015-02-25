package bg.mentormate.academy.reservations.providers;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import bg.mentormate.academy.reservations.database.DBConstants;
import bg.mentormate.academy.reservations.database.DBHelper;

/**
 * Created by Student16 on 2/5/2015.
 */
public class CustomContentProvider extends ContentProvider {

    String TAG = "CustomContentProvider";

    private DBHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();

        if(database == null)
            return false;
        else
            return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {


        int uriType = DBConstants.uriMatcher.match(uri);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // Use the UriMatcher to see what kind of query we have and format the db query accordingly
        switch            (uriType){
            // If this is a row query, limit the result set to the passed in row.
            case DBConstants.TAG_VENUE_ID:
            case DBConstants.SINGLE_ROW:
                    queryBuilder.setTables(DBConstants.DB_TABLE_VENUES);
                    String rowID = uri.getPathSegments().get(1);
                    queryBuilder.appendWhere(DBConstants.KEY_ID + "=" + rowID);
                break;

            case DBConstants.SEARCH:

                queryBuilder.setTables(DBConstants.DB_TABLE_VENUES);
                queryBuilder.setProjectionMap(DBConstants.SEARCH_SUGGEST_PROJECTION_MAP);
                Log.d("URI", uri.toString());
                if (selectionArgs == null) {
                    throw new IllegalArgumentException(
                            "selectionArgs must be provided for the Uri: " + uri);
                }
                selectionArgs[0] = "%" + selectionArgs[0] + "%";
//                String query = selectionArgs[0];
//                queryBuilder.appendWhere(DBConstants.KEY_SEARCH_COLUMN +
//                        " LIKE \"%" + query + "%\"");
//                selectionArgs = null;
                Cursor cursor = queryBuilder.query(database, projection, "name LIKE ?", selectionArgs, null, null, sortOrder);
                if (cursor.moveToFirst()) {
                    do {
                        Log.d("SUGGESTION", cursor.getString(cursor.getColumnIndex("_id")));
                        Log.d("SUGGESTION", cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1)));

                    } while (cursor.moveToNext());
                }
                break;
            case DBConstants.ALLROWS:
            case DBConstants.TAG_VENUES:
                queryBuilder.setTables(DBConstants.DB_TABLE_VENUES);
                if (sortOrder == null || sortOrder == ""){
                    // No sorting-> sort on names by default
                    sortOrder = DBConstants.DB_TABLE_VENUES_NAME;
                }
                break;
//            case DBConstants.TAG_SEARCH_SUGGEST:
//                if (selectionArgs == null) {
//                    throw new IllegalArgumentException(
//                            "selectionArgs must be provided for the Uri: " + uri);
//                }
//                return getSuggestions(selectionArgs[0]);
//            case DBConstants.TAG_SEARCH_VENUES:
//                if (selectionArgs == null) {
//                    throw new IllegalArgumentException(
//                            "selectionArgs must be provided for the Uri: " + uri);
//                }
//                return search(selectionArgs[0]);
//            case DBConstants.TAG_GET_VENUE:
//                return getWord(uri);
//            case DBConstants.TAG_REFRESH_SHORTCUT:
//                return refreshShortcut(uri);
//            default:
//                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }

        if (projection != null) {
            Log.d("SELECT", projection[0]);
        }
        if (selection != null) {
            Log.d("WHERE", selection);
        }
        if (selectionArgs != null) {
            Log.d("ARGS", selectionArgs[0]);
        }
        Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
        // Register to watch a content URI for changes
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        // Return a string that identifies the MIME type
        // for a Content Provider URI
        switch (DBConstants.uriMatcher.match(uri)) {
            case DBConstants.ALLROWS:
                return DBConstants.VENUES_MIME_TYPE;
            case DBConstants.SINGLE_ROW:
                return DBConstants.DEFINITION_MIME_TYPE;
            case DBConstants.SEARCH:
                return SearchManager.SUGGEST_MIME_TYPE;
            //default:
            //    throw new IllegalArgumentException("Unknown URL " + uri);
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri _uri = null;
        String tableName = getTable(uri);
        long _ID = database.insert(tableName, "", values);
        if (_ID > 0) {
            _uri = ContentUris.withAppendedId(uri, _ID);
            getContext().getContentResolver().notifyChange(_uri, null);
        }
        return _uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        String tableName = getTable(uri);
        count = database.delete(tableName, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        String tableName = getTable(uri);
        count = database.update(tableName, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    private String getTable(Uri uri) throws SQLException {
        String tableName;
        switch (DBConstants.uriMatcher.match(uri)){
//            case DBConstants.TAG_SEARCH_SUGGEST:
//            case DBConstants.TAG_SEARCH_VENUES:
//            case DBConstants.TAG_GET_VENUE:
//            case DBConstants.TAG_REFRESH_SHORTCUT:
            case DBConstants.TAG_VENUES:
                tableName = DBConstants.DB_TABLE_VENUES;
                break;
            case DBConstants.TAG_USERS:
                tableName = DBConstants.DB_TABLE_USERS;
                break;
            case DBConstants.TAG_RESERVATIONS:
                tableName = DBConstants.DB_TABLE_RESERVATIONS;
                break;
            case DBConstants.TAG_CITIES:
                tableName = DBConstants.DB_TABLE_CITIES;
                break;
            default: throw new SQLException("Failed to define table name row from " + uri);
        }
        Log.d("TABLE", tableName);
        return tableName;
    }

    /**
     * Returns a Cursor over all words that match the given query
     *
     * @param query The string to search for
     * @param columns The columns to include, if null then all are included
     * @return Cursor over all words that match, or null if none found.
     */
    public Cursor getWordMatches(String query, String[] columns) {
        Log.d(TAG,"getWordMatches");
        String selection = DBConstants.DB_TABLE_VENUES_NAME + " LIKE ?";
        String[] selectionArgs = new String[] {query+"%"};

        return getSearchResults(selection, selectionArgs, columns);

        /* This builds a query that looks like:
         *     SELECT <columns> FROM <table> WHERE <KEY_WORD> MATCH 'query*'
         * which is an FTS3 search for the query text (plus a wildcard) inside the word column.
         *
         * - "rowid" is the unique id for all rows but we need this value for the "_id" column in
         *    order for the Adapters to work, so the columns need to make "_id" an alias for "rowid"
         * - "rowid" also needs to be used by the SUGGEST_COLUMN_INTENT_DATA alias in order
         *   for suggestions to carry the proper intent data.
         *   These aliases are defined in the DictionaryProvider when queries are made.
         * - This can be revised to also search the definition text with FTS3 by changing
         *   the selection clause to use FTS_VIRTUAL_TABLE instead of KEY_WORD (to search across
         *   the entire table, but sorting the relevance could be difficult.
         */
    }

    /**
     * Performs a database query.
     * @param selection The selection clause
     * @param selectionArgs Selection arguments for "?" components in the selection
     * @param columns The columns to return
     * @return A Cursor over all rows matching the query
     */
    private Cursor getSearchResults(String selection, String[] selectionArgs, String[] columns) {
        Log.d("getSearchResults", columns.toString());
        Cursor cursor = query(DBConstants.CONTENT_URI_VENUES,
                columns, selection, selectionArgs, DBConstants.DB_TABLE_VENUES_NAME);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }
}
