package com.appinforium.newthinktankcodingtutorials;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends BaseAdapter {

    LayoutInflater inflater;
    List<ViewItem> items;

    public PlaylistAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        items = new ArrayList<ViewItem>();
    }

    public void setData(List<YoutubeAPI.YoutubeItem> data) {
        if (data != null) {
            for (YoutubeAPI.YoutubeItem item : data) {
                ViewItem viewItem = new ViewItem();
                viewItem.thumbnailUrl = item.thumbnailUrl;
                viewItem.videoId = item.videoId;
                items.add(viewItem);
            }
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public ViewItem getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewItem item = getItem(position);
        ImageView thumbnailImageView;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.playlist_list_item, null);
            thumbnailImageView = (ImageView) convertView.findViewById(R.id.videoThumbnailImageView);
            item.thumbnailImageView = thumbnailImageView;
        } else {
            thumbnailImageView = (ImageView) convertView.findViewById(R.id.videoThumbnailImageView);
            item.thumbnailImageView = thumbnailImageView;
        }

        if (item.thumbnailBitmap != null) {
            thumbnailImageView.setImageBitmap(item.thumbnailBitmap);
        }

        TextView testTextView = (TextView) convertView.findViewById(R.id.testTextView);
        testTextView.setText(item.videoId);

        return convertView;
    }

    static class ViewItem {
        String title;
        String description;
        String videoId;
        String thumbnailUrl;
        ImageView thumbnailImageView;
        Bitmap thumbnailBitmap;
    }
}
