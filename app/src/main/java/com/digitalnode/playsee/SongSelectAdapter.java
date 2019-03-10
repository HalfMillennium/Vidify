package com.digitalnode.playsee;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.mortbay.jetty.Main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SongSelectAdapter extends ArrayAdapter<SongInfo> {

    private ArrayList<SongInfo> dataSet;
    private boolean[] checks;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView songName;
        TextView artistAlbum;
        CheckBox checkBox;
    }

    public SongSelectAdapter(ArrayList<SongInfo> data, Context context) {
        super(context, R.layout.song_list_layout, data);
        this.dataSet = data;
        this.mContext=context;

        checks = new boolean[data.size()];
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SongInfo song = dataSet.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.song_list_layout, parent, false);
            viewHolder.songName =  convertView.findViewById(R.id.title);
            viewHolder.artistAlbum = convertView.findViewById(R.id.artist_album);
            viewHolder.checkBox = convertView.findViewById(R.id.checkBox);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        lastPosition = position;

        viewHolder.songName.setText(song.getName());
        viewHolder.artistAlbum.setText(song.getArtist() + " | " + song.getAlbum());
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checks[position] = !checks[position];
                if (!isChecked) {
                    checks[position] = false;
                    MainActivity.allTracks.remove(position);
                } else {
                    checks[position] = true;
                    MainActivity.allTracks.add(position, MainActivity.trackConstants.get(position));
                }
            }
        });
        viewHolder.checkBox.setChecked(checks[position]);

        // Return the completed view to render on screen
        return convertView;
    }
}