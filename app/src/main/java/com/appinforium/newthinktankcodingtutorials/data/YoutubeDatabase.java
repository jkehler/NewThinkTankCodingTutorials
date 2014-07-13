package com.appinforium.newthinktankcodingtutorials.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by jeff on 7/10/14.
 */
public class YoutubeDatabase extends SQLiteOpenHelper {

    private static final String DEBUG_TAG = "YoutubeDatabase";
    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "youtube_data";

    public static final String TABLE_PLAYLISTS = "playlists";
    public static final String TABLE_VIDEOS = "videos";
    public static final String ID = "_id";
    public static final String COL_PLAYLIST_ID = "playlist_id";
    public static final String COL_TITLE = "title";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_VIDEO_ID = "video_id";
    public static final String COL_PUBLISHED_AT = "published_at";
    public static final String COL_THUMBNAIL_URL = "thumbnail_url";
    public static final String COL_THUMBNAIL_BITMAP = "thumbnail_bitmap";
    public static final String COL_POSITION = "position";

    private static final String CREATE_TABLE_PLAYLISTS = "CREATE TABLE " + TABLE_PLAYLISTS
            + " (" + ID + " integer primary key autoincrement, "
            + COL_TITLE + " text not null, "
            + COL_PLAYLIST_ID + " text UNIQUE not null, "
            + COL_DESCRIPTION + " text, "
            + COL_PUBLISHED_AT + " datetime not null, "
            + COL_THUMBNAIL_URL + " text, "
            + COL_THUMBNAIL_BITMAP + " blob);";

    private static final String CREATE_TABLE_VIDEOS = "CREATE TABLE " + TABLE_VIDEOS
            + " (" + ID + " integer primary key autoincrement, "
            + COL_TITLE + " text not null, "
            + COL_VIDEO_ID + " text UNIQUE not null, "
            + COL_PLAYLIST_ID + " text not null, "
            + COL_DESCRIPTION + " text not null, "
            + COL_THUMBNAIL_URL + " text not null, "
            + COL_PUBLISHED_AT + " datetime not null, "
            + COL_POSITION + " integer not null, "
            + COL_THUMBNAIL_BITMAP + " blob);";

    public YoutubeDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PLAYLISTS);
        db.execSQL(CREATE_TABLE_VIDEOS);
        Log.d(DEBUG_TAG, "onCreate called");
//        seedPlaylistData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(DEBUG_TAG, "Upgrading database. Existing contents will be lost. ["
                + oldVersion + "]->[" + newVersion + "]");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLISTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEOS);
        onCreate(db);
    }

    private void seedPlaylistData(SQLiteDatabase db) {
        String baseQuery = "INSERT INTO " + TABLE_PLAYLISTS + " ("
                + COL_TITLE + ", "
                + COL_PLAYLIST_ID + ", "
                + COL_THUMBNAIL_URL + ", "
                + COL_THUMBNAIL_BITMAP + ") values ";
        db.execSQL(baseQuery + "('Android Studio', 'PLGLfVvz_LVvSPjWpLPFEfOCbezi6vATIh', null, null);");
        db.execSQL(baseQuery + "('Git', 'PLGLfVvz_LVvQHO1PfyscjIPkNJjgHsLyH', null, null);");
        db.execSQL(baseQuery + "('Ask an App Developer Anything', 'PLGLfVvz_LVvSpUc78P7PBfLlrQEEth0lm', null, null);");
        db.execSQL(baseQuery + "('Android Development for Beginners', 'PLGLfVvz_LVvSKgnFm8-6Fz1cd6zt_KxTC', null, null);");
        db.execSQL(baseQuery + "('Inkscape Video Tutorials', 'PLGLfVvz_LVvTSi9bKrvGR2_DBg0Tv8Dxo', null, null);");
        db.execSQL(baseQuery + "('C Programming', 'PLGLfVvz_LVvSaXCpKS395wbCcmsmgRea7', null, null);");
        db.execSQL(baseQuery + "('Android Development', 'PLGLfVvz_LVvQUjiCc8lUT9aO0GsWA4uNe', null, null);");
        db.execSQL(baseQuery + "('Java Algorithms', 'PLGLfVvz_LVvReUrWr94U-ZMgjYTQ538nT', null, null);");
        db.execSQL(baseQuery + "('How to Layout a Website', 'PLGLfVvz_LVvT59FVZJeJtVUr3h7PluW6Q', null, null);");
        db.execSQL(baseQuery + "('Code Refactoring', 'PLGLfVvz_LVvSuz6NuHAzpM52qKM6bPlCV', null, null);");
        db.execSQL(baseQuery + "('Object Oriented Design', 'PLGLfVvz_LVvS5P7khyR4xDp7T9lCk9PgE', null, null);");
        db.execSQL(baseQuery + "('UML 2.0', 'PLGLfVvz_LVvQ5G-LdJ8RLqe-ndo7QITYc', null, null);");
        db.execSQL(baseQuery + "('Design Patterns', 'PLF206E906175C7E07', null, null);");
        db.execSQL(baseQuery + "('XML', 'PLBB413675AFBDC1F4', null, null);");
        db.execSQL(baseQuery + "('Java Programming', 'PLE7E8B7F4856C9B19', null, null);");
        db.execSQL(baseQuery + "('Learn HTML', 'PL82EBDA7BEB6BD571', null, null);");
        db.execSQL(baseQuery + "('JQuery & Ajax', 'PL3877C5434C042349', null, null);");
        db.execSQL(baseQuery + "('Javascript Programming', 'PLBA965A22D89CF13B', null, null);");
        db.execSQL(baseQuery + "('Cascading Style Sheets', 'PL07454EA7FF8D28AB', null, null);");
        db.execSQL(baseQuery + "('PHP & MySQL', 'PL2407F4EE0530B251', null, null);");

        Log.d(DEBUG_TAG, "seedPlaylistData called");
    }

}
