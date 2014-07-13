package com.appinforium.newthinktankcodingtutorials;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.appinforium.newthinktankcodingtutorials.adapter.PlaylistCursorAdapter;
import com.appinforium.newthinktankcodingtutorials.data.YoutubeDatabase;
import com.appinforium.newthinktankcodingtutorials.data.YoutubeProvider;


public class VideoListFragment extends ListFragment {

    private static final String DEBUG_TAG = "VideoListFragment";

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
                YoutubeDatabase.COL_TITLE, YoutubeDatabase.COL_THUMBNAIL_BITMAP,
                YoutubeDatabase.COL_DESCRIPTION };

        Log.d(DEBUG_TAG, "playlistId: " + getActivity().getIntent().getStringExtra(PlaylistsActivity.PLAYLIST_ID_MESSAGE));

        Uri content_uri = Uri.withAppendedPath(
                YoutubeProvider.VIDEOS_CONTENT_URI,
                getActivity().getIntent().getStringExtra(PlaylistsActivity.PLAYLIST_ID_MESSAGE)
        );

        Cursor cursor = getActivity().getContentResolver().query(content_uri, projection, null, null, null);

        PlaylistCursorAdapter adapter = new PlaylistCursorAdapter(getActivity(), cursor, true);
        setListAdapter(adapter);
    }

    public interface OnVideoSelectedListener {
        public void onVideoSelected(long id);
    }

}
