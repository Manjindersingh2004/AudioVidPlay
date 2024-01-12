package com.example.multimediamanager;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

public class videoPlayerActivity extends AppCompatActivity {

    VideoView videoView;
    CustomMediaController mediaController;
    int Rotation=0;
    private int currentPosition = 0;
    private boolean isVideoPaused = false;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (videoView != null) {
            currentPosition = videoView.getCurrentPosition();
            isVideoPaused = !videoView.isPlaying();
        }
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentPosition = savedInstanceState.getInt("currentPosition", 0);
        isVideoPaused = savedInstanceState.getBoolean("isVideoPaused", false);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        videoView = findViewById(R.id.video);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//full screen


        Intent i = getIntent();
        Uri path = Uri.parse(i.getStringExtra("path"));
        videoView.setVideoURI(path);

        mediaController = new CustomMediaController(this);
        videoView.setMediaController(mediaController);
        if (mediaPlayerClass.mediaPlayer != null && mediaPlayerClass.mediaPlayer.isPlaying()) {
            mediaPlayerClass.mediaPlayer.pause();
        }
        videoView.start();


        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        });


        LinearLayout overlayLayout = findViewById(R.id.overlayLayout);
        overlayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the media controller
                if(mediaController!=null){
                   mediaController.toggleController();

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Keep the screen on while the video is playing
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (isVideoPaused) {
            videoView.pause();
        } else {
            videoView.seekTo(currentPosition);
            videoView.start();
        }
    }

}

