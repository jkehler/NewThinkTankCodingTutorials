package com.appinforium.newthinktankcodingtutorials;

import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.appinforium.newthinktankcodingtutorials.service.PlaylistDownloaderService;

import java.util.List;

public class PlaylistActivity extends ActionBarActivity {

    YoutubeAPI ytApi;
    String playlistId;
    String playlistTitle;
    List<YoutubeAPI.YoutubeItem> videoItems;
    FragmentManager fragmentManager;
    VideoListFragment videoListFragment;

    private static final String DEBUG_TAG = "PlaylistActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        ytApi = new YoutubeAPI(DeveloperKey.DEVELOPER_KEY);

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
        return super.onOptionsItemSelected(item);
    }

    public void refreshClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), PlaylistDownloaderService.class);
        Log.d(DEBUG_TAG, "refreshClicked - playlist_id: " + playlistId);
        intent.putExtra("playlist_id", playlistId);
        startService(intent);
    }

    private class GetVideos extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            videoItems = ytApi.getPlaylistItems(playlistId, 50);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            for (int i = 0; i < videoItems.size(); i++) {
//                Log.d("videoId", videoItems.get(i).videoId);
//            }

            //videoListFragment.updateVideoList(videoItems);

//            playlistsAdapter.setData(response);
//            playlistsAdapter.notifyDataSetChanged();
//            new ThumbnailLoaderTask(playlistsAdapter).execute(response);

        }
    }
}
