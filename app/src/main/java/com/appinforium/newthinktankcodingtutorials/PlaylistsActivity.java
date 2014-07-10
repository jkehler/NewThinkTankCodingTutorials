package com.appinforium.newthinktankcodingtutorials;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.List;


public class PlaylistsActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    YoutubeAPI ytApi;
    PlaylistsAdapter playlistsAdapter;
    GridView playlistsGridView;
    public static final String PLAYLIST_ID_MESSAGE = "curPlaylistId";
    public static final String PLAYLIST_TITLE_MESSAGE = "curPlaylistTitle";

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        PlaylistsAdapter.ViewItem item = (PlaylistsAdapter.ViewItem) adapterView.getItemAtPosition(i);
        Log.d("playlistId", item.playlistId);
        Intent intent = new Intent(this, PlaylistActivity.class);
        intent.putExtra(PLAYLIST_ID_MESSAGE, item.playlistId);
        intent.putExtra(PLAYLIST_TITLE_MESSAGE, item.title);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlists);

        ytApi = new YoutubeAPI(DeveloperKey.DEVELOPER_KEY);

        playlistsGridView = (GridView) findViewById(R.id.playlistsGridView);
        playlistsAdapter = new PlaylistsAdapter(this);
        playlistsGridView.setAdapter(playlistsAdapter);
        playlistsGridView.setOnItemClickListener(this);

        GetPlaylists getPlaylists = new GetPlaylists();
        getPlaylists.execute();

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


    private class GetPlaylists extends AsyncTask<Void, Void, Void> {

        List<YoutubeAPI.PlaylistItem> response;

        @Override
        protected Void doInBackground(Void... voids) {
            response = ytApi.getChannelPlaylists("UCwRXb5dUK4cvsHbx-rGzSgw", 50);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            playlistsAdapter.setData(response);
            playlistsAdapter.notifyDataSetChanged();
            new ThumbnailLoaderTask(playlistsAdapter).execute(response);

        }
    }
}
