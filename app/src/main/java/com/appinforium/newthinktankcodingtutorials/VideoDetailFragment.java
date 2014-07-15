package com.appinforium.newthinktankcodingtutorials;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appinforium.newthinktankcodingtutorials.data.YoutubeDatabase;
import com.appinforium.newthinktankcodingtutorials.data.YoutubeProvider;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import java.util.List;

/**
 * Created by jeff on 7/13/14.
 */
public class VideoDetailFragment extends Fragment implements View.OnClickListener {

    private static final String DEBUG_TAG = "VideoDetailFragement";
    public static final String VIDEO_INDEX = "video_index";
    private static final int VIDEO_INDEX_NOT_SET = -1;
    private static final int REQ_START_STANDALONE_PLAYER = 1;
    private static final int REQ_RESOLVE_SERVICE_MISSING = 2;

    private String videoId = null;

    private TextView videoDescriptionTextView;
    private TextView videoTitleTextView;
    private Button watchVideoButton;



    public VideoDetailFragment() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_START_STANDALONE_PLAYER && resultCode != getActivity().RESULT_OK) {
            YouTubeInitializationResult errorReason =
                    YouTubeStandalonePlayer.getReturnedInitializationResult(data);
            if (errorReason.isUserRecoverableError()) {
                errorReason.getErrorDialog(getActivity(), 0).show();
            } else {
                String errorMessage =
                        String.format("There was an error initializing hte YouTube Player (%1$s)", errorReason.toString());
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewHierarchy = inflater.inflate(R.layout.fragment_video_detail, container, false);

        videoDescriptionTextView = (TextView) viewHierarchy.findViewById(R.id.descDescriptionTextView);
        videoTitleTextView = (TextView) viewHierarchy.findViewById(R.id.descTitleTextView);
        watchVideoButton = (Button) viewHierarchy.findViewById(R.id.watchVideoButton);

        watchVideoButton.setOnClickListener(this);

        Bundle args = getArguments();
        long videoIndex = args != null ? args.getLong(VIDEO_INDEX, VIDEO_INDEX_NOT_SET) : VIDEO_INDEX_NOT_SET;

        if (videoIndex != VIDEO_INDEX_NOT_SET) {
            setVideo(videoIndex);
        } else {
            // hide controls
            videoTitleTextView.setVisibility(View.INVISIBLE);
            videoDescriptionTextView.setVisibility(View.INVISIBLE);
            watchVideoButton.setVisibility(View.INVISIBLE);
        }

        return viewHierarchy;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    public void setVideo(long id) {

        String[] projection = {YoutubeDatabase.COL_TITLE, YoutubeDatabase.COL_DESCRIPTION,
                                YoutubeDatabase.COL_VIDEO_ID};

        Uri content_uri = Uri.withAppendedPath(YoutubeProvider.VIDEO_CONTENT_URI, String.valueOf(id));
        Cursor cursor = getActivity().getContentResolver().query(content_uri, projection, null, null, null);

        if (cursor.moveToFirst()) {
            videoDescriptionTextView.setText(cursor.getString(cursor.getColumnIndex(YoutubeDatabase.COL_DESCRIPTION)));
            videoTitleTextView.setText(cursor.getString(cursor.getColumnIndex(YoutubeDatabase.COL_TITLE)));
            getActivity().setTitle(cursor.getString(cursor.getColumnIndex(YoutubeDatabase.COL_TITLE)));
            videoId = cursor.getString(cursor.getColumnIndex(YoutubeDatabase.COL_VIDEO_ID));
        } else {
            videoTitleTextView.setText("Error loading details");
        }


        videoTitleTextView.setVisibility(View.VISIBLE);
        videoDescriptionTextView.setVisibility(View.VISIBLE);
        watchVideoButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;

        if (view == watchVideoButton) {
            intent = YouTubeStandalonePlayer.createVideoIntent(getActivity(),
                    getResources().getString(R.string.youtube_api_key), videoId, 0, true, false);

        }

        if (intent != null) {
            if (canResolveIntent(intent)) {
                startActivityForResult(intent, REQ_START_STANDALONE_PLAYER);
            } else {
                YouTubeInitializationResult.SERVICE_MISSING
                        .getErrorDialog(getActivity(), REQ_RESOLVE_SERVICE_MISSING).show();
            }
        }
    }

    private boolean canResolveIntent(Intent intent) {
        List<ResolveInfo> resolveInfo = getActivity().getPackageManager().queryIntentActivities(intent, 0);
        return resolveInfo != null && !resolveInfo.isEmpty();
    }

}
