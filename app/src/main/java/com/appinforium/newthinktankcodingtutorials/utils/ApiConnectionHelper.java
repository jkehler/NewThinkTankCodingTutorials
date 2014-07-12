package com.appinforium.newthinktankcodingtutorials.utils;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * Created by jeff on 7/11/14.
 */
public class ApiConnectionHelper {

    private static final String DEBUG_TAG = "ApiConnectionHelper";

    public static JSONObject getJsonResponse(URL apiUrl) {
        String response = null;

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            HttpGet httpGet = new HttpGet(String.valueOf(apiUrl));
            httpResponse = httpClient.execute(httpGet);

            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);

            return new JSONObject(response);

        } catch (ClientProtocolException e) {
            Log.w(DEBUG_TAG, "ClientProtocolException", e);
        } catch (IOException e) {
            Log.w(DEBUG_TAG, "IOException", e);
        } catch (JSONException e) {
            Log.w(DEBUG_TAG, "JSONException", e);
        }

        return null;
    }
}
