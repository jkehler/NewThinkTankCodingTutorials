package com.appinforium.newthinktankcodingtutorials.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appinforium.newthinktankcodingtutorials.R;
import com.appinforium.newthinktankcodingtutorials.data.YoutubeDatabase;

/**
 * Created by jeff on 7/11/14.
 */
public class PlaylistCursorAdapter extends CursorAdapter {

    LayoutInflater inflater;

    public PlaylistCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return inflater.inflate(R.layout.playlist_list_item, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView testTextView = (TextView) view.findViewById(R.id.testTextView);
        testTextView.setText(cursor.getString(cursor.getColumnIndex(YoutubeDatabase.COL_TITLE)));

        ImageView thumbnailImageView = (ImageView) view.findViewById(R.id.videoThumbnailImageView);
        byte[] thumbnailBytes = cursor.getBlob(cursor.getColumnIndex(YoutubeDatabase.COL_THUMBNAIL_BITMAP));
        if (thumbnailBytes == null) {
            thumbnailImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.no_thumbnail));
        }
    }
}
