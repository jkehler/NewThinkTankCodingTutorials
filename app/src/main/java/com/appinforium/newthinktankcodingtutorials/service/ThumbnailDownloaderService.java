package com.appinforium.newthinktankcodingtutorials.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.appinforium.newthinktankcodingtutorials.data.YoutubeDatabase;
import com.appinforium.newthinktankcodingtutorials.data.YoutubeProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by jeff on 7/12/14.
 */
public class ThumbnailDownloaderService extends Service {

    private static final String DEBUG_TAG = "ThumbnailDownloaderService";
    private String playlistId;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        playlistId = intent.getStringExtra("playlist_id");

        ImageDownloaderTask task = new ImageDownloaderTask();
        task.execute();

        return Service.START_FLAG_REDELIVERY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class ImageDownloaderTask extends AsyncTask<Void, Void, Boolean> {

        private static final String DEBUG_TAG = "ImageDownloaderTask";

        @Override
        protected Boolean doInBackground(Void... items) {

            if (playlistId != null) {
                Uri content_uri = Uri.withAppendedPath(YoutubeProvider.THUMBNAILS_CONTENT_URI, playlistId);
                Log.d(DEBUG_TAG, "content_uri: " + String.valueOf(content_uri));

                String[] projection = {YoutubeDatabase.ID, YoutubeDatabase.COL_THUMBNAIL_URL};
                Cursor cursor = getContentResolver().query(content_uri, projection, null, null, null);

                Log.d(DEBUG_TAG, "item count: " + String.valueOf(cursor.getCount()));

                while (cursor.moveToNext()) {
                    String thumbnailUrl = cursor.getString(cursor.getColumnIndex(YoutubeDatabase.COL_THUMBNAIL_URL));
                    int id = cursor.getInt(cursor.getColumnIndex(YoutubeDatabase.ID));
                    Log.d(DEBUG_TAG, "Downloading " + thumbnailUrl);
                    Bitmap bitmap = downloadImage(thumbnailUrl);
                    if (bitmap != null) {
                        ContentValues values = new ContentValues();

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] thumbnail = baos.toByteArray();
                        values.put(YoutubeDatabase.COL_THUMBNAIL_BITMAP, thumbnail);

                        Uri update_content_uri = Uri.withAppendedPath(YoutubeProvider.VIDEOS_CONTENT_URI, playlistId + "/" + id);
                        getContentResolver().update(update_content_uri, values, null, null);
                    }
                }
            }
            return null;
        }

    }

    static Bitmap downloadImage(String strUrl) {

        URL url = null;
        InputStream inputStream = null;

        try {
            url = new URL(strUrl);
            URLConnection connection = null;
            connection = url.openConnection();
            connection.setUseCaches(true);
            if (connection.getContentType().contentEquals("image/jpeg")) {
                inputStream = (InputStream) connection.getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                return bitmap;
            }

            return null;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
