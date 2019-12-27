package com.example.runtracker.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.content.UriMatcher;
import android.text.TextUtils;

import com.example.runtracker.DBHandler;

public class MyContentProvider extends ContentProvider {

    private DBHandler myDB;

    private static final String AUTHORITY =
            "com.example.runtracker.ContentProvider.MyContentProvider";
    private static final String TABLE_RUNNER = "Runner";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + TABLE_RUNNER);

    public static final int RUNNER = 1;
    public static final int RUNNER_ID = 2;

    private static final UriMatcher sURIMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, TABLE_RUNNER, RUNNER);
        sURIMatcher.addURI(AUTHORITY, TABLE_RUNNER + "/#",
                RUNNER_ID);
    }

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = myDB.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case RUNNER:
                rowsDeleted = sqlDB.delete(DBHandler.TABLE_RUNNER,
                        selection,
                        selectionArgs);
                break;
            case RUNNER_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(DBHandler.TABLE_RUNNER,
                            DBHandler.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(DBHandler.TABLE_RUNNER,
                            DBHandler.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " +
                        uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = myDB.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case RUNNER:
                id = sqlDB.insert(DBHandler.TABLE_RUNNER,
                        null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "
                        + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(TABLE_RUNNER + "/" + id);
    }

    @Override
    public boolean onCreate() {
        myDB = new DBHandler(getContext(), null, null, 1);
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DBHandler.TABLE_RUNNER);
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case RUNNER_ID:
                queryBuilder.appendWhere(DBHandler.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            case RUNNER:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
        Cursor cursor = queryBuilder.query(myDB.getReadableDatabase(),
                projection, selection, selectionArgs, null, null,
                sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),
                uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = myDB.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case RUNNER:
                rowsUpdated =
                        sqlDB.update(DBHandler.TABLE_RUNNER,
                                values,
                                selection,
                                selectionArgs);
                break;
            case RUNNER_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated =
                            sqlDB.update(DBHandler.TABLE_RUNNER,
                                    values,
                                    DBHandler.COLUMN_ID + "=" + id,
                                    null);
                } else {
                    rowsUpdated =
                            sqlDB.update(DBHandler.TABLE_RUNNER,
                                    values,
                                    DBHandler.COLUMN_ID + "=" + id
                                            + " and "
                                            + selection,
                                    selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "
                        + uri);
        }
        getContext().getContentResolver().notifyChange(uri,
                null);
        return rowsUpdated;
    }
}
