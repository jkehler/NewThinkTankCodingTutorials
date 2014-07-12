package com.appinforium.newthinktankcodingtutorials;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.appinforium.newthinktankcodingtutorials.adapter.PlaylistCursorAdapter;
import com.appinforium.newthinktankcodingtutorials.data.YoutubeDatabase;
import com.appinforium.newthinktankcodingtutorials.data.YoutubeProvider;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VideoListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VideoListFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class VideoListFragment extends Fragment {

    private static final String DEBUG_TAG = "VideoListFragment";
    PlaylistCursorAdapter adapter;

//    PlaylistAdapter playlistAdapter;
//    private OnFragmentInteractionListener mListener;

//    public static VideoListFragment newInstance(String param1, String param2) {
//        VideoListFragment fragment = new VideoListFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
    public VideoListFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewHierarchy = inflater.inflate(R.layout.fragment_video_list, container, false);

        String[] projection = { YoutubeDatabase.ID, YoutubeDatabase.COL_VIDEO_ID, YoutubeDatabase.COL_TITLE, YoutubeDatabase.COL_THUMBNAIL_BITMAP };

        Log.d(DEBUG_TAG, "intent videoId: " + getActivity().getIntent().getStringExtra(PlaylistsActivity.PLAYLIST_ID_MESSAGE));

        Uri content_uri = Uri.withAppendedPath(
                YoutubeProvider.VIDEOS_CONTENT_URI,
                getActivity().getIntent().getStringExtra(PlaylistsActivity.PLAYLIST_ID_MESSAGE)
        );

        Log.d(DEBUG_TAG, "content_uri: " + content_uri);
        Cursor cursor = getActivity().getContentResolver().query(content_uri, projection, null, null, null);

        Log.d(DEBUG_TAG, "item count: " + String.valueOf(cursor.getCount()));

//                Uri.withAppendedPath(YoutubeProvider.VIDEOS_CONTENT_URI,
//                        getActivity().getIntent().getStringExtra(PlaylistsActivity.PLAYLIST_ID_MESSAGE)),
//                projection, null, null, null);
//
        adapter = new PlaylistCursorAdapter(getActivity(), cursor, true);
        ListView videoListView = (ListView) viewHierarchy.findViewById(R.id.videoListView);
        videoListView.setAdapter(adapter);
//        playlistAdapter = new PlaylistAdapter(getActivity());
//        videoListView.setAdapter(playlistAdapter);
        return viewHierarchy;

    }

//    public void updateVideoList(List<YoutubeAPI.YoutubeItem> items) {
//        //playlistAdapter.setData(items);
//    }


    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        public void onFragmentInteraction(Uri uri);
//    }

}
