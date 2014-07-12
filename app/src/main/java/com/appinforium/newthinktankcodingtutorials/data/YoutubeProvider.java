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
    public static final int PLAYLISTS = 100;
    public static final int PLAYLIST_ID = 101;
    public static final int PLAYLIST_STR_ID = 102;
    public static final int PLAYLIST_VIDEOS = 200;
    public static final int VIDEO_ID = 201;
    public static final int VIDEO = 202;

    private static final String PLAYLISTS_BASE_PATH = "playlists";
    private static final String VIDEO_BASE_PATH = "video";
    private static final String VIDEOS_BASE_PATH = "videos";

    public static final Uri PLAYLISTS_CONTENT_URI = Uri.parse("content://" + AUTHORITY
                + "/" + PLAYLISTS_BASE_PATH);
    public static final Uri VIDEO_CONTENT_URI = Uri.parse("content://" + AUTHORITY
                + "/" + VIDEO_BASE_PATH);
    public static final Uri VIDEOS_CONTENT_URI = Uri.parse("content://" + AUTHORITY
                + "/" + VIDEOS_BASE_PATH);

//    public static final String PLAYLIST_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
//            + "/mt-playlist";
//    public static final String PLAYLIST_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
//            + "/mt-playlist";
//
//    public static final String VIDEO_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
//            + "/mt-video";
//    public static final String VIDEO_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
//            + "/mt-video";

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, PLAYLISTS_BASE_PATH, PLAYLISTS);
        sURIMatcher.addURI(AUTHORITY, PLAYLISTS_BASE_PATH + "/#", PLAYLIST_ID);
        //sURIMatcher.addURI(AUTHORITY, VIDEOS_BASE_PATH playlist/#", PLAYLIST_VIDEOS);
        //sURIMatcher.addURI(AUTHORITY, VIDEOS_BASE_PATH, VIDEOS);
        sURIMatcher.addURI(AUTHORITY, VIDEO_BASE_PATH, VIDEO);
        sURIMatcher.addURI(AUTHORITY, VIDEO_BASE_PATH + "/#", VIDEO_ID);
        sURIMatcher.addURI(AUTHORITY, VIDEOS_BASE_PATH + "/*", PLAYLIST_VIDEOS);
    }

    public boolean onCreate() {
        mDB = new YoutubeDatabase(getContext());

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        Log.d(DEBUG_TAG, "query uri: " + uri.toString());
        int uriType = sURIMatcher.match(uri);

        Log.d(DEBUG_TAG, "uriType: " + uriType);

        switch (uriType) {
            case PLAYLIST_ID:
                queryBuilder.setTables(YoutubeDatabase.TABLE_PLAYLISTS);
                queryBuilder.appendWhere(YoutubeDatabase.ID + "="
                        + uri.getLastPathSegment());
                break;
            case PLAYLISTS:
                queryBuilder.setTables(YoutubeDatabase.TABLE_PLAYLISTS);
                // no filter
                break;
            case VIDEO_ID:
                queryBuilder.setTables(YoutubeDatabase.TABLE_VIDEOS);
                queryBuilder.appendWhere(YoutubeDatabase.ID + "="
                        + uri.getLastPathSegment());
                break;
            case PLAYLIST_VIDEOS:
                queryBuilder.setTables(YoutubeDatabase.TABLE_VIDEOS);
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
            case VIDEO:
                try {
                    long newID = db.insertOrThrow(YoutubeDatabase.TABLE_VIDEOS,
                            null, contentValues);
                    if (newID > 0) {
                        Log.d(DEBUG_TAG, "we here?");
                        Uri newUri = ContentUris.withAppendedId(uri, newID);
                        getContext().getContentResolver().notifyChange(uri, null);
                        db.close();
                        return newUri;
                    } else {
                        throw new SQLException("Failed to insert row into " + uri);
                    }
                } catch (SQLiteConstraintException e) {
                    Log.i(DEBUG_TAG, "Ignoring constraint failure.", e);
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
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
