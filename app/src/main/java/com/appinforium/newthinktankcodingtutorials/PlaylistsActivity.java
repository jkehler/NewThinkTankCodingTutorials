package com.appinforium.newthinktankcodingtutorials;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;

import com.appinforium.newthinktankcodingtutorials.adapter.PlaylistsCursorAdapter;
import com.appinforium.newthinktankcodingtutorials.data.YoutubeDatabase;
import com.appinforium.newthinktankcodingtutorials.data.YoutubeProvider;
import com.appinforium.newthinktankcodingtutorials.service.PlaylistsDownloaderService;

import java.util.List;


public class PlaylistsActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    YoutubeAPI ytApi;
    GridView playlistsGridView;
    public static final String PLAYLIST_ID_MESSAGE = "curPlaylistId";
    public static final String PLAYLIST_TITLE_MESSAGE = "curPlaylistTitle";

    private static final String DEBUG_TAG = "PlaylistsActivity";

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String[] projection = { YoutubeDatabase.COL_PLAYLIST_ID, YoutubeDatabase.COL_TITLE };
        Cursor cursor = getContentResolver().query(
                Uri.withAppendedPath(YoutubeProvider.PLAYLISTS_CONTENT_URI, String.valueOf(id)),
                projection, null, null, null);
        if (cursor.moveToFirst()) {
            String playlistId = cursor.getString(cursor.getColumnIndex(YoutubeDatabase.COL_PLAYLIST_ID));
            String title = cursor.getString(cursor.getColumnIndex(YoutubeDatabase.COL_TITLE));
            Log.d(DEBUG_TAG, "onItemClick called: " + playlistId);

            Intent intent = new Intent(this, PlaylistActivity.class);
            intent.putExtra(PLAYLIST_ID_MESSAGE, playlistId);
            intent.putExtra(PLAYLIST_TITLE_MESSAGE, title);
            cursor.close();
            startActivity(intent);
        } else {

            cursor.close();
        }
//        PlaylistsAdapter.ViewItem item = (PlaylistsAdapter.ViewItem) adapterView.getItemAtPosition(i);
//        Log.d("playlistId", item.playlistId);
//        Intent intent = new Intent(this, PlaylistActivity.class);
//        intent.putExtra(PLAYLIST_ID_MESSAGE, item.playlistId);
//        intent.putExtra(PLAYLIST_TITLE_MESSAGE, item.title);
//        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlists);

        String[] projection = { YoutubeDatabase.ID, YoutubeDatabase.COL_TITLE, YoutubeDatabase.COL_THUMBNAIL_BITMAP };

        Cursor playlistsCursor = getContentResolver().query(YoutubeProvider.PLAYLISTS_CONTENT_URI,
                projection, null, null, null);

        PlaylistsCursorAdapter adapter = new PlaylistsCursorAdapter(getApplicationContext(), playlistsCursor, true);

        Log.d(DEBUG_TAG, "onCreate");

        Intent intent = new Intent(getApplicationContext(), PlaylistsDownloaderService.class);
        intent.putExtra("channel_id", getResources().getString(R.string.channel_id));
        startService(intent);

//        CursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(),
//                R.layout.playlists_grid_item, playlistsCursor, uiBindFrom, uiBindTo);


//        ytApi = new YoutubeAPI(DeveloperKey.DEVELOPER_KEY);
//
        playlistsGridView = (GridView) findViewById(R.id.playlistsGridView);
        playlistsGridView.setAdapter(adapter);
        playlistsGridView.setOnItemClickListener(this);

//        playlistsAdapter = new PlaylistsAdapter(this);
//        playlistsGridView.setAdapter(playlistsAdapter);
//        playlistsGridView.setOnItemClickListener(this);
//
//        GetPlaylists getPlaylists = new GetPlaylists();
//        getPlaylists.execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.playlists, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
