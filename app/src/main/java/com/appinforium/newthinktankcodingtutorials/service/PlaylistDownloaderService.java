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

public class PlaylistDownloaderService extends Service {

    private static final String DEBUG_TAG = "PlaylistDownloaderService";
    String playlistId;
    String strUrl;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            playlistId = intent.getStringExtra("playlist_id");

            strUrl = String.format(
                    "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=%s&playlistId=%s&key=%s",
                    getResources().getString(R.string.api_max_results),
                    playlistId, getResources().getString(R.string.youtube_api_key));

            //try {
            //    URL apiUrl = new URL(strUrl);
            YoutubeAPITask youtubeAPITask = new YoutubeAPITask();
            youtubeAPITask.execute(strUrl);
//        } catch (MalformedURLException e) {
//            Log.e(DEBUG_TAG, "Malformed URL: " + strUrl);
//        }

            return Service.START_FLAG_REDELIVERY;
        } catch (NullPointerException e) {
            Log.d(DEBUG_TAG, "NullPointerException", e);
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
//            thumbnailIntent.putExtra("playlist_id", playlistId);
//            startService(thumbnailIntent);
        }


        private boolean jsonParse(String strUrl) {
            boolean succeeded = true;
            boolean lastPage = true;
            String nextPageToken = null;
            URL apiUrl;

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

                            JSONObject snippet = item.getJSONObject("snippet");

                            String title = snippet.getString("title");
                            String description = snippet.getString("description");
                            String publishedAt = snippet.getString("publishedAt");

                            String playlistId = snippet.getString("playlistId");
                            String position = snippet.getString("position");

                            JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                            JSONObject thumbnail = thumbnails.getJSONObject("medium");

                            String thumbnailUrl = thumbnail.getString("url");

                            JSONObject resourceId = snippet.getJSONObject("resourceId");

                            String videoId = resourceId.getString("videoId");
                            Log.d(DEBUG_TAG, "videoId " + videoId);

                            ContentValues videoData = new ContentValues();

                            videoData.put(YoutubeDatabase.COL_TITLE, title);
                            videoData.put(YoutubeDatabase.COL_DESCRIPTION, description);
                            videoData.put(YoutubeDatabase.COL_PLAYLIST_ID, playlistId);
                            videoData.put(YoutubeDatabase.COL_VIDEO_ID, videoId);
                            videoData.put(YoutubeDatabase.COL_THUMBNAIL_URL, thumbnailUrl);
                            videoData.put(YoutubeDatabase.COL_PUBLISHED_AT, publishedAt);
                            videoData.put(YoutubeDatabase.COL_POSITION, Integer.valueOf(position));

                            Uri content_uri = Uri.withAppendedPath(YoutubeProvider.VIDEOS_CONTENT_URI, playlistId);
                            Log.d(DEBUG_TAG, "content_uri: " + content_uri);

                            getContentResolver().insert(content_uri, videoData);

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
