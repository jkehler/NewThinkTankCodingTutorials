package com.appinforium.newthinktankcodingtutorials;

import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.appinforium.newthinktankcodingtutorials.service.PlaylistDownloaderService;
import com.appinforium.newthinktankcodingtutorials.service.ThumbnailDownloaderService;

import java.util.List;

public class PlaylistActivity extends ActionBarActivity implements VideoListFragment.OnVideoSelectedListener {

    String playlistId;
    String playlistTitle;
    FragmentManager fragmentManager;
    VideoListFragment videoListFragment;

    private final static String PLAYLIST_ID = "PLAYLIST_ID";
    private final static String PLAYLIST_TITLE = "PLAYLIST_TITLE";

    private static final String DEBUG_TAG = "PlaylistActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);


        if (savedInstanceState == null) {
            Intent intent = this.getIntent();
            playlistTitle = intent.getStringExtra(PlaylistsActivity.PLAYLIST_TITLE_MESSAGE);
            playlistId = intent.getStringExtra(PlaylistsActivity.PLAYLIST_ID_MESSAGE);
            Log.d(DEBUG_TAG, "savedInstanceState is null");
        } else {
            playlistId = savedInstanceState.getString(PLAYLIST_ID);
            playlistTitle = savedInstanceState.getString(PLAYLIST_TITLE);
            Log.d(DEBUG_TAG, "restoring from savedInstanceState");
        }
        this.setTitle(playlistTitle);

        fragmentManager = getSupportFragmentManager();
        videoListFragment = (VideoListFragment) fragmentManager.findFragmentById(R.id.fragmentVideoList);


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(DEBUG_TAG, "saving playlistId and playlistTitle to savedInstanceState");
        outState.putString(PLAYLIST_ID, playlistId);
        outState.putString(PLAYLIST_TITLE, playlistTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.playlist, menu);
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
        if (id == R.id.action_refresh) {
            Intent intent = new Intent(getApplicationContext(), PlaylistDownloaderService.class);
            Log.d(DEBUG_TAG, "refreshClicked - playlist_id: " + playlistId);
            intent.putExtra("playlist_id", playlistId);
            startService(intent);

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onVideoSelected(long id) {
        Log.d(DEBUG_TAG, "Received id: " + id);

        VideoDetailFragment videoDetailFragment = (VideoDetailFragment)
                fragmentManager.findFragmentById(R.id.fragmentVideoDetail);

        if (videoDetailFragment == null || !videoDetailFragment.isVisible()) {
            Intent intent = new Intent(this, VideoDetailActivity.class);
            intent.putExtra(VideoDetailActivity.ITEM_ID_MESSAGE, id);
            startActivity(intent);
        } else {
            videoDetailFragment.setVideo(id);
        }
    }
}
