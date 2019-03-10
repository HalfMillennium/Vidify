package com.digitalnode.playsee.BingSearchApi;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.net.*;
import java.util.*;
import java.io.*;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class BingSearch {

// ***********************************************
// *** Update or verify the following values. ***
// **********************************************

    // Replace the subscriptionKey string value with your valid subscription key.
    static String subscriptionKey = "b9c4b89f04b54b078ccbf6a863ed0b5d";

    // Verify the endpoint URI.  At this writing, only one endpoint is used for Bing
    // search APIs.  In the future, regional endpoints may be available.  If you
    // encounter unexpected authorization errors, double-check this value against
    // the endpoint for your Bing Web search instance in your Azure dashboard.
    static String host = "https://api.cognitive.microsoft.com";
    static String path = "/bing/v7.0/videos/search";

    static String searchTerm;

    private String videoId, runtime;
    private final String TAG = "BingSearch";

    public BingSearch(String term) {
        searchTerm = term.replaceAll("[^A-Za-z0-9%\\[\\]]", "");

        try {
            String complete = new GetSearch().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static SearchResults SearchVideos (String searchQuery) throws Exception {
        // construct URL of search request (endpoint + query string)
        URL url = new URL(host + path + "?q=" +  URLEncoder.encode(searchQuery, "UTF-8"));
        HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey);

        // receive JSON body
        InputStream stream = connection.getInputStream();
        String response = new Scanner(stream).useDelimiter("\\A").next();

        // construct result object for return
        SearchResults results = new SearchResults(new HashMap<>(), response);

        // extract Bing-related HTTP headers
        Map<String, List<String>> headers = connection.getHeaderFields();
        for (String header : headers.keySet()) {
            if (header == null) continue;      // may have null key
            if (header.startsWith("BingAPIs-") || header.startsWith("X-MSEdge-")) {
                results.relevantHeaders.put(header, headers.get(header).get(0));
            }
        }

        stream.close();
        return results;
    }

    // pretty-printer for JSON; uses GSON parser to parse and re-serialize
    public static String prettify(String json_text) {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(json_text).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(json);
    }

    private class GetSearch extends AsyncTask<Void, Void, String>
    {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                SearchResults result = SearchVideos(searchTerm);

                //Log.d("BING", "\nRelevant HTTP Headers:\n");
                for (String header : result.relevantHeaders.keySet())
                    Log.d("BING", header + ": " + result.relevantHeaders.get(header));

                //Log.d("BING", "\nJSON Response:\n");
                Log.d("BING", prettify(result.jsonResponse));

                Gson gson = new Gson();
                String jsonOutput = result.jsonResponse;
                Type listType = new TypeToken<VideoResults>(){}.getType();
                VideoResults results = gson.fromJson(jsonOutput, listType);
                videoId = results.getValue().get(0).getContentUrl();
                runtime = results.getValue().get(0).getDuration();
            }
            catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
            return "complete";
        }
    }

    public String getVideoId()
    {
        String id = videoId.substring(videoId.lastIndexOf("=")+1);
        Log.d("video id", id);
        return id;
    }

    public String getRuntime()
    {
        return runtime;
    }
}

// Container class for search results encapsulates relevant headers and JSON data
class SearchResults{
    HashMap<String, String> relevantHeaders;
    String jsonResponse;
    SearchResults(HashMap<String, String> headers, String json) {
        relevantHeaders = headers;
        jsonResponse = json;
    }
}