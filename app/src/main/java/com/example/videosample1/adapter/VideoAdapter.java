package com.example.videosample1.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videosample1.R;
import com.example.videosample1.VideoDetailsActivity;
import com.example.videosample1.viewholder.VideoViewHolder;
import com.example.videosample1.model.Video;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder> {
    private static final String TAG = "VideoAdapter";
    private Context context;
    private ArrayList<Video> playerIdx;

    public VideoAdapter(Context context,ArrayList<Video> playerIdx){
        this.context = context;
        this.playerIdx = playerIdx;
    }
    @NonNull
    @NotNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View videoView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video,parent,false);
        return new VideoViewHolder(videoView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull VideoViewHolder holder, int position) {
        startPlayer(holder.playerView,playerIdx.get(position).getSources().get(0));
        holder.textView.setText(playerIdx.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoDetailsActivity.class);
                intent.putExtra("url",playerIdx.get(position).getSources().get(0));
                intent.putExtra("seek",holder.playerView.getCurrentPosition());
                intent.putExtra("title",playerIdx.get(position).getTitle());
                intent.putExtra("description",playerIdx.get(position).getDescription());

                ViewCompat.setTransitionName(holder.playerView, playerIdx.get(position).getTitle());

                Log.e(TAG, "onClick: "+ViewCompat.getTransitionName(holder.playerView) );
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        (AppCompatActivity)context,
                        holder.playerView,
                        ViewCompat.getTransitionName(holder.playerView));

                ActivityCompat.startActivity(context, intent, options.toBundle());
            }
        });
    }

    private void startPlayer(VideoView videoView, String sampleUrl){
        //specify the location of media file
        Uri uri=Uri.parse(sampleUrl);
        videoView.setVideoURI(uri);
        videoView.setBackground(ContextCompat.getDrawable(context,R.drawable.ic_launcher_background));
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(0f, 0f);
            }
        });
//        videoView.start();


        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {

                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    videoView.setBackground(null);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return playerIdx.size();
    }


}
