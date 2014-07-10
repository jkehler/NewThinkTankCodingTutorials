package com.appinforium.newthinktankcodingtutorials;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PlaylistsAdapter extends BaseAdapter implements
        ThumbnailLoaderTask.OnThumbnailLoadedListener {

    LayoutInflater inflater;
    List<ViewItem> items;
    final Handler handler;

    public PlaylistsAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        items = new ArrayList<ViewItem>();
        handler = new Handler();
    }

    public void setData(List<YoutubeAPI.PlaylistItem> data) {
        if (data != null) {
            for (YoutubeAPI.PlaylistItem item : data) {
                ViewItem viewItem = new ViewItem();
                viewItem.thumbnailUrl = item.getThumbnailUrl();
                viewItem.description = item.getDescription();
                viewItem.title = item.getTitle();
                viewItem.playlistId = item.getPlaylistId();
                items.add(viewItem);
            }

            //new ThumbnailLoaderTask(this).execute(data);
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
            convertView = inflater.inflate(R.layout.playlists_grid_item, null);
            thumbnailImageView = (ImageView) convertView.findViewById(R.id.playlistThumbnailImageView);
            item.thumbnailImageView = thumbnailImageView;
        } else {
            thumbnailImageView = (ImageView) convertView.findViewById(R.id.playlistThumbnailImageView);
            item.thumbnailImageView = thumbnailImageView;
        }

        TextView playlistTitleTextView = (TextView) convertView.findViewById(R.id.playlistTitleTextView);


        if (item.thumbnailBitmap != null) {
            thumbnailImageView.setImageBitmap(item.thumbnailBitmap);
        }

        playlistTitleTextView.setText(item.title);

        return convertView;

    }

    @Override
    public void onThumbnailLoaded(Bitmap thumbnailBitmap, int position) {
        final ViewItem item = getItem(position);
        if (thumbnailBitmap == null) {
            Log.d("bitmap", "is null");
        } else {

            item.thumbnailBitmap = thumbnailBitmap;

            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (item.thumbnailImageView != null) {
                        item.thumbnailImageView.setImageBitmap(item.thumbnailBitmap);
                    }
                }
            };

            handler.post(runnable);
        }
    }

    static class ViewItem {
        TextView playlistTitleTextView;
        ImageView thumbnailImageView;
        Bitmap thumbnailBitmap;
        String thumbnailUrl;
        String title;
        String description;
        String playlistId;
    }
}
