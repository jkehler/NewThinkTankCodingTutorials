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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeff on 7/8/14.
 */

public class YoutubeAPI {

    String developerKey;

    public YoutubeAPI(String developerKey) {
        this.developerKey = developerKey;
    }


    public class PlaylistVideoItem {

    }

    public class PlaylistItem {
        private String playlistId;
        private String title;
        private String description;
        private String thumbnailUrl;
        private int thumbnailWidth;
        private int thumbnailHeight;
        private Bitmap thumbnailBitmap;
        private ImageView thumbnailImageView;

        public String getPlaylistId() {
            return playlistId;
        }

        public void setPlaylistId(String playlistId) {
            this.playlistId = playlistId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getThumbnailWidth() {
            return thumbnailWidth;
        }

        public void setThumbnailWidth(int thumbnailWidth) {
            this.thumbnailWidth = thumbnailWidth;
        }

        public int getThumbnailHeight() {
            return thumbnailHeight;
        }

        public void setThumbnailHeight(int thumbnailHeight) {
            this.thumbnailHeight = thumbnailHeight;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }

        public Bitmap getThumbnailBitmap() {
            return thumbnailBitmap;
        }

        public void setThumbnailBitmap(Bitmap thumbnailBitmap) {
            this.thumbnailBitmap = thumbnailBitmap;
        }

        public ImageView getThumbnailImageView() {
            return thumbnailImageView;
        }

        public void setThumbnailImageView(ImageView thumbnailImageView) {
            this.thumbnailImageView = thumbnailImageView;
        }
    }

    public void getPlaylistItems(String playlistId, int maxResults) {

    }

    public List<PlaylistItem> getChannelPlaylists(String channelId, int maxResults) {
        String strUrl = String.format("https://www.googleapis.com/youtube/v3/playlists?part=snippet&channelId=%s&maxResults=%d&key=%s",
                channelId, maxResults, this.developerKey);

        List<PlaylistItem> playlistItems = new ArrayList<PlaylistItem>();

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

                    PlaylistItem playlistItem = new PlaylistItem();

                    playlistItem.setPlaylistId(item.getString("id"));

                    JSONObject snippet = item.getJSONObject("snippet");

                    playlistItem.setTitle(snippet.getString("title"));
                    playlistItem.setDescription(snippet.getString("description"));

                    JSONObject thumbnails = snippet.getJSONObject("thumbnails");

                    JSONObject thumbnail = thumbnails.getJSONObject("medium");

                    playlistItem.setThumbnailUrl(thumbnail.getString("url"));
                    playlistItem.setThumbnailHeight(Integer.valueOf(thumbnail.getString("height")));
                    playlistItem.setThumbnailWidth(Integer.valueOf(thumbnail.getString("width")));

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
