
package iak.almuwahhid.com.iak1.Helper;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by gueone on 5/12/2017.
 */

public class TaskContentProvider  extends ContentProvider {
    private Database mydb;
    public static final int TASKS = 100;
    public static final int TASK_WITH_ID = 101;

    static final String PROVIDER_NAME = "com.iak.almuwahhid";
    String TABLE_NAME = "github";

    static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/repo");

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "/repo", TASKS);
        uriMatcher.addURI(PROVIDER_NAME, "/repo" + "/#", TASK_WITH_ID);

        return uriMatcher;
    }



    @Override
    public boolean onCreate() {
        Context context = getContext();
        mydb = new Database(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mydb.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursor;

        switch (match) {
            // Query for the tasks directory
            case TASKS:
                cursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mydb.getWritableDatabase();
        // Write URI matching code to identify the match for the tasks directory
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned

        switch (match) {
            case TASKS:
                long id = db.insert(TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
