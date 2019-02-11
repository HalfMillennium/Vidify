package com.digitalnode.playsee;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import org.mortbay.jetty.Main;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import androidx.appcompat.app.AppCompatActivity;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.PlaylistTracksInformation;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "b3ebe9ba8e7643569391460329f57a40";
    private static final String REDIRECT_URI = "https://github.com/HalfMillennium/Vidify";
    private static final int REQUEST_CODE = 1337;

    private SpotifyAppRemote mSpotifyAppRemote;
    private ArrayList<String> playlistNames = new ArrayList<>();
    private ArrayList<Playlist> allPlaylists = new ArrayList<>();

    private ArrayList<PlaylistSimple> playlistSimples = new ArrayList<>();
    private String userId = null;

    private static Playlist selectedPlaylist;

    private SpotifyService generalService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        if(userIsSignedIn())
        {
            setContentView(R.layout.playlist_selection_layout);
        }
    }

    protected boolean userIsSignedIn()
    {
        return false;
    }

    public void getSignInPage(View v)
    {
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming", "user-read-private"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    setContentView(R.layout.playlist_selection_layout);
                    Toast.makeText(this, "You're signed in!", Toast.LENGTH_SHORT).show();

                    SpotifyApi api = new SpotifyApi();

                    api.setAccessToken(response.getAccessToken());

                    final SpotifyService spotify = api.getService();

                    generalService = spotify;
                    //doAsync();
                    new GetSpotifyInfo().execute();
                    //Log.d("user id", spotify.getMe().display_name);

                    spotify.getMyPlaylists(new Callback<Pager<PlaylistSimple>>() {
                        @Override
                        public void success(Pager<PlaylistSimple> playlistSimplePager, Response response) {
                            List<PlaylistSimple> playlists = playlistSimplePager.items;

                            for(PlaylistSimple list : playlists)
                            {
                                playlistNames.add(list.name);
                                playlistSimples.add(list);
                                Log.d("username", list.owner.display_name);
                                Log.d("code", list.external_urls.toString()
                                                    .substring(list.external_urls.toString().lastIndexOf("/")+1, list.external_urls.toString().lastIndexOf("}")));
                            }

                            setUpListView();
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });

                    updateUI();
                    break;

                // Auth flow returned an error
                case ERROR:
                    Toast.makeText(this, "Hm. An error occurred.", Toast.LENGTH_SHORT).show();
                    // SAE = Spotify Authentication Error
                    Log.d("SAE", response.getError());
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }

    private class GetSpotifyInfo extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            userId = generalService.getMe().id;
            return null;
        }

        protected void onPostExecute(Void aVoid) {
        }
    }

    public void updateUI()
    {

    }

    /****** SPOTIFY API ******/

    private void setUpListView()
    {
        final ListView playlists = findViewById(R.id.playlist_view);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.playlist_entry_layout, R.id.title, playlistNames);
        playlists.setAdapter(arrayAdapter);

        playlists.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                new GetSpotifyPlaylist().execute(position);
            }
        });
    }

    private class GetSpotifyPlaylist extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            Playlist u = generalService.getPlaylist(userId, playlistSimples.get(params[0]).external_urls.toString()
                    .substring(playlistSimples.get(params[0]).external_urls.toString().lastIndexOf("/")+1, playlistSimples.get(params[0]).external_urls.toString().lastIndexOf("}")));
            Log.d("Playlist", u.name + ", owned by " + userId);
            selectedPlaylist = u;
            Intent intent = VideoListView.makeIntent(MainActivity.this);
            startActivity(intent);
            return null;
        }

        protected void onPostExecute(Void aVoid) {
        }
    }

    public static Playlist getSelPlaylist()
    {
        return selectedPlaylist;
    }

    /****************************************/


}
