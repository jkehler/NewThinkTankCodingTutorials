package com.appinforium.newthinktankcodingtutorials;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends BaseAdapter {

    LayoutInflater inflater;
    List<ViewItem> items;

    public PlaylistAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        items = new ArrayList<ViewItem>();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //ViewItem item = getItem(position);

        return convertView;
    }

    static class ViewItem {
        String title;
        String description;
        String videoId;
        String thumbnailUrl;
    }
}
