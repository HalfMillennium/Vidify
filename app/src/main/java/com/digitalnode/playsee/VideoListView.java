package com.digitalnode.playsee;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalnode.playsee.BingSearchApi.BingSearch;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.youtube.YouTubeScopes;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistTrack;

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
    // songs = list of song names from Spotify; songsPres = presentable version of 'songs', that includes runtimes;
    // videoIDs = ids of video results from YoutubeSearch.java
    private ArrayList<String> songs = new ArrayList<>();
    private ArrayList<String> songsPres = new ArrayList<>();
    private ArrayList<String> videoIDs = new ArrayList<>();

    // contains the index of the currently selected video in the list - obviously, default value is zero.
    private int currSel = 0;

    private ListView listView;
    private YouTubePlayerView mYouTubePlayerView, YouTubePlayerView;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    private YouTubePlayer player;

    private ArrayList<String> videoTests2;

    private static final String TAG = "VideoListView";

    private String currently_playing = null;

    private ArrayList<String> debugPlaylist;

    /**
     * Create the main activity.
     * @param savedInstanceState previously saved instance data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list_view);
        getUserPlaylist();
        getUrls();

        Log.d("urls", videoIDs.toString());

        String[] testIDs = {"7nJRGARveVc", "w4LkSRXrK34", "tm6x0q2INSs", "1ykDNxDl7Zs", "ApHM1ct4tdM", "S-0TYeg9Rzc", "3qVPNONdF58"};

        videoTests2 = new ArrayList<>();
        for(String t : testIDs) { videoTests2.add(t); }

        debugPlaylist = videoIDs;

        ArrayList<String> songsPresTest = new ArrayList<>();
        songsPresTest.add("Casio - Jungle:PT4M13S");
        songsPresTest.add("Heavy, California - Jungle:PT4M13S");
        songsPresTest.add("Not Enough - Benny Sings:PT4M13S");
        songsPresTest.add("Feedback Delicates - Vinyl Williams:PT4M13S");
        songsPresTest.add("Golden Years - David Bowie:PT4M13S");
        songsPresTest.add("Feel It All Around - Washed Out:PT4M13S");
        songsPresTest.add("No Rain - Blind Melon:PT4M13S");

        setUpPlayer();

        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();

        SongListViewAdapter adapter = new SongListViewAdapter(songsPres, this);

        listView = findViewById(R.id.song_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Toast.makeText(VideoListView.this, songs.get(position), Toast.LENGTH_SHORT).show();
                currSel = position;
                player.cueVideo(debugPlaylist.get(position));
            }
        });
    }

    public void getUserPlaylist()
    {
        ArrayList<PlaylistTrack> allSongs = MainActivity.allTracks;
        for(PlaylistTrack track : allSongs)
        {
            songs.add(track.track.name.replaceAll(" ", "%20") + "--" + track.track.artists.get(0).name.replaceAll(" ", "%20"));
            songsPres.add(track.track.name + " - " + track.track.artists.get(0).name + ":");
            Log.d(track.track.name, track.track.artists.get(0).name);
        }
    }
    /*** Why am I doing this in two steps? Because I felt like it. ***/
    public void getUrls() {
        try {
            Toast.makeText(this, "# Songs: " + songs.size(), Toast.LENGTH_SHORT).show();
            for (int i = 0; i < songs.size(); i++) {
                /*
                YoutubeSearch search = new YoutubeSearch(songs.get(i));
                videoIDs.add(search.getID());
                songsPres.set(i, songsPres.get(i) + search.getRuntime());
                Log.d("song", search.getID());
                */

                BingSearch search = new BingSearch(songs.get(i));
                Log.d(TAG, search.getRuntime());

                videoIDs.add(search.getVideoId());
                songsPres.set(i, songsPres.get(i) + search.getRuntime());
                Log.d(TAG, search.getVideoId());
            }
        } catch (NullPointerException e) {
            Toast.makeText(this, "Whoops! That's our bad. Try again later.", Toast.LENGTH_SHORT).show();
            Log.d("null-pointer", "query quota likely exceeded");
        }
    }

    public static Intent makeIntent(Context context) { return new Intent(context, VideoListView.class); }

    /****** YOUTUBE PLAYER API *******/
    public void setUpPlayer()
    {
        YouTubePlayerView = findViewById(R.id.youtube_player);
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG,"YTPlayer: Done initializing.");
                player = youTubePlayer;
                youTubePlayer.loadVideos(debugPlaylist);
                player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                    @Override
                    public void onLoading() {
                        //Toast.makeText(VideoListView.this, "Video Loading...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLoaded(String s) {
                        currently_playing = s;
                        //Toast.makeText(VideoListView.this, "Video Loaded.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdStarted() {

                    }

                    @Override
                    public void onVideoStarted() {
                        //Toast.makeText(VideoListView.this, "Video Started!", Toast.LENGTH_SHORT).show();
                        for(int i = 0; i < debugPlaylist.size(); i++)
                        {
                            View item = getViewByPosition(i, listView);
                            TextView title = item.findViewById(R.id.title);
                            TextView runtime = item.findViewById(R.id.runtime);

                            if(debugPlaylist.get(i).equals(currently_playing))
                            {
                                //Log.d("playing: ", debugPlaylist.get(i));
                                title.setTextColor(getResources().getColor(R.color.lighterPurple));
                                runtime.setTextColor(getResources().getColor(R.color.lighterPurple));
                            } else {
                                title.setTextColor(getResources().getColor(R.color.normGray));
                                runtime.setTextColor(getResources().getColor(R.color.dimGray));
                            }

                        }

                    }

                    @Override
                    public void onVideoEnded() {

                    }

                    @Override
                    public void onError(YouTubePlayer.ErrorReason errorReason) {

                    }
                });
                //player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG,"YTPlayer: Failed to initialize.");
            }
        };

        YouTubePlayerView.initialize(YouTubeConfig.getApiKey(), onInitializedListener);
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            //Toast.makeText(this, "position  `: "+pos, Toast.LENGTH_SHORT).show();
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            //Toast.makeText(this, "childIndex: "+childIndex, Toast.LENGTH_SHORT).show();
            return listView.getChildAt(childIndex);
        }
    }

    private class SearchBing extends AsyncTask<String, Void, BingSearch>
    {
        @Override
        protected BingSearch doInBackground(String... params) {
            return new BingSearch(params[0]);
        }
    }
}
