package com.digitalnode.playsee;

import androidx.appcompat.app.AppCompatActivity;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class SongSelectionActivity extends AppCompatActivity {

    private RelativeLayout parentLayout;
    private Playlist playlist;
    private ArrayList<SongInfo> songInfos;
    private ListView listView;
    private final int MAX_CHECKED = 20;
    private int num_checked = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_selection);

        parentLayout = findViewById(R.id.parent_layout);
        playlist = MainActivity.getSelPlaylist();
        songInfos = new ArrayList<>();

        for(PlaylistTrack track : playlist.tracks.items)
        {
            SongInfo song = new SongInfo(track.track.name, track.track.artists.get(0).name, track.track.album.name);
            songInfos.add(song);
        }
        // ^^ pass as a list of 3 lists --> name = vals.get(0).get(i), artist = vals.get(1).get(i), etc.

        SongSelectAdapter adapter = new SongSelectAdapter(songInfos, this);

        listView = findViewById(R.id.song_selection);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox cb = view.findViewById(R.id.checkBox);
                if(num_checked == MAX_CHECKED)
                {
                    Toast.makeText(SongSelectionActivity.this, "You can't select more than 20 songs!", Toast.LENGTH_SHORT).show();
                } else if(cb.isChecked()){
                    num_checked++;
                } else {
                    num_checked--;
                }
            }
        });

        Snackbar snackbar = Snackbar
                .make(parentLayout, "It looks like you selected a playlist with more than 20 songs.", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void confirmSelection(View view)
    {
        Intent intent = VideoListView.makeIntent(SongSelectionActivity.this);
        startActivity(intent);
    }

    public static Intent makeIntent(Context context) { return new Intent(context, SongSelectionActivity.class); }
}
