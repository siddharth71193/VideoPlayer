package com.example.videosample1;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.videosample1.util.Constants;

public class VideoDetailsActivity extends AppCompatActivity {
    private static final String TAG = "VideoDetailsActivity";
    private String mUrl;
    private VideoView mVideoView;
    private int duration;
    private TextView textView;
    private View constraintLayout;

    private float xCoOrdinate, yCoOrdinate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_details);

        initView();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView(){
        mVideoView = findViewById(R.id.videoPlayer2);
        textView = findViewById(R.id.details1);
        constraintLayout = findViewById(R.id.layout);

        mUrl = getIntent().getExtras().getString(Constants.IntentExtraKeys.VIDEO_URL);
        duration = getIntent().getExtras().getInt(Constants.IntentExtraKeys.SEEKBAR_POSITION);
        String imageTransitionName = getIntent().getExtras().getString(Constants.IntentExtraKeys.VIDEO_TRANSITION_NAME);
        String description = getIntent().getExtras().getString(Constants.IntentExtraKeys.VIDEO_DESCRIPTION);

        textView.setText(description);

        mVideoView.setTransitionName(imageTransitionName);
        Log.e(TAG, "onCreate: "+ imageTransitionName );
        startPlayer(mVideoView,mUrl,duration);

        mVideoView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        xCoOrdinate = mVideoView.getX() - event.getRawX();
                        yCoOrdinate = mVideoView.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        constraintLayout.animate().x(event.getRawX() + xCoOrdinate).y(event.getRawY() + yCoOrdinate).setDuration(0).start();
                        break;
                    case MotionEvent.ACTION_UP:
                        supportFinishAfterTransition();
                    default:
                        return false;
                }
                return true;
            }
        });
    }

    private void startPlayer(VideoView videoView, String sampleUrl,int duration){
        Uri uri=Uri.parse(sampleUrl);

        videoView.setMediaController(null);
        videoView.setVideoURI(uri);
        videoView.seekTo(duration);
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(100f, 100f);
            }
        });
        videoView.start();
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }
}