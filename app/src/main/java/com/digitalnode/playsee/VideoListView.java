package com.digitalnode.playsee;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AppComponentFactory;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import org.mortbay.jetty.Main;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.PlaylistTracksInformation;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;

public class VideoListView extends YouTubeBaseActivity {

    GoogleAccountCredential mCredential;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { YouTubeScopes.YOUTUBE_READONLY };

    protected ArrayList<String> songDesc = new ArrayList<>();
    private Playlist list;
    private ArrayList<String> songs = new ArrayList<>();
    private ArrayList<String> songsPres = new ArrayList<>();
    private ArrayList<String> videoIDs = new ArrayList<>();
    private ArrayList<String> runtimes = new ArrayList<>();

    // contains the index of the currently selected video in the list - obviously, default value is zero.
    private int currSel = 0;

    private OrientationEventListener mOrientationListener;
    private YouTubePlayerView mYouTubePlayerView, YouTubePlayerView;
    private YouTubePlayer.OnInitializedListener onInitializedListener;

    private ArrayList<String> videoTests2;

    private static final String TAG = "VideoListView";

    private final String DEBUG_TAG = "VideoListView";
    /**
     * Create the main activity.
     * @param savedInstanceState previously saved instance data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list_view);
        getUserPlaylist();

        //if(savedInstanceState == null)
          //  getUrls();

        Log.d("urls", videoIDs.toString());

        // will replace 'videoIds'
        String[] idArray = new String[videoIDs.size()];
        for(int i = 0; i < idArray.length; i++) { idArray[i] = videoIDs.get(i); }

        String[] testIDs = {"7nJRGARveVc", "w4LkSRXrK34", "_BGVa18vm7M", "J6dtXL_P2b8", "Mva_EluErSA", "OOrAxiPi2Zg"};
        ArrayList<String> listViewTest = new ArrayList<>();
        for(int i = 0; i < 6; i++)
        {
            listViewTest.add("A Song - Artist:2:30");
        }

        videoTests2 = new ArrayList<>();
        for(String t : testIDs)
            videoTests2.add(t);

        SetUpPlayer();

        SongListViewAdapter adapter = new SongListViewAdapter(listViewTest, this);

        ListView listView = findViewById(R.id.song_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Toast.makeText(VideoListView.this, songs.get(position), Toast.LENGTH_SHORT).show();
                currSel = position;
                SetUpPlayer();
            }
        });
    }

    public void getUserPlaylist()
    {
        list = MainActivity.getSelPlaylist();
        ArrayList<PlaylistTrack> allSongs = (ArrayList) list.tracks.items;
        for(PlaylistTrack track : allSongs)
        {
            songs.add(track.track.name.replaceAll(" ", "%20") + "--" + track.track.artists.get(0).name.replaceAll(" ", "%20"));
            songsPres.add(track.track.name + " - " + track.track.artists.get(0).name + ":");
        }
    }
    /*** Why am I doing this in two steps? Because I felt like it. ***/
    public void getUrls() {
        try {
            for (int i = 0; i < songs.size(); i++) {
                YoutubeSearch search = new YoutubeSearch(songs.get(i));
                videoIDs.add(search.getID());
                songsPres.set(i, songsPres.get(i) + search.getRuntime());
                Log.d("song", search.getID());
            }
        } catch (NullPointerException e) {
            Toast.makeText(this, "Whoops! That's our bad. Try again later.", Toast.LENGTH_SHORT).show();
            Log.d("null-pointer", "query quota likely exceeded");
        }
    }

    public static Intent makeIntent(Context context) { return new Intent(context, VideoListView.class); }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putStringArrayList("songsPres", songsPres);
        savedInstanceState.putStringArrayList("videoIDs", videoIDs);
        savedInstanceState.putInt("selected", currSel);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        songsPres = savedInstanceState.getStringArrayList("songsPres");
        videoIDs = savedInstanceState.getStringArrayList("videoIDs");
        currSel = savedInstanceState.getInt("selected");
    }

    public static void restartActivity(Activity activity){
        activity.finish();
        activity.startActivity(activity.getIntent());
    }


    /****** YOUTUBE PLAYER API STUFF *******/
    public void SetUpPlayer()
    {
        YouTubePlayerView = findViewById(R.id.youtube_player);
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG,"YTPlayer: Done initializing.");

                youTubePlayer.loadVideos(videoTests2, currSel, 0);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG,"YTPlayer: Failed to initialize.");
            }
        };

        YouTubePlayerView.initialize(YouTubeConfig.getApiKey(), onInitializedListener);
    }
    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
}
