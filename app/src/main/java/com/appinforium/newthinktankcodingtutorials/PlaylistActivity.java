package com.appinforium.newthinktankcodingtutorials;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.appinforium.newthinktankcodingtutorials.R;

public class PlaylistActivity extends ActionBarActivity {

    YoutubeAPI ytApi;
    String playlistId;
    String playlistTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        ytApi = new YoutubeAPI(DeveloperKey.DEVELOPER_KEY);

        Intent intent = this.getIntent();
        playlistTitle = intent.getStringExtra(PlaylistsActivity.PLAYLIST_TITLE_MESSAGE);
        playlistId = intent.getStringExtra(PlaylistsActivity.PLAYLIST_ID_MESSAGE);
        this.setTitle(playlistTitle);
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
        return super.onOptionsItemSelected(item);
    }
}
