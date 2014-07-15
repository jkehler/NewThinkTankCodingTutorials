package com.appinforium.newthinktankcodingtutorials;

import android.app.ListFragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.appinforium.newthinktankcodingtutorials.adapter.PlaylistCursorAdapter;
import com.appinforium.newthinktankcodingtutorials.data.YoutubeDatabase;
import com.appinforium.newthinktankcodingtutorials.data.YoutubeProvider;


public class VideoListFragment extends ListFragment {

    private static final String DEBUG_TAG = "VideoListFragment";
    String playlistId;

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.d(DEBUG_TAG, "Selected id: " + id);
        OnVideoSelectedListener listener = (OnVideoSelectedListener) getActivity();
        listener.onVideoSelected(id);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] projection = { YoutubeDatabase.ID, YoutubeDatabase.COL_VIDEO_ID,
                YoutubeDatabase.COL_TITLE, YoutubeDatabase.COL_THUMBNAIL_URL };

        Log.d(DEBUG_TAG, "playlistId: " + getActivity().getIntent().getStringExtra(PlaylistsActivity.PLAYLIST_ID_MESSAGE));

//        if (savedInstanceState != null) {
//            playlistId = savedInstanceState.getString("playlistId");
//            Log.d(DEBUG_TAG, "restoring playlistId from savedInstanceState");
//        } else {
        playlistId = getActivity().getIntent().getStringExtra(PlaylistsActivity.PLAYLIST_ID_MESSAGE);
//        }

        Uri content_uri = Uri.withAppendedPath(
                YoutubeProvider.VIDEOS_CONTENT_URI,
                playlistId
        );

        Cursor cursor = getActivity().getContentResolver().query(content_uri, projection, null, null, null);

        PlaylistCursorAdapter adapter = new PlaylistCursorAdapter(getActivity(), cursor, true);
        setListAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d(DEBUG_TAG, "onSaveInstanceState called");
    }

    public interface OnVideoSelectedListener {
        public void onVideoSelected(long id);
    }

}
