package com.digitalnode.playsee;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private String[] videoIds;
    private Lifecycle lifecycle;
    private static Map<String, Integer> orderStr = new HashMap<String, Integer>();
    private static ArrayList<String> orderInt = new ArrayList<>();

    private static int videoListSize;

    RecyclerViewAdapter(String[] videoIds, Lifecycle lifecycle) {
        this.videoIds = videoIds;
        this.lifecycle = lifecycle;

        for(int i = 0; i < videoIds.length; i++) { orderStr.put(videoIds[i], i); }
        for(int i = 0; i < videoIds.length; i++) { orderInt.add(videoIds[i]); }

        Log.d("orderStr", orderStr.toString());
        Log.d("orderInt", orderInt.toString());

        videoListSize = videoIds.length;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) LayoutInflater.from(parent.getContext()).inflate(R.layout.video_view_layout, parent, false);
        youTubePlayerView.getPlayerUIController().showFullscreenButton(true);
        lifecycle.addObserver(youTubePlayerView);


        return new ViewHolder(youTubePlayerView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.cueVideo(videoIds[position]);
        viewHolder.youTubePlayerView.initialize(initializedYouTubePlayer ->
                initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady() {
                        viewHolder.youTubePlayer = initializedYouTubePlayer;
                        viewHolder.youTubePlayer.cueVideo(videoIds[position], 0);
                    }
                    // playNext() instead
                    @Override
                    public void onStateChange(@NonNull PlayerConstants.PlayerState state) {
                        if(state.equals(PlayerConstants.PlayerState.ENDED))
                        {
                            Log.d("nextId_mod", orderInt.get(position+1));
                            if(position+1 < videoListSize) {
                                viewHolder.youTubePlayer.loadVideo(orderInt.get(position+1), 0);
                                Log.d("nextVid", orderInt.get(position+1));
                            }
                        }
                    }

                    @Override
                    public void onPlaybackQualityChange(@NonNull PlayerConstants.PlaybackQuality playbackQuality) { }

                    @Override
                    public void onPlaybackRateChange(@NonNull PlayerConstants.PlaybackRate playbackRate) { }

                    @Override
                    public void onError(@NonNull PlayerConstants.PlayerError error) { }

                    @Override
                    public void onApiChange() { }

                    @Override
                    public void onCurrentSecond(float second) { }

                    @Override
                    public void onVideoDuration(float duration) { }

                    @Override
                    public void onVideoLoadedFraction(float loadedFraction) { }

                    @Override
                    public void onVideoId(@NonNull String videoId) { }
                }), true);
        // put the code for each here!!

    }

    @Override
    public int getItemCount() {
        return videoIds.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private YouTubePlayerView youTubePlayerView;
        private YouTubePlayer youTubePlayer;
        private String currentVideoId;
        private String nextVideoId;

        ViewHolder(YouTubePlayerView playerView) {
            super(playerView);
            youTubePlayerView = playerView;

            youTubePlayerView.initialize(initializedYouTubePlayer ->
                    initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady() {
                            youTubePlayer = initializedYouTubePlayer;
                            youTubePlayer.cueVideo(currentVideoId, 0);
                        }
                    }), true);
            youTubePlayerView.enterFullScreen();
        }

        void cueVideo(String videoId) {
            if(videoId != null) {
                currentVideoId = videoId;

                if (youTubePlayer == null)
                    return;

                //youTubePlayer.cueVideo(videoId, 0);
            }
        }
    }
}