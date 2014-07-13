package com.appinforium.newthinktankcodingtutorials;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jeff on 7/13/14.
 */
public class VideoDetailFragment extends Fragment {

    private static final String DEBUG_TAG = "VideoDetailFragement";

    public VideoDetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_video_detail, container, false);

    }


}
