package com.appinforium.newthinktankcodingtutorials.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
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

public class PlaylistUpdaterIntentService extends IntentService {

    private static final String DEBUG_TAG = "PlaylistUpdaterIntentService";
    public static final String PLAYLIST_ID = "playlist_id_msg";

    public PlaylistUpdaterIntentService() {
        super(DEBUG_TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String playlistId = intent.getStringExtra(PLAYLIST_ID);

        Log.d(DEBUG_TAG, "starting playlistId: " + playlistId);
        if (playlistId != null) {
            String strUrl = String.format(
                    "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=%s&playlistId=%s&key=%s",
                    getResources().getString(R.string.api_max_results),
                    playlistId, getResources().getString(R.string.youtube_api_key));

            if (!processRequest(strUrl)) {
                Log.e(DEBUG_TAG, "processRequest failed.");
            }
        }
    }

    private boolean processRequest(String strUrl) {
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
