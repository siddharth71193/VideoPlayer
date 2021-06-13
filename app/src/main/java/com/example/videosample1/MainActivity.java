package com.example.videosample1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.VideoView;

import com.example.videosample1.adapter.VideoAdapter;
import com.example.videosample1.model.Video;
import com.example.videosample1.model.VideoModel;
import com.example.videosample1.util.CommonUtils;
import com.example.videosample1.viewholder.VideoViewHolder;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Video> videoModels = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private int previousVideoIdx = -1;
    private boolean firstTimeLoad = false;
    private int seekTo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firstTimeLoad = true;
        
        parseVideoData();

        setUpRecyclerView();
    }

    private void parseVideoData(){
        VideoModel videoModel = CommonUtils.parseJson(this);
        videoModels.addAll(videoModel.getCategories().get(0).getVideos());
    }

    private void setUpRecyclerView(){
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        VideoAdapter videoAdapter = new VideoAdapter(this,videoModels);
        mRecyclerView.setAdapter(videoAdapter);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull @NotNull View view) {
            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull @NotNull View view) {
                view.findViewById(R.id.videoPlayer1).setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.ic_launcher_background));
                ((VideoView)view.findViewById(R.id.videoPlayer1)).stopPlayback();
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                playVideo((LinearLayoutManager) mRecyclerView.getLayoutManager());
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        mRecyclerView
                .getViewTreeObserver()
                .addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                // At this point the layout is complete and the
                                // dimensions of recyclerView and any child views
                                // are known.
                                playVideo((LinearLayoutManager)mRecyclerView.getLayoutManager());
                                mRecyclerView
                                        .getViewTreeObserver()
                                        .removeOnGlobalLayoutListener(this);
                            }
                        });
    }

    private void playVideo(LinearLayoutManager linearLayoutManager){
        int firstIdx = linearLayoutManager.findFirstVisibleItemPosition();
        int lastIdx = linearLayoutManager.findLastVisibleItemPosition();

        int centerIdx = (firstIdx+lastIdx)/2;
        if(centerIdx == previousVideoIdx)
            return;
        stopVideo();
        previousVideoIdx = centerIdx;

        VideoViewHolder videoViewHolder = (VideoViewHolder) mRecyclerView.findViewHolderForAdapterPosition(centerIdx);
        videoViewHolder.playVideo();
    }

    private void playOnResume(){
        int firstIdx = ((LinearLayoutManager)mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        int lastIdx = ((LinearLayoutManager)mRecyclerView.getLayoutManager()).findLastVisibleItemPosition();

        int centerIdx = (firstIdx+lastIdx)/2;
        previousVideoIdx = centerIdx;

        VideoViewHolder videoViewHolder = (VideoViewHolder) mRecyclerView.findViewHolderForAdapterPosition(centerIdx);
        videoViewHolder.playVideo();
        videoViewHolder.setSeekTo(seekTo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!firstTimeLoad)
            playOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        firstTimeLoad = false;
        VideoViewHolder videoViewHolder = (VideoViewHolder) mRecyclerView.findViewHolderForAdapterPosition(previousVideoIdx);
        if(videoViewHolder != null) {
            seekTo = videoViewHolder.getSeekTo();
        }
    }

    private void stopVideo(){
        if(previousVideoIdx != -1) {
            VideoViewHolder videoViewHolder = (VideoViewHolder) mRecyclerView.findViewHolderForAdapterPosition(previousVideoIdx);
            if(videoViewHolder != null)
                videoViewHolder.stopVideo();
        }
    }

}