package com.digitalnode.playsee;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.Video;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * Print a list of videos matching a search term.
 *
 * @author Jeremy Walker
 */
public class YoutubeSearch {

    private String keyword;
    //private String tempStr = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=casio%20jungle&type=video&key=AIzaSyBIuSNO32yI2s7xoPb3fAhm9qRfWzjzhYM";
    private final String API_KEY = "AIzaSyBZi7BjEILO34yKgyKbX4IGZ_AZuz0y2m8";

    private String id = null;
    private String runtime = null;

    public static boolean ERROR_STATE = false;

    public YoutubeSearch(String keyword)
    {
        ERROR_STATE = false;
        String n = keyword.replaceAll(" ", "%20");
        this.keyword = n.replace("--", " ");

        Log.d("UPDATED KEY", this.keyword);

        /*
        try {
            id = new AccessUrls().execute(this.keyword).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            Log.d("id-get-inter-exptn", e.getMessage());
        }*/
    }

    private class AccessUrls extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String sURL = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=" + params[0].replaceAll("[^A-Za-z0-9%\\[\\]]", "") + "&type=video&key=" + API_KEY + "&maxResults=1";
            String var = null;

            Log.d("here1", sURL);
            try {
                // Connect to the URL using java's native library
                URL url = new URL(sURL);
                URLConnection request = url.openConnection();
                request.connect();

                // Convert to a JSON object to print data
                JsonParser jp = new JsonParser(); //from gson
                JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
                Log.d("rootJSON", root.toString());
                Log.d("urlJSON", url.toString());
                JsonObject video = root.getAsJsonObject().get("items").getAsJsonArray().get(0).getAsJsonObject();
                var = video.get("id").getAsJsonObject().get("videoId").getAsString();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("Error parsing url", e.getMessage());
                ERROR_STATE = true;
            }

            Log.d("here", "here2");
            return var;
        }
    }

    private class GetVideoDuration extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String sURL = "https://www.googleapis.com/youtube/v3/videos?id=" + params[0] + "&part=contentDetails&key=" + API_KEY;
            String var = null;

            try {
                // Connect to the URL using java's native library
                URL url = new URL(sURL);
                URLConnection request = url.openConnection();
                request.connect();

                // Convert to a JSON object to print data
                JsonParser jp = new JsonParser(); //from gson
                JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
                Log.d("rootJSON", root.toString());
                Log.d("urlJSON", url.toString());
                JsonObject video = null;
                try {
                    video = root.getAsJsonObject().get("items").getAsJsonArray().get(0).getAsJsonObject().get("contentDetails").getAsJsonObject();
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    Log.e("Chief", "It looks like we've exceeded our quota for the day :(");
                }
                var = video.get("duration").getAsString();
            } catch (IOException e) {
                e.printStackTrace();
                // contDets = contentDetails
                Log.d("Error parsing contDets", e.getMessage());
                ERROR_STATE = true;
            }

            return var;
        }
    }

    public String getID()
    {
        String val = null;
        try {
            val = new AccessUrls().execute(this.keyword).get();
            runtime = new GetVideoDuration().execute(val).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            Log.d("id-get-inter-exptn", e.getMessage());
        }
        return val;
    }

    public String getRuntime()
    {
        return runtime;
    }
}