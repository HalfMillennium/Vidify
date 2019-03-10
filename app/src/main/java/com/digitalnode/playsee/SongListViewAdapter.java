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
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SongListViewAdapter extends ArrayAdapter<String> implements View.OnClickListener{

    private final String TAG = "SongListViewActivity";

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

        Log.d(TAG, song);

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
        //song.substring(0, song.lastIndexOf(":"))
        String title = song.substring(0, song.lastIndexOf(":"));
        //Log.d("song subs", song.substring(song.lastIndexOf(":")+1));
        viewHolder.txtTitle.setText(title);
        String runtime = song.substring(song.lastIndexOf(":")+1);
        ConvertTime convert = new ConvertTime(song.substring(song.lastIndexOf(":")+1));
        viewHolder.txtRuntime.setText(convert.getFmtTime());

        /*
        try {
            viewHolder.txtRuntime.setText(runtime.substring(2, runtime.lastIndexOf("M")) + ":" + runtime.substring(runtime.lastIndexOf("M")+1, runtime.length()-1));
        } catch (StringIndexOutOfBoundsException e) {
            viewHolder.txtRuntime.setText("0:" + runtime.substring(runtime.length()-2, runtime.length()-1));
        }*/

        // Return the completed view to render on screen
        return convertView;
    }
}