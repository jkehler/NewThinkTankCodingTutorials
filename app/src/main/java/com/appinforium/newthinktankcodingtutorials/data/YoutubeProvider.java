package com.appinforium.newthinktankcodingtutorials.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.auth.AUTH;

import java.sql.SQLException;

/**
 * Created by jeff on 7/10/14.
 */
public class YoutubeProvider extends ContentProvider {

    private static final String DEBUG_TAG = "YoutubeProvider";

    private YoutubeDatabase mDB;



    private static final String AUTHORITY = "com.appinforium.newthinktankcodingtutorials.data.YoutubeProvider";

    private static final int GET_PLAYLISTS = 100;
    private static final int GET_PLAYLIST_ID = 101;
    private static final int GET_PLAYLIST_VIDEOS = 102;
    private static final int GET_PLAYLIST_VIDEO_ID = 103;
    private static final int GET_NULL_PLAYLIST_THUMBNAILS = 104;
    private static final int GET_NULL_VIDEO_THUMBNAILS = 105;

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);
    static {
        // content://com.appinforium.newthinktankcodingtutorials.data.YoutubeProvider/playlists
        sURIMatcher.addURI(AUTHORITY, "playlists", GET_PLAYLISTS);

        // content://com.appinforium.newthinktankcodingtutorials.data.YoutubeProvider/playlists/#
        sURIMatcher.addURI(AUTHORITY, "playlists/#", GET_PLAYLIST_ID);

        // content://com.appinforium.newthinktankcodingtutorials.data.YoutubeProvider/videos/*
        sURIMatcher.addURI(AUTHORITY, "videos/*", GET_PLAYLIST_VIDEOS);

        // content://com.appinforium.newthinktankcodingtutorials.data.YoutubeProvider/videos/*/#
        sURIMatcher.addURI(AUTHORITY, "videos/*/#", GET_PLAYLIST_VIDEO_ID);

        // content://com.appinforium.newthinktankcodingtutorials.data.YoutubeProvider/null_thumbnails
        sURIMatcher.addURI(AUTHORITY, "null_thumbnails", GET_NULL_PLAYLIST_THUMBNAILS);

        // content://com.appinforium.newthinktankcodingtutorials.data.YoutubeProvider/null_thumbnails/*
        sURIMatcher.addURI(AUTHORITY, "null_thumbnails/*", GET_NULL_VIDEO_THUMBNAILS);

    }

    public static final Uri PLAYLISTS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/playlists");
    public static final Uri VIDEOS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/videos");
    public static final Uri THUMBNAILS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/null_thumbnails");

    public boolean onCreate() {
        mDB = new YoutubeDatabase(getContext());

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        int uriType = sURIMatcher.match(uri);

        switch (uriType) {
            case GET_PLAYLIST_ID:
                queryBuilder.setTables(YoutubeDatabase.TABLE_PLAYLISTS);
                queryBuilder.appendWhere(YoutubeDatabase.ID + "="
                        + uri.getLastPathSegment());
                break;
            case GET_PLAYLISTS:
                queryBuilder.setTables(YoutubeDatabase.TABLE_PLAYLISTS);
                // no filter
                break;
            case GET_PLAYLIST_VIDEO_ID:
                queryBuilder.setTables(YoutubeDatabase.TABLE_VIDEOS);
                queryBuilder.appendWhere(YoutubeDatabase.ID + "="
                        + uri.getLastPathSegment());
                break;
            case GET_PLAYLIST_VIDEOS:
                queryBuilder.setTables(YoutubeDatabase.TABLE_VIDEOS);
                queryBuilder.appendWhere(YoutubeDatabase.COL_PLAYLIST_ID + " = '"
                        + uri.getLastPathSegment() + "'");
                break;
            case GET_NULL_VIDEO_THUMBNAILS:
                queryBuilder.setTables(YoutubeDatabase.TABLE_VIDEOS);
                queryBuilder.appendWhere(YoutubeDatabase.COL_THUMBNAIL_BITMAP + " is null and ");
                queryBuilder.appendWhere(YoutubeDatabase.COL_THUMBNAIL_URL + " is not null and ");
                queryBuilder.appendWhere(YoutubeDatabase.COL_PLAYLIST_ID + " = '"
                        + uri.getLastPathSegment() + "'");
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(mDB.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        Log.d(DEBUG_TAG, "query uri: " + uri.toString());
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase db = mDB.getWritableDatabase();
        Log.d(DEBUG_TAG, "uriType: " + String.valueOf(uriType));

        switch (uriType) {
            case GET_PLAYLIST_VIDEOS:
                try {
                    long newID = db.insertOrThrow(YoutubeDatabase.TABLE_VIDEOS,
                            null, contentValues);
                    if (newID > 0) {
                        Uri newUri = ContentUris.withAppendedId(uri, newID);
                        //Log.d(DEBUG_TAG, "notifyChange uri: " + uri);
                        //Log.d(DEBUG_TAG, "notifyChange newUri: " + newUri);
                        getContext().getContentResolver().notifyChange(newUri, null);
                        return newUri;
                    } else {
                        throw new SQLException("Failed to insert row into " + uri);
                    }
                } catch (SQLiteConstraintException e) {
                    Log.i(DEBUG_TAG, "Ignoring constraint failure.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid URI for insert");

        }

        db.close();
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase db = mDB.getWritableDatabase();

        Log.d(DEBUG_TAG, "update uri: " + uri);
        int rowsAffected = 0;

        switch (uriType) {
            case GET_PLAYLIST_VIDEO_ID:
                String id = uri.getLastPathSegment();
                StringBuilder modSelection = new StringBuilder(YoutubeDatabase.ID + "=" + id);

                if (!TextUtils.isEmpty(selection)) {
                    modSelection.append(" AND " + selection);
                }

                rowsAffected = db.update(YoutubeDatabase.TABLE_VIDEOS,
                        contentValues, modSelection.toString(), selectionArgs);

                Log.d(DEBUG_TAG, "rowsAffected: " + String.valueOf(rowsAffected));
                break;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsAffected;
    }
}
