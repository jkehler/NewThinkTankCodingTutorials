package com.appinforium.newthinktankcodingtutorials;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by jeff on 7/13/14.
 */
public class VideoDetailActivity extends ActionBarActivity {

    public static final String DEBUG_TAG = "VideoDetailActivity";
    public static final String ITEM_ID_MESSAGE = "item_id_message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_video_detail);

        Intent intent = this.getIntent();
        long itemId = intent.getLongExtra(ITEM_ID_MESSAGE, -1);

    }
}
