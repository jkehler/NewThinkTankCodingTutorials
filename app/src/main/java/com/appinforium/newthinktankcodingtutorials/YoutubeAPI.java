package com.appinforium.newthinktankcodingtutorials;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.ImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jeff on 7/8/14.
 */

public class YoutubeAPI {

    String developerKey;

    public YoutubeAPI(String developerKey) {
        this.developerKey = developerKey;
    }


    static class YoutubeItem {
        String playlistId;
        String videoId;
        String title;
        String description;
        String thumbnailUrl;
        String publishedAt;
        int thumbnailWidth;
        int thumbnailHeight;
    }

    public List<YoutubeItem> getPlaylistItems(String playlistId, int maxResults) {
        String strUrl = String.format("https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=%d&playlistId=%s&key=%s",
                maxResults, playlistId, this.developerKey);

        List<YoutubeItem> videoItems = new ArrayList<YoutubeItem>();

        String jsonStr = makeApiConnection(strUrl);

        if (jsonStr != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);

                JSONObject pageInfo = jsonObject.getJSONObject("pageInfo");
                String totalResults = pageInfo.getString("totalResults");
                String resultsPerPage = pageInfo.getString("resultsPerPage");

                String nextPageToken;
                try {
                    nextPageToken = jsonObject.getString("nextPageToken");
                } catch (JSONException e) {
                    nextPageToken = null;
                }

                JSONArray items = null;
                items = jsonObject.getJSONArray("items");

                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);

                    YoutubeItem videoItem = new YoutubeItem();

                    JSONObject snippet = item.getJSONObject("snippet");

                    videoItem.title = snippet.getString("title");
                    videoItem.description = snippet.getString("description");
                    videoItem.publishedAt = snippet.getString("publishedAt");
                    videoItem.playlistId = snippet.getString("playlistId");

                    JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                    JSONObject thumbnail = thumbnails.getJSONObject("medium");

                    videoItem.thumbnailUrl = thumbnail.getString("url");
                    videoItem.thumbnailHeight = Integer.valueOf(thumbnail.getString("height"));
                    videoItem.thumbnailWidth = Integer.valueOf(thumbnail.getString("width"));

                    JSONObject resourceId = snippet.getJSONObject("resourceId");

                    videoItem.videoId = resourceId.getString("videoId");

                    videoItems.add(videoItem);
                }

            } catch (JSONException e) {
                 e.printStackTrace();
                Log.e("JSONException", e.toString());
            }
        }

        return videoItems;
    }

    public List<YoutubeItem> getChannelPlaylists(String channelId, int maxResults) {
        String strUrl = String.format("https://www.googleapis.com/youtube/v3/playlists?part=snippet&channelId=%s&maxResults=%d&key=%s",
                channelId, maxResults, this.developerKey);

        List<YoutubeItem> playlistItems = new ArrayList<YoutubeItem>();

        String jsonStr = makeApiConnection(strUrl);

        if (jsonStr != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);

                JSONObject pageInfo = jsonObject.getJSONObject("pageInfo");
                String totalResults = pageInfo.getString("totalResults");
                String resultsPerPage = pageInfo.getString("resultsPerPage");

                String nextPageToken;
                try {
                    nextPageToken = jsonObject.getString("nextPageToken");
                } catch (JSONException e) {
                    nextPageToken = null;
                }

                JSONArray items = null;
                items = jsonObject.getJSONArray("items");

                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);

                    YoutubeItem playlistItem = new YoutubeItem();

                    playlistItem.playlistId = item.getString("id");

                    JSONObject snippet = item.getJSONObject("snippet");

                    playlistItem.title = snippet.getString("title");
                    playlistItem.description = snippet.getString("description");

                    JSONObject thumbnails = snippet.getJSONObject("thumbnails");

                    JSONObject thumbnail = thumbnails.getJSONObject("medium");

                    playlistItem.thumbnailUrl = thumbnail.getString("url");
                    playlistItem.thumbnailHeight = Integer.valueOf(thumbnail.getString("height"));
                    playlistItem.thumbnailWidth = Integer.valueOf(thumbnail.getString("width"));

                    playlistItems.add(playlistItem);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return playlistItems;


    }

    private String makeApiConnection(String apiUrl) {

        String response = "";

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            HttpGet httpGet = new HttpGet(apiUrl);
            httpResponse = httpClient.execute(httpGet);

            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}
