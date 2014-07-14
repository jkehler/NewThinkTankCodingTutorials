package com.appinforium.newthinktankcodingtutorials.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.DebugUtils;
import android.util.Log;

import com.appinforium.newthinktankcodingtutorials.R;
import com.appinforium.newthinktankcodingtutorials.data.YoutubeDatabase;
import com.appinforium.newthinktankcodingtutorials.data.YoutubeProvider;
import com.appinforium.newthinktankcodingtutorials.utils.ApiConnectionHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by jeff on 7/11/14.
 */

public class PlaylistsDownloaderService extends Service {

    private static final String DEBUG_TAG = "PlaylistsDownloaderService";
    String channelId;
    String strUrl;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            channelId = intent.getStringExtra("channel_id");

            strUrl = String.format(
                    "https://www.googleapis.com/youtube/v3/playlists?part=snippet&maxResults=%s&channelId=%s&key=%s",
                    getResources().getString(R.string.api_max_results),
                    channelId, getResources().getString(R.string.youtube_api_key));

            YoutubeAPITask youtubeAPITask = new YoutubeAPITask();
            youtubeAPITask.execute(strUrl);

            return Service.START_FLAG_REDELIVERY;
        } catch (NullPointerException e) {
            Log.e(DEBUG_TAG, "NullPointerException", e);
        }

        return 0;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class YoutubeAPITask extends AsyncTask<String, Void, Boolean> {

        private static final String DEBUG_TAG = "YoutubeAPITask";

        @Override
        protected Boolean doInBackground(String... urls) {
            boolean succeeded = false;
            String url = urls[0];

            if (url != null) {
                succeeded = jsonParse(url);
            }
            return succeeded;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
//            Intent thumbnailIntent = new Intent(getApplicationContext(), ThumbnailDownloaderService.class);
//            thumbnailIntent.putExtra("channel_id", channelId);
//            startService(thumbnailIntent);
        }


        private boolean jsonParse(String strUrl) {
            boolean succeeded = true;
            boolean lastPage = true;
            String nextPageToken = null;
            URL apiUrl;

            String[] allowed_playlist_ids = getResources().getStringArray(R.array.allowed_playlist_ids);


            do {

                try {

                    if (nextPageToken != null) {
                        String tmpUrl = strUrl + "&pageToken=" + nextPageToken;
                        apiUrl = new URL(tmpUrl);
                    } else {
                        apiUrl = new URL(strUrl);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    break;
                }

                JSONObject jsonObject = ApiConnectionHelper.getJsonResponse(apiUrl);

                if (jsonObject != null) {
                    try {

                        try {
                            nextPageToken = jsonObject.getString("nextPageToken");
                            lastPage = false;
                        } catch (JSONException e) {
                            nextPageToken = null;
                            lastPage = true;
                        }

                        JSONArray items = null;
                        items = jsonObject.getJSONArray("items");

                        for (int i = 0; i < items.length(); i++) {
                            JSONObject item = items.getJSONObject(i);

                            String playlistId = item.getString("id");



                            JSONObject snippet = item.getJSONObject("snippet");

                            String title = snippet.getString("title");
                            String description = snippet.getString("description");
                            String publishedAt = snippet.getString("publishedAt");

                            JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                            JSONObject thumbnail = thumbnails.getJSONObject("medium");

                            String thumbnailUrl = thumbnail.getString("url");

                            ContentValues contentValues = new ContentValues();

                            contentValues.put(YoutubeDatabase.COL_TITLE, title);
                            contentValues.put(YoutubeDatabase.COL_DESCRIPTION, description);
                            contentValues.put(YoutubeDatabase.COL_PLAYLIST_ID, playlistId);
                            contentValues.put(YoutubeDatabase.COL_THUMBNAIL_URL, thumbnailUrl);
                            contentValues.put(YoutubeDatabase.COL_PUBLISHED_AT, publishedAt);

                            for (String playlist_id : allowed_playlist_ids) {
                                if (playlist_id.equals(playlistId)) {
                                    Log.d(DEBUG_TAG, "allowed playlist_id: " + playlistId);
                                    getContentResolver().insert(YoutubeProvider.PLAYLISTS_CONTENT_URI,
                                            contentValues);
                                }
                            }
//                            Uri content_uri = Uri.withAppendedPath(YoutubeProvider.PLAYLISTS_CONTENT_URI, playlistId);
//                            Log.d(DEBUG_TAG, "content_uri: " + content_uri);

//                            getContentResolver().insert(YoutubeProvider.PLAYLISTS_CONTENT_URI, contentValues);

                        }

                    } catch (JSONException e) {
                        Log.e(DEBUG_TAG, "JSONException", e);
                        succeeded = false;
                    }

                }
            } while (!lastPage);

            return succeeded;
        }
    }
}