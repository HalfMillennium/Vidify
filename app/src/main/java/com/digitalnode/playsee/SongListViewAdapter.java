package com.digitalnode.playsee;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;

public class SongListViewAdapter extends ArrayAdapter<String> implements View.OnClickListener{

    private ArrayList<String> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtTitle;
        TextView txtRuntime;
    }

    public SongListViewAdapter(ArrayList<String> data, Context context) {
        super(context, R.layout.video_title_list_layout, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public void onClick(View v) {
        // set up song selection stuff
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String song = dataSet.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.video_title_list_layout, parent, false);
            viewHolder.txtTitle =  convertView.findViewById(R.id.title);
            viewHolder.txtRuntime = convertView.findViewById(R.id.runtime);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        lastPosition = position;
        Log.d("song-SLVA",song);
        viewHolder.txtTitle.setText(song.substring(0, song.lastIndexOf(":")));
        String runtime = song.substring(song.lastIndexOf(":")+1);

        try {
            viewHolder.txtRuntime.setText(runtime.substring(2, runtime.lastIndexOf("M")) + ":" + runtime.substring(runtime.lastIndexOf("M")+1, runtime.length()-1));
        } catch (StringIndexOutOfBoundsException e) {
            viewHolder.txtRuntime.setText("0:" + runtime.substring(runtime.length()-2, runtime.length()-1));
        }

        // Return the completed view to render on screen
        return convertView;
    }
}