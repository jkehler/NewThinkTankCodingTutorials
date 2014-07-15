package com.appinforium.newthinktankcodingtutorials;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.appinforium.newthinktankcodingtutorials.service.PlaylistUpdaterIntentService;


public class PlaylistActivity extends Activity implements
        VideoListFragment.OnVideoSelectedListener {

    String playlistId;
    String playlistTitle;
    boolean isDynamic;

    private final static String PLAYLIST_ID = "PLAYLIST_ID";
    private final static String PLAYLIST_TITLE = "PLAYLIST_TITLE";

    private static final String DEBUG_TAG = "PlaylistActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        Log.d(DEBUG_TAG, "onCreate called - savedInstanceState: " + savedInstanceState);
//        if (savedInstanceState == null) {
            Intent intent = this.getIntent();
            playlistTitle = intent.getStringExtra(PlaylistsActivity.PLAYLIST_TITLE_MESSAGE);
            playlistId = intent.getStringExtra(PlaylistsActivity.PLAYLIST_ID_MESSAGE);
//            Log.d(DEBUG_TAG, "savedInstanceState is null");
//        } else {
//            playlistId = savedInstanceState.getString(PLAYLIST_ID);
//            playlistTitle = savedInstanceState.getString(PLAYLIST_TITLE);
//            Log.d(DEBUG_TAG, "restoring from savedInstanceState");
//        }
        this.setTitle(playlistTitle);

        FragmentManager fragmentManager = getFragmentManager();

        // Test if we are in a dynamic layout
        Fragment videoDetailFragment = fragmentManager.findFragmentById(R.id.fragmentVideoDetail);
        isDynamic = videoDetailFragment == null || !videoDetailFragment.isInLayout();

        Log.d(DEBUG_TAG, "isDynamic: " + isDynamic);

        // Dynamically load the video List fragment if necessary
        if (isDynamic && savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            VideoListFragment videoListFragment = new VideoListFragment();

            fragmentTransaction.add(R.id.playlistLayoutRoot, videoListFragment, "videoList");
            fragmentTransaction.commit();
        }

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
            Intent intent = new Intent(getApplicationContext(), PlaylistUpdaterIntentService.class);
            intent.putExtra(PlaylistUpdaterIntentService.PLAYLIST_ID, playlistId);
            startService(intent);

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onVideoSelected(long id) {
        Log.d(DEBUG_TAG, "Received id: " + id);

        VideoDetailFragment videoDetailFragment;
        FragmentManager fragmentManager = getFragmentManager();


        if (isDynamic) {
            // Dynamically swap the videoList fragment with videoDetail
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            videoDetailFragment = new VideoDetailFragment();

            // pass video Index to fragment via args
            Bundle args = new Bundle();
            args.putLong(VideoDetailFragment.VIDEO_INDEX, id);
            videoDetailFragment.setArguments(args);

            fragmentTransaction.replace(R.id.playlistLayoutRoot, videoDetailFragment, "videoDetail");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.setCustomAnimations(
                    android.R.animator.fade_in, android.R.animator.fade_out);
            fragmentTransaction.commit();

        } else {
            // use the already visible videoDetail fragment
            videoDetailFragment = (VideoDetailFragment)
                    fragmentManager.findFragmentById(R.id.fragmentVideoDetail);
            videoDetailFragment.setVideo(id);
        }

    }


}
