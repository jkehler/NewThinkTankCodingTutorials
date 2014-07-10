package com.appinforium.newthinktankcodingtutorials;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class ThumbnailLoaderTask extends AsyncTask<List<YoutubeAPI.PlaylistItem>, Void, Void> {

    private OnThumbnailLoadedListener listener;

    public interface OnThumbnailLoadedListener {

        void onThumbnailLoaded(Bitmap thumbnailBitmap, int position);

    }

    public ThumbnailLoaderTask(OnThumbnailLoadedListener listener) {
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(List<YoutubeAPI.PlaylistItem>... lists) {
        List<YoutubeAPI.PlaylistItem> items = lists[0];
        for (int i = 0; i < items.size(); i++) {
            String url = items.get(i).getThumbnailUrl();
            Bitmap bitmap = downloadImage(url);
            listener.onThumbnailLoaded(bitmap, i);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

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
