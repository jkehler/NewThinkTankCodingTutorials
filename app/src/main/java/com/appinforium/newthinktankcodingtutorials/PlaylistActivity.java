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

public class PlaylistActivity extends ActionBarActivity {

    String playlistId;
    String playlistTitle;
    FragmentManager fragmentManager;
    VideoListFragment videoListFragment;

    private static final String DEBUG_TAG = "PlaylistActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        ActionBar actionBar = getSupportActionBar();


        Intent intent = this.getIntent();
        playlistTitle = intent.getStringExtra(PlaylistsActivity.PLAYLIST_TITLE_MESSAGE);
        playlistId = intent.getStringExtra(PlaylistsActivity.PLAYLIST_ID_MESSAGE);
        this.setTitle(playlistTitle);

        fragmentManager = getSupportFragmentManager();
        videoListFragment = (VideoListFragment) fragmentManager.findFragmentById(R.id.fragmentVideoList);


//        GetVideos getVideos = new GetVideos();
//        getVideos.execute();
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


}
