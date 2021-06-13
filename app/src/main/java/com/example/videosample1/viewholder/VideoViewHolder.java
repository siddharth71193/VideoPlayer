package com.example.videosample1.viewholder;

import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videosample1.R;

public class VideoViewHolder extends RecyclerView.ViewHolder {
    public VideoView playerView;
    public TextView textView;

    public VideoViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
        super(itemView);
        playerView = itemView.findViewById(R.id.videoPlayer1);
        textView = itemView.findViewById(R.id.details);
    }

    public void playVideo() {
        playerView.start();
    }

    public void stopVideo() {
        playerView.stopPlayback();
        playerView.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_launcher_background));
    }

    public int getSeekTo(){
        return playerView.getCurrentPosition();
    }

    public void setSeekTo(int seekTo){
        playerView.seekTo(seekTo);
    }
}
